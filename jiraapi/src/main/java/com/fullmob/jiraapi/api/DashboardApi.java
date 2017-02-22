package com.fullmob.jiraapi.api;

import com.fullmob.jiraapi.responses.DashboardsResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface DashboardApi {

    @GET("dashboard")
    Observable<Response<DashboardsResponse>> getDashboards();
}
