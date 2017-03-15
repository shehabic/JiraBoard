package com.fullmob.jiraboard.managers.issues;

import com.fullmob.jiraapi.managers.IssuesApiClient;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.responses.SearchResults;

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
}
