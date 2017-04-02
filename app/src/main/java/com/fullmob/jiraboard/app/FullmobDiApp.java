package com.fullmob.jiraboard.app;

import android.app.Application;
import android.content.Context;

import com.fullmob.jiraapi.HttpClientBuilder;
import com.fullmob.jiraboard.DebugUtils;
import com.fullmob.jiraboard.di.components.ApiComponent;
import com.fullmob.jiraboard.di.components.CaptureBoardComponent;
import com.fullmob.jiraboard.di.components.DaggerMainComponent;
import com.fullmob.jiraboard.di.components.HomeScreenComponent;
import com.fullmob.jiraboard.di.components.IssueScreenComponent;
import com.fullmob.jiraboard.di.components.IssueTypesScreenComponent;
import com.fullmob.jiraboard.di.components.LoginScreenComponent;
import com.fullmob.jiraboard.di.components.MainComponent;
import com.fullmob.jiraboard.di.components.ManagersComponent;
import com.fullmob.jiraboard.di.components.ProjectsScreenComponent;
import com.fullmob.jiraboard.di.components.StatusesScreenComponent;
import com.fullmob.jiraboard.di.components.TicketsScreenComponent;
import com.fullmob.jiraboard.di.components.TransitionsScreenComponent;
import com.fullmob.jiraboard.di.components.WorkflowDiscoveryComponent;
import com.fullmob.jiraboard.di.modules.CaptureBoardModule;
import com.fullmob.jiraboard.di.modules.HomeScreenModule;
import com.fullmob.jiraboard.di.modules.IssueScreenModule;
import com.fullmob.jiraboard.di.modules.IssueTypesModule;
import com.fullmob.jiraboard.di.modules.LoginScreenModule;
import com.fullmob.jiraboard.di.modules.MainModule;
import com.fullmob.jiraboard.di.modules.ProjectsScreenModule;
import com.fullmob.jiraboard.di.modules.StatusesScreenModule;
import com.fullmob.jiraboard.di.modules.TicketsScreenModule;
import com.fullmob.jiraboard.di.modules.TransitionScreenModule;
import com.fullmob.jiraboard.di.modules.WorkflowDiscoveryModule;
import com.fullmob.jiraboard.ui.home.HomeScreenView;
import com.fullmob.jiraboard.ui.home.captureboard.CaptureBoardView;
import com.fullmob.jiraboard.ui.home.statuses.StatusesView;
import com.fullmob.jiraboard.ui.home.tickets.TicketsScreenView;
import com.fullmob.jiraboard.ui.issue.IssueScreenView;
import com.fullmob.jiraboard.ui.issuetypes.IssueTypesView;
import com.fullmob.jiraboard.ui.login.LoginView;
import com.fullmob.jiraboard.ui.projects.ProjectsView;
import com.fullmob.jiraboard.ui.transitions.TransitionsScreenView;

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
    public ManagersComponent getManagersModule() {
        if (managersComponent == null) {
            managersComponent = getApiComponent().plus();
        }

        return managersComponent;
    }

    @Override
    public ProjectsScreenComponent createProjectsScreenComponent(ProjectsView projectsView) {
        return getManagersModule().plusProjects(new ProjectsScreenModule(projectsView));
    }

    @Override
    public WorkflowDiscoveryComponent createWorkflowDiscoveryComponent() {
        return getManagersModule().plusWorkflowDiscovery(new WorkflowDiscoveryModule());
    }

    @Override
    public IssueTypesScreenComponent createIssueTypesComponent(IssueTypesView view) {
        return getManagersModule().plusIssueTypesScreenComponent(new IssueTypesModule(view));
    }

    @Override
    public CaptureBoardComponent createCaptureBoardFragmentComponent(CaptureBoardView view) {
        return getManagersModule().plusCaptureBoardComponent(new CaptureBoardModule(view));
    }

    @Override
    public TicketsScreenComponent createTicketsScreenComponent(TicketsScreenView view) {
        return getManagersModule().plusTicketsScreenComponent(new TicketsScreenModule(view));
    }

    @Override
    public StatusesScreenComponent createStatusesScreenComponent(StatusesView view) {
        return getManagersModule().plusStatusesScreenComponent(new StatusesScreenModule(view));
    }

    @Override
    public IssueScreenComponent createIssueScreenComponent(IssueScreenView view) {
        return getManagersModule().plusIssueScreenComponent(new IssueScreenModule(view));
    }

    @Override
    public TransitionsScreenComponent createTransitionsScreenComponent(TransitionsScreenView view) {
        return getManagersModule().plusTransitionsScreenComponent(new TransitionScreenModule(view));
    }

    @Override
    public HomeScreenComponent getHomeScreenComponent(HomeScreenView view) {
        return getManagersModule().plusHomeScreenComponent(new HomeScreenModule(view));
    }
}
