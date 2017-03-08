package com.fullmob.jiraboard.managers.issues;

import com.fullmob.jiraapi.managers.IssuesApiClient;
import com.fullmob.jiraapi.models.Issue;

import io.reactivex.Observable;
import retrofit2.Response;

public class IssuesManager {
    private final IssuesApiClient api;

    public IssuesManager(IssuesApiClient issuesApiClient) {
        this.api = issuesApiClient;
    }

    public Observable<Response<Issue>> getIssue(String issueKey) {
        return api.getIssue(issueKey);
    }
}
