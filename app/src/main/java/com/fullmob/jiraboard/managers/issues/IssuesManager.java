package com.fullmob.jiraboard.managers.issues;

import com.fullmob.jiraapi.managers.IssuesApiClient;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.models.issue.Status;
import com.fullmob.jiraapi.requests.issue.Transition;
import com.fullmob.jiraapi.responses.IssueTransitionsResponse;
import com.fullmob.jiraapi.responses.SearchResults;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.db.Mapper;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;
import com.fullmob.jiraboard.ui.models.UIIssueTransition;
import com.fullmob.jiraboard.ui.models.UIIssueTransitionGroups;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class IssuesManager {
    private final IssuesApiClient api;
    private final DBManagerInterface dbManager;
    private final Mapper mapper;

    public IssuesManager(IssuesApiClient issuesApiClient, DBManagerInterface dbManager) {
        this.api = issuesApiClient;
        this.dbManager = dbManager;
        this.mapper = new Mapper();
    }

    public Issue getIssueSync(String issueKey) throws IOException {
        return api.getIssueSync(issueKey).body();
    }

    public Observable<Response<Issue>> getIssue(String issueKey) {
        return getIssue(issueKey, "none");
    }

    private Observable<Response<Issue>> getIssue(String issueKey, String expand) {
        return api.getIssue(issueKey, expand);
    }

    public Observable<Response<Issue>> getIssueForRendering(String issueKey) {
        return getIssue(issueKey, "renderedFields");
    }

    public Observable<Response<SearchResults>> search(String projectKey, String searchText) {
        return search(projectKey, searchText, 10);
    }

    public Observable<Response<SearchResults>> search(String projectKey, String searchText, int limit) {
        return search(projectKey, searchText, limit, 0);
    }

    public Observable<Response<SearchResults>> search(String projectKey, String searchText, int limit, int offset) {
        return search(projectKey, searchText, limit, offset, "fields=key,id,summary,status,issuetype");
    }

    public Observable<Response<SearchResults>> search(String projectKey, String searchText, int limit, int offset, String fields) {
        return api.search(projectKey, searchText, limit, offset, fields);
    }

    public Observable<UIIssueTransitionGroups> getPossibleTransitions(Issue issue) {
        return getAllPossibleTransitions(issue);
    }

    private Observable<UIIssueTransitionGroups> getAllPossibleTransitions(Issue issue) {
        final Status status = issue.getIssueFields().getStatus();
        String projectId = issue.getIssueFields().getProject().getId();
        HashSet<UIIssueTransition> uiTransitions = dbManager.findDistinctTransitionsForIssue(issue);

        return uiTransitions.size() == 0
            ? fetchPossibleTransitionsFromServer(issue, status)
            : returnPossibleTransitionsFromDB(status, uiTransitions, projectId);
    }

    private Observable<UIIssueTransitionGroups> returnPossibleTransitionsFromDB(
        Status status,
        HashSet<UIIssueTransition> uiTransitions,
        String projectId
    ) {
        HashMap<String, UIIssueStatus> statuses = getUniqueStatuses(projectId);
        UIIssueTransitionGroups transitionGroups = new UIIssueTransitionGroups();
        HashSet<String> added = new HashSet<>();
        List<UIIssueTransition> transitions = sortDirectTransitionsFirst(status, uiTransitions);
        for (UIIssueTransition uiIssueTransition : transitions) {
            if (!added.contains(uiIssueTransition.toName) && !uiIssueTransition.toName.equals(status.getName())) {
                if (statuses.containsKey(uiIssueTransition.toName)) {
                    uiIssueTransition.toColor = statuses.get(uiIssueTransition.toName).getStatusCategory().getColorName();
                }
                if (statuses.containsKey(uiIssueTransition.fromName)) {
                    uiIssueTransition.fromColor = statuses.get(uiIssueTransition.fromName).getStatusCategory().getColorName();
                }
                added.add(uiIssueTransition.toName);
                if (isDirectTransition(status, uiIssueTransition)) {
                    transitionGroups.getDirectTransitions().add(uiIssueTransition);
                } else {
                    transitionGroups.getFurtherTransitions().add(uiIssueTransition);
                }
            }
        }

        sortByColor(transitionGroups);

        return Observable.just(transitionGroups);
    }

    private List<UIIssueTransition> sortDirectTransitionsFirst(final Status status, HashSet<UIIssueTransition> uiTransitions) {
        List<UIIssueTransition> transitions = new ArrayList<>(uiTransitions);
        Collections.sort(transitions, new Comparator<UIIssueTransition>() {
            @Override
            public int compare(UIIssueTransition o1, UIIssueTransition o2) {
                int val1 = o1.fromName.toLowerCase().equals(status.getName().toLowerCase()) ? 1 : 2;
                int val2 = o2.fromName.toLowerCase().equals(status.getName().toLowerCase()) ? 1 : 2;

                return val1 - val2;
            }
        });

        return transitions;
    }

    public HashMap<String, UIIssueStatus> getUniqueStatuses(String projectId) {
        HashSet<UIIssueStatus> statuses = dbManager.findProjectIssueStatuses(projectId);
        HashMap<String, UIIssueStatus> map = new HashMap<>();
        for (UIIssueStatus status : statuses) {
            map.put(status.getName(), status);
        }

        return map;
    }

    public Observable<UIIssueTransitionGroups> fetchPossibleTransitionsFromServer(Issue issue, final Status status) {
        try {
            return api.getPossibleIssueTransitions(issue.getKey())
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Response<IssueTransitionsResponse>, ObservableSource<UIIssueTransitionGroups>>() {
                    @Override
                    public ObservableSource<UIIssueTransitionGroups> apply(Response<IssueTransitionsResponse> response) throws Exception {
                        UIIssueTransitionGroups transitionGroups = new UIIssueTransitionGroups();
                        if (response.body() != null && response.body().getTransitions() != null) {
                            for (Transition transition : response.body().getTransitions()) {
                                transitionGroups.getDirectTransitions().add(
                                    mapper.createUIIssueTransitionsFromDirectTransition(transition, status)
                                );
                            }
                        }
                        sortByColor(transitionGroups);

                        return Observable.just(transitionGroups);
                    }
                });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void sortByColor(UIIssueTransitionGroups transitionGroups) {
        sortUITransitionsByColor(transitionGroups.getDirectTransitions());
        sortUITransitionsByColor(transitionGroups.getFurtherTransitions());
    }

    private void sortUITransitionsByColor(List<UIIssueTransition> transitionList) {
        Collections.sort(transitionList, new Comparator<UIIssueTransition>() {
            @Override
            public int compare(UIIssueTransition o1, UIIssueTransition o2) {
                int val1 = valForColor(o1.toColor);
                int val2 = valForColor(o2.toColor);

                return (val1 == val2) ? o1.toName.compareTo(o2.toName) : val1 - val2;
            }

            private int valForColor(String colorName) {
                if (colorName.toLowerCase().equals("yellow")) {
                    return 1;
                } else if (colorName.toLowerCase().equals("green")) {
                    return 2;
                }

                return 0;
            }
        });

    }

    private boolean isDirectTransition(Status status, UIIssueTransition uiIssueTransition) {
        return uiIssueTransition.fromName.toLowerCase().equals(status.getName().toLowerCase());
    }

    public Response moveIssue(String key, String targetId) throws IOException {
        return api.moveIssueSync(key, targetId);
    }
}
