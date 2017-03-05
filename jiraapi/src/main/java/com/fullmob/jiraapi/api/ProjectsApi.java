package com.fullmob.jiraapi.api;

import com.fullmob.jiraapi.models.Project;
import com.fullmob.jiraapi.models.ProjectIssueTypeStatus;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProjectsApi {

    @GET("project")
    Observable<Response<List<Project>>> getProjects();

    @GET("project/{id}/statuses")
    Observable<Response<List<ProjectIssueTypeStatus>>> getProjectIssueStatuses(@Path("id") String projectId);

    // Sync

    @GET("project/{id}/statuses")
    Call<List<ProjectIssueTypeStatus>> getProjectIssueStatusesSync(@Path("id") String projectId);

    @GET("project")
    Call<List<Project>> getProjectsSync();
}
