package com.fullmob.jiraapi.api;

import com.fullmob.jiraapi.models.Resolution;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface ResolutionsApi {

    @GET("resolution")
    Observable<Response<List<Resolution>>> getResolutions();
}
