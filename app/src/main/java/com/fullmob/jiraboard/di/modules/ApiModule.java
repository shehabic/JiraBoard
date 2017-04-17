package com.fullmob.jiraboard.di.modules;

import com.fullmob.jiraapi.ApiClientBuilder;
import com.fullmob.jiraapi.HttpClientBuilder;
import com.fullmob.jiraapi.api.AuthApi;
import com.fullmob.jiraapi.api.IssuesApi;
import com.fullmob.jiraapi.api.ProjectsApi;
import com.fullmob.jiraapi.api.UserApi;
import com.fullmob.jiraapi.config.ApiConfig;
import com.fullmob.jiraapi.managers.AuthManager;
import com.fullmob.jiraapi.managers.IssuesApiClient;
import com.fullmob.jiraapi.managers.JiraCloudUserManager;
import com.fullmob.jiraapi.managers.ProjectsApiClient;
import com.fullmob.jiraboard.BuildConfig;
import com.fullmob.jiraboard.app.FullmobAppInterface;
import com.fullmob.jiraboard.di.scopes.UserScope;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.managers.user.AuthenticationManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@UserScope
@Module
public class ApiModule {

    @UserScope
    @Provides
    public ApiConfig getApiConfig(EncryptedStorage encryptedStorage) {
        ApiConfig apiConfig = new ApiConfig();
        String subDomain = encryptedStorage.getSubDomain();
        if (subDomain == null) {
            subDomain = "test";
        }
        apiConfig.setBaseUrl("https://" + subDomain + BuildConfig.JIRA_CLOUD);
        apiConfig.setUser(encryptedStorage.getUsername());
        apiConfig.setPassword(encryptedStorage.getPassword());
        apiConfig.setDebug(BuildConfig.DEBUG);

        return apiConfig;
    }

    @UserScope
    @Provides
    public IssuesApiClient providesIssuesManager(OkHttpClient httpClient, ApiConfig apiConfig) {
        return new IssuesApiClient(getApi(IssuesApi.class, apiConfig, httpClient));
    }

    @UserScope
    @Provides
    public JiraCloudUserManager providesJiraCloudUserManager(OkHttpClient httpClient, ApiConfig apiConfig) {
        return new JiraCloudUserManager(getApi(UserApi.class, apiConfig, httpClient));
    }

    @UserScope
    @Provides
    public ProjectsApiClient providesProjectsManager(OkHttpClient httpClient, ApiConfig apiConfig) {
        return new ProjectsApiClient(getApi(ProjectsApi.class, apiConfig, httpClient));
    }

    @UserScope
    @Provides
    public AuthManager providesAuthManager(OkHttpClient httpClient, ApiConfig apiConfig) {
        return new AuthManager(getApi(AuthApi.class, apiConfig, httpClient));
    }

    @UserScope
    @Provides
    public OkHttpClient providesOkHttpClient(ApiConfig apiConfig, FullmobAppInterface app) {
        HttpClientBuilder builder = new HttpClientBuilder(apiConfig);
        app.addDebugInterceptors(builder);

        return builder.build();
    }

    @UserScope
    @Provides
    public AuthenticationManager providesAuthenticationManager(AuthManager auth, EncryptedStorage encryption) {
        return new AuthenticationManager(auth, encryption);
    }

    private <T> T getApi(Class<T> api, ApiConfig apiConfig, OkHttpClient httpClient) {
        return new ApiClientBuilder<>(api).setHttpClient(httpClient).setConfig(apiConfig).build();
    }
}
