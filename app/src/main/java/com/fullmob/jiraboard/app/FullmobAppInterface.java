package com.fullmob.jiraboard.app;

import android.content.Context;

import com.fullmob.jiraapi.HttpClientBuilder;
import com.fullmob.jiraboard.di.components.ApiComponent;
import com.fullmob.jiraboard.di.components.LoginScreenComponent;
import com.fullmob.jiraboard.di.components.MainComponent;
import com.fullmob.jiraboard.ui.login.LoginView;

public interface FullmobAppInterface {
    Context getContext();
    MainComponent getMainComponent();
    void addDebugInterceptors(HttpClientBuilder builder);

    public LoginScreenComponent createHomeScreenComponent();

    LoginScreenComponent createLoginScreenComponent(LoginView loginView);

    ApiComponent getApiComponent();
}
