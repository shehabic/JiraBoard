package com.fullmob.jiraapi.api;

import com.fullmob.jiraapi.requests.LoginRequest;
import com.fullmob.jiraapi.responses.AuthResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface AuthApi {

    @POST("/rest/auth/1/session")
    Observable<Response<AuthResponse>> login(@Body LoginRequest loginRequest);

    @POST
    Observable<Response<AuthResponse>> login(@Url String url, @Body LoginRequest loginRequest);
}
