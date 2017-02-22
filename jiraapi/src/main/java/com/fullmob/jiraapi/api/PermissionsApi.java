package com.fullmob.jiraapi.api;

import com.fullmob.jiraapi.models.MyPermissions;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface PermissionsApi {

    @GET("mypermissions")
    Observable<Response<MyPermissions>> getMyPermissions();
}
