package com.fullmob.jiraapi.api;

import com.fullmob.jiraapi.models.Priority;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface PriorityApi {

    @GET("priority")
    Observable<Response<List<Priority>>> getPriorities();
}
