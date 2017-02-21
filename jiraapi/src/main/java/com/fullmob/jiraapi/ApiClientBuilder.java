package com.fullmob.jiraapi;

import com.fullmob.jiraapi.config.ApiConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientBuilder<T> {
    private final Class<T> api;
    private ApiConfig apiConfig;
    private OkHttpClient httpClient;

    public ApiClientBuilder(Class<T> api) {
        this.api = api;
    }

    public ApiClientBuilder<T> setConfig(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;

        return this;
    }

    public T build() {
        if (this.httpClient == null) {
            HttpClientBuilder httpClientBuilder = new HttpClientBuilder(apiConfig);
            this.httpClient = httpClientBuilder.build();
        }

        Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(apiConfig.getBaseUrl())
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

        return retrofit.create(api);
    }

    public ApiClientBuilder<T> setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;

        return this;
    }
}
