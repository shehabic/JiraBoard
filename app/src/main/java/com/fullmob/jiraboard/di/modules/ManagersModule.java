package com.fullmob.jiraboard.di.modules;


import com.fullmob.graphlib.discovery.WorkflowDiscovery;
import com.fullmob.jiraapi.managers.IssuesManager;
import com.fullmob.jiraapi.managers.JiraCloudUserManager;
import com.fullmob.jiraapi.managers.ProjectsManager;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.projects.ProjManager;
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
    public ProjManager providesProjManager(
        ProjectsManager manager,
        IssuesManager issuesManager,
        EncryptedStorage storage,
        DBManagerInterface db
    ) {
        return new ProjManager(manager, issuesManager, db, storage);
    }

    @Provides
    public WorkflowDiscovery providesWorkflowDiscovery(
        ProjectsManager manager,
        IssuesManager issuesManager
    ) {
        return new WorkflowDiscovery(issuesManager, manager);
    }

    @Provides
    public WorkflowDiscoveryManager providesWorkflowDiscoveryManager(
        DBManagerInterface db,
        WorkflowDiscovery workflowDiscovery
    ) {
        return new WorkflowDiscoveryManager(workflowDiscovery, db);
    }
}
