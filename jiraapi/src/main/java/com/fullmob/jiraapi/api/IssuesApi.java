package com.fullmob.jiraapi.api;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.models.issue.Issuetype;
import com.fullmob.jiraapi.models.issue.Status;
import com.fullmob.jiraapi.requests.CommentRequest;
import com.fullmob.jiraapi.requests.TransitionRequest;
import com.fullmob.jiraapi.requests.issue.UpdateIssueRequest;
import com.fullmob.jiraapi.responses.IssueTransitionsResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IssuesApi {

    // Async (RX)

    @GET("issuetype")
    Observable<Response<List<Issuetype>>> getIssueTypes();

    @GET("status")
    Observable<Response<List<Status>>> getIssueStatuses();

    @GET("issue/{id}")
    Observable<Response<Issue>> getIssue(@Path("id") String issueId);

    @POST("issue/{id}/comment")
    Observable<Response<Issue>> addComment(@Path("id") String issueId, @Body CommentRequest commentRequest);

    @PUT("issue/{id}")
    Observable<Response<Issue>> updateIssue(@Path("id") String issueId, @Body UpdateIssueRequest updateIssueRequest);

    @GET("issue/{id}/transitions")
    Observable<Response<IssueTransitionsResponse>> getPossibleIssueTransitions(@Path("id") String issueId);

    @POST("issue/{id}/transitions")
    Observable<Response> moveIssue(@Path("id") String issueId, @Body TransitionRequest updateIssueRequest);

    // Synchronous

    @GET("issue/{id}")
    Call<Issue> getIssueSync(@Path("id") String issueId);

    @GET("issue/{id}/transitions")
    Call<IssueTransitionsResponse> getPossibleIssueTransitionsSync(@Path("id") String issueId, @Query("r") String rand);

    @POST("issue/{id}/transitions")
    Call<ResponseBody> moveIssueSync(@Path("id") String issueId, @Body TransitionRequest updateIssueRequest);

    @GET("issuetype/{id}")
    Call<Issuetype> getIssueTypeAsync(@Path("id") String id);
}
