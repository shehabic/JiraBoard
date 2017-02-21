package com.fullmob.jiraapi.api;

import com.fullmob.jiraapi.responses.Project;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface ProjectsApi {

    @GET("rest/api/2/project")
    Observable<Response<List<Project>>> getProjects();
}
