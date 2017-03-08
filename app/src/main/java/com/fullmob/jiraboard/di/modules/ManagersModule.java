package com.fullmob.jiraboard.di.modules;


import com.fullmob.graphlib.discovery.WorkflowDiscovery;
import com.fullmob.jiraapi.managers.IssuesApiClient;
import com.fullmob.jiraapi.managers.JiraCloudUserManager;
import com.fullmob.jiraapi.managers.ProjectsApiClient;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.managers.storage.LocalStorageInterface;
import com.fullmob.jiraboard.managers.user.UserManager;
import com.fullmob.jiraboard.managers.workflow.WorkflowDiscoveryManager;

import dagger.Module;
import dagger.Provides;

@Module
public class ManagersModule {
    @Provides
    public UserManager providesUserManager(JiraCloudUserManager userManager, LocalStorageInterface localStorage) {
        return new UserManager(userManager, localStorage);
    }

    @Provides
    public IssuesManager providesIssuesManager(IssuesApiClient issuesApiClient) {
        return new IssuesManager(issuesApiClient);
    }

    @Provides
    public ProjectsManager providesProjManager(
        ProjectsApiClient manager,
        IssuesApiClient issuesApiClient,
        EncryptedStorage storage,
        DBManagerInterface db
    ) {
        return new ProjectsManager(manager, issuesApiClient, db, storage);
    }

    @Provides
    public WorkflowDiscovery providesWorkflowDiscovery(
        ProjectsApiClient manager,
        IssuesApiClient issuesApiClient
    ) {
        return new WorkflowDiscovery(issuesApiClient, manager);
    }

    @Provides
    public WorkflowDiscoveryManager providesWorkflowDiscoveryManager(
        DBManagerInterface db,
        WorkflowDiscovery workflowDiscovery
    ) {
        return new WorkflowDiscoveryManager(workflowDiscovery, db);
    }
}
