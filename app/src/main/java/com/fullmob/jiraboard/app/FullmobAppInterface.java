package com.fullmob.jiraboard.app;

import android.content.Context;

import com.fullmob.jiraapi.HttpClientBuilder;
import com.fullmob.jiraboard.di.components.ApiComponent;
import com.fullmob.jiraboard.di.components.CaptureBoardComponent;
import com.fullmob.jiraboard.di.components.IssueTypesScreenComponent;
import com.fullmob.jiraboard.di.components.LoginScreenComponent;
import com.fullmob.jiraboard.di.components.MainComponent;
import com.fullmob.jiraboard.di.components.ManagersComponent;
import com.fullmob.jiraboard.di.components.ProjectsScreenComponent;
import com.fullmob.jiraboard.di.components.TicketsScreenComponent;
import com.fullmob.jiraboard.di.components.WorkflowDiscoveryComponent;
import com.fullmob.jiraboard.ui.home.captureboard.CaptureBoardView;
import com.fullmob.jiraboard.ui.home.tickets.TicketsScreenView;
import com.fullmob.jiraboard.ui.issuetypes.IssueTypesView;
import com.fullmob.jiraboard.ui.login.LoginView;
import com.fullmob.jiraboard.ui.projects.ProjectsView;

public interface FullmobAppInterface {
    Context getContext();
    MainComponent getMainComponent();
    void addDebugInterceptors(HttpClientBuilder builder);

    LoginScreenComponent createHomeScreenComponent();

    LoginScreenComponent createLoginScreenComponent(LoginView loginView);

    ApiComponent getApiComponent();

    ProjectsScreenComponent createProjectsScreenComponent(ProjectsView projectsView);

    ManagersComponent getManagersModule();

    WorkflowDiscoveryComponent createWorkflowDiscoveryComponent();

    IssueTypesScreenComponent createIssueTypesComponent(IssueTypesView view);

    CaptureBoardComponent createCaptureBoardFragmentComponent(CaptureBoardView view);

    TicketsScreenComponent createTicketsScreenComponent(TicketsScreenView view);
}
