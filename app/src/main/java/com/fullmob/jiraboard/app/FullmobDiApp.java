package com.fullmob.jiraboard.app;

import android.app.Application;
import android.content.Context;

import com.fullmob.jiraapi.HttpClientBuilder;
import com.fullmob.jiraboard.di.components.ApiComponent;
import com.fullmob.jiraboard.di.components.DaggerMainComponent;
import com.fullmob.jiraboard.di.components.LoginScreenComponent;
import com.fullmob.jiraboard.di.components.MainComponent;
import com.fullmob.jiraboard.di.modules.LoginScreenModule;
import com.fullmob.jiraboard.di.modules.MainModule;
import com.fullmob.jiraboard.ui.login.LoginView;

public class FullmobDiApp extends Application implements FullmobAppInterface {

    private MainComponent mainComponent;
    private ApiComponent apiComponent;

    protected void initDI() {
        mainComponent = DaggerMainComponent.builder()
            .mainModule(new MainModule(this))
            .build();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public MainComponent getMainComponent() {
        return mainComponent;
    }

    @Override
    public void addDebugInterceptors(HttpClientBuilder builder) {

    }

    @Override
    public LoginScreenComponent createHomeScreenComponent() {
        return null;
    }

    @Override
    public LoginScreenComponent createLoginScreenComponent(LoginView loginView) {
        return getApiComponent().plus(new LoginScreenModule(loginView));
    }

    @Override
    public ApiComponent getApiComponent() {
        if (apiComponent == null) {
            apiComponent = mainComponent.plus();
        }

        return apiComponent;
    }
}
