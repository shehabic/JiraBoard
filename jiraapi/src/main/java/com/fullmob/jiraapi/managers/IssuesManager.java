package com.fullmob.jiraapi.managers;

import com.fullmob.jiraapi.api.IssuesApi;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.requests.TransitionRequest;
import com.fullmob.jiraapi.requests.issue.Transition;
import com.fullmob.jiraapi.responses.IssueTransitionsResponse;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class IssuesManager extends AbstractApiManager<IssuesApi> {

    public IssuesManager(IssuesApi issuesApi) {
        super(issuesApi);
    }

    public Observable<Response<Issue>> moveIssue(final String issueKey, final String toStateId) {
        return api.getPossibleIssueTransitions(issueKey)
            .subscribeOn(Schedulers.io())
            .concatMap(new Function<Response<IssueTransitionsResponse>, ObservableSource<Response>>() {
                @Override
                public ObservableSource<Response> apply(Response<IssueTransitionsResponse> response) throws Exception {
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
                    if (issueResponse.code() != 200) {
                        throw new Exception(issueResponse.errorBody().toString());
                    }

                    return api.getIssue(issueKey);
                }
            });
    }

    public Response<Issue> getIssueSync(String key) throws IOException {
        Response<Issue> response = api.getIssueSync(key).execute();

        return response;
    }

    public Response<IssueTransitionsResponse> getPossibleIssueTransitionsSync(String key) throws IOException {
        String rand = String.valueOf(System.currentTimeMillis());
        return api.getPossibleIssueTransitionsSync(key, rand).execute();
    }

    public Response moveIssueSync(String key, String transitionId) throws IOException {
        return api.moveIssueSync(key, new TransitionRequest(new Transition(transitionId))).execute();
    }
}
