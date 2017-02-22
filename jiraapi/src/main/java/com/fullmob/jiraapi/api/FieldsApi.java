package com.fullmob.jiraapi.api;

import com.fullmob.jiraapi.models.Field;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface FieldsApi {

    @GET("field")
    Observable<Response<List<Field>>> getFields();
}
