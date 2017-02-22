package com.fullmob.jiraapi.api;

import com.fullmob.jiraapi.models.User;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface UserApi {

    @GET("myself")
    Observable<Response<User>> getProfile();
}
