package com.fullmob.jiraboard.app;

import android.app.Application;
import android.content.Context;

import com.fullmob.jiraapi.HttpClientBuilder;
import com.fullmob.jiraboard.DebugUtils;
import com.fullmob.jiraboard.di.components.ApiComponent;
import com.fullmob.jiraboard.di.components.DaggerMainComponent;
import com.fullmob.jiraboard.di.components.LoginScreenComponent;
import com.fullmob.jiraboard.di.components.MainComponent;
import com.fullmob.jiraboard.di.components.ManagersComponent;
import com.fullmob.jiraboard.di.components.ProjectsScreenComponent;
import com.fullmob.jiraboard.di.modules.LoginScreenModule;
import com.fullmob.jiraboard.di.modules.MainModule;
import com.fullmob.jiraboard.di.modules.ProjectsScreenModule;
import com.fullmob.jiraboard.ui.login.LoginView;
import com.fullmob.jiraboard.ui.projects.ProjectsView;

public class FullmobDiApp extends Application implements FullmobAppInterface {

    private MainComponent mainComponent;
    private ApiComponent apiComponent;
    private ManagersComponent managersComponent;

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
        DebugUtils.initDebugTools(this);
    }

    @Override
    public LoginScreenComponent createHomeScreenComponent() {
        return null;
    }

    @Override
    public LoginScreenComponent createLoginScreenComponent(LoginView loginView) {
        return getManagersModule().plus(new LoginScreenModule(loginView));
    }

    @Override
    public ApiComponent getApiComponent() {
        if (apiComponent == null) {
            apiComponent = mainComponent.plus();
        }

        return apiComponent;
    }

    @Override
    public ProjectsScreenComponent createProjectsScreenComponent(ProjectsView projectsView) {
        return getManagersModule().plusProjects(new ProjectsScreenModule(projectsView));
    }

    @Override
    public ManagersComponent getManagersModule() {
        if (managersComponent == null) {
            managersComponent = getApiComponent().plus();
        }

        return managersComponent;
    }
}
