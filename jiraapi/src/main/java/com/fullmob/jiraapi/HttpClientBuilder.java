package com.fullmob.jiraapi;

import com.fullmob.jiraapi.config.ApiConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpClientBuilder {
    private final ApiConfig apiConfig;
    private Cache cache;
    private OkHttpClient.Builder builder;
    private List<Interceptor> interceptors;

    public HttpClientBuilder(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
        interceptors = new ArrayList<>();
    }

    private static void buildCache(OkHttpClient.Builder builder, Cache cache) {
        builder.cache(cache);
    }

    private void buildDefaultQueryParameters(OkHttpClient.Builder builder) {
        if (apiConfig.getUser() != null && apiConfig.getPassword() != null) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder builder = chain.request().newBuilder();
                    builder.addHeader("Authorization", Credentials.basic(apiConfig.getUser(), apiConfig.getPassword()));

                    return chain.proceed(builder.build());
                }
            });
        }
    }

    private void addDebugInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        interceptors.add(interceptor);
    }

    private void buildInterceptors() {
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }
    }

    public HttpClientBuilder setCache(Cache cache) {
        this.cache = cache;

        return this;
    }

    public HttpClientBuilder setOkHttpBuilder(OkHttpClient.Builder builder) {
        this.builder = builder;

        return this;
    }

    public HttpClientBuilder addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);

        return this;
    }

    public OkHttpClient build() {
        if (this.builder == null) {
            builder = new OkHttpClient.Builder();
        }
        if (apiConfig.isDebug()) {
            addDebugInterceptor();
        }
        if (this.cache != null) {
            buildCache(builder, cache);
        }
        buildDefaultQueryParameters(builder);
        buildInterceptors();

        return builder.build();
    }
}
