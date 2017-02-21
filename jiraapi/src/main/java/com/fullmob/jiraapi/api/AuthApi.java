package com.fullmob.jiraapi.api;

import com.fullmob.jiraapi.requests.LoginRequest;
import com.fullmob.jiraapi.responses.AuthResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("rest/auth/1/session")
    Observable<Response<AuthResponse>> login(@Body LoginRequest loginRequest);
}
