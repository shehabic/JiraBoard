package com.fullmob.graphlib;

import android.support.annotation.NonNull;

import com.fullmob.jiraapi.ApiClientBuilder;
import com.fullmob.jiraapi.BuildConfig;
import com.fullmob.jiraapi.HttpClientBuilder;
import com.fullmob.jiraapi.api.AuthApi;
import com.fullmob.jiraapi.api.FieldsApi;
import com.fullmob.jiraapi.api.IssuesApi;
import com.fullmob.jiraapi.api.ProjectsApi;
import com.fullmob.jiraapi.api.ResolutionsApi;
import com.fullmob.jiraapi.config.ApiConfig;

import java.util.concurrent.CountDownLatch;

class BaseUnitTest {
    int LOCK_WAIT_IN_SECONDS = 20;
    CountDownLatch lock = new CountDownLatch(1);

    private static final String BASE_URL = "https://" + BuildConfig.JIRA_SUB_DOMAIN + ".atlassian.net/rest/api/2/";
    static final String USERNAME = BuildConfig.JIRA_USERNAME;
    static final String PASSWORD = BuildConfig.JIRA_PASSWORD;
//    static final String TEST_TICKET_ID = BuildConfig.JIRA_TEST_TICKET_ID;
//    static final String TEST_TICKET_ID = "PFM-6550";
//    static final String TEST_TICKET_ID = "MOB-4943";
    static final String TEST_TICKET_ID = "PFM-6595";
//    static final String TEST_TICKET_ID = "LOG-1189";
//    static final String TEST_TICKET_ID = "PTE-476";

    AuthApi getAuthApi() {
        return getApi(AuthApi.class, getNonAuthenticatedApiConfig());
    }

    IssuesApi getIssuesApi() {
        return getApi(IssuesApi.class, getAuthenticatedApiConfig());
    }

    FieldsApi getFieldsApi() {
        return getApi(FieldsApi.class, getAuthenticatedApiConfig());
    }

    ProjectsApi getProjectsApi() {
        return getApi(ProjectsApi.class, getAuthenticatedApiConfig());
    }

    ResolutionsApi getResolutionsApi() {
        return getApi(ResolutionsApi.class, getAuthenticatedApiConfig());
    }


    @NonNull
    private ApiConfig getAuthenticatedApiConfig() {
        ApiConfig apiConfig = getNonAuthenticatedApiConfig();
        apiConfig.setUser(USERNAME);
        apiConfig.setPassword(PASSWORD);

        return apiConfig;
    }

    @NonNull
    private ApiConfig getNonAuthenticatedApiConfig() {
        ApiConfig apiConfig = new ApiConfig();
        apiConfig.setBaseUrl(BASE_URL);
        apiConfig.setDebug(false);

        return apiConfig;
    }

    private <T> T getApi(Class<T> api, ApiConfig apiConfig) {
        ApiClientBuilder<T> builder = new ApiClientBuilder<>(api);

        return builder.setHttpClient(new HttpClientBuilder(apiConfig).build()).setConfig(apiConfig).build();
    }

}
