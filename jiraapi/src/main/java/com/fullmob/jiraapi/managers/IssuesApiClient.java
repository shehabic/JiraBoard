package com.fullmob.jiraapi.managers;

import com.fullmob.jiraapi.api.IssuesApi;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.models.issue.Issuetype;
import com.fullmob.jiraapi.requests.TransitionRequest;
import com.fullmob.jiraapi.requests.issue.Transition;
import com.fullmob.jiraapi.responses.IssueTransitionsResponse;
import com.fullmob.jiraapi.responses.SearchResults;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class IssuesApiClient extends AbstractApiManager<IssuesApi> {

    public IssuesApiClient(IssuesApi issuesApi) {
        super(issuesApi);
    }

    public Observable<Response<Issue>> getIssue(final String issueKey) {
        return getIssue(issueKey, null);
    }

    public Observable<Response<Issue>> getIssue(final String issueKey, String expandableFields) {
        return api.getIssue(issueKey, expandableFields).subscribeOn(Schedulers.io());
    }

    public Observable<Response<Issue>> moveIssue(final String issueKey, final String toStateId) {
        return api.getPossibleIssueTransitions(issueKey)
            .subscribeOn(Schedulers.io())
            .concatMap(new Function<Response<IssueTransitionsResponse>, ObservableSource<Response>>() {
                @Override
                public ObservableSource<Response> apply(Response<IssueTransitionsResponse> response) throws Exception {
                    response.raw().close();
                    for (Transition trans : response.body().getTransitions()) {
                        if (trans.getId().equals(toStateId)) {
                            return api.moveIssue(issueKey, new TransitionRequest(trans));
                        }
                    }
                    throw new Exception("Invalid transition");
                }
            })
            .flatMap(new Function<Response, ObservableSource<Response<Issue>>>() {
                @Override
                public ObservableSource<Response<Issue>> apply(Response issueResponse) throws Exception {
                    issueResponse.raw().close();
                    if (issueResponse.code() != 200) {
                        throw new Exception(issueResponse.errorBody().toString());
                    }

                    return api.getIssue(issueKey, "none");
                }
            });
    }

    public Response<Issue> getIssueSync(String key) throws IOException {
        return api.getIssueSync(key).execute();
    }

    public Response<IssueTransitionsResponse> getPossibleIssueTransitionsSync(String key) throws IOException {
        String rand = String.valueOf(System.currentTimeMillis());

        return api.getPossibleIssueTransitionsSync(key, rand).execute();
    }

    public Response moveIssueSync(String key, String transitionId) throws IOException {
        return api.moveIssueSync(key, new TransitionRequest(new Transition(transitionId))).execute();
    }

    public Response<Issuetype> getIssueTypeAsync(String issueTypeId) throws IOException {
        return api.getIssueTypeAsync(issueTypeId).execute();
    }

    public Observable<Response<SearchResults>> search(String projectKey, String searchText, int limit, int offset, String fields) {
        searchText = searchText.replaceAll("\"", "");
        String conditions = String.format("summary~\"%s\"", searchText);
        if (isIssueKey(projectKey, searchText)) {
            conditions = "key=" + projectKey + "-" + searchText.trim().replaceAll("\\D", "");
        }
        String jql = String.format("project=%s AND (%s)", projectKey, conditions);

        return api.search(jql, limit, offset, fields);
    }

    private boolean isIssueKey(String projectKey, String searchText) {
        return searchText.trim().toUpperCase().startsWith(projectKey.toUpperCase() + "-")
            || !searchText.matches("\\D+");
    }
}
