package com.fullmob.jiraboard.di.modules;


import com.fullmob.graphlib.discovery.WorkflowDiscovery;
import com.fullmob.jiraapi.managers.IssuesApiClient;
import com.fullmob.jiraapi.managers.JiraCloudUserManager;
import com.fullmob.jiraapi.managers.ProjectsApiClient;
import com.fullmob.jiraboard.di.scopes.UserScope;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.jobs.JobsHandler;
import com.fullmob.jiraboard.managers.jobs.handlers.IssueTransitionJobHandler;
import com.fullmob.jiraboard.managers.jobs.handlers.WorkflowJobHandler;
import com.fullmob.jiraboard.managers.jobs.job.IssueTransitionJob;
import com.fullmob.jiraboard.managers.jobs.job.WorkflowDiscoveryJob;
import com.fullmob.jiraboard.managers.notifications.NotificationsManager;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.managers.queue.QueueManager;
import com.fullmob.jiraboard.managers.serializers.SerializerInterface;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.managers.storage.LocalStorageInterface;
import com.fullmob.jiraboard.managers.user.UserManager;
import com.fullmob.jiraboard.managers.workflow.WorkflowDiscoveryManager;
import com.fullmob.jiraboard.transitions.TransitionManager;

import dagger.Module;
import dagger.Provides;

@Module
@UserScope
public class ManagersModule {

    @UserScope
    @Provides
    public UserManager providesUserManager(
        JiraCloudUserManager userManager,
        LocalStorageInterface localStorage,
        SerializerInterface serializer
    ) {
        return new UserManager(userManager, localStorage, serializer);
    }

    @UserScope
    @Provides
    public IssuesManager providesIssuesManager(IssuesApiClient issuesApiClient, DBManagerInterface dbManager) {
        return new IssuesManager(issuesApiClient, dbManager);
    }

    @UserScope
    @Provides
    public ProjectsManager providesProjManager(
        ProjectsApiClient manager,
        IssuesApiClient issuesApiClient,
        EncryptedStorage storage,
        DBManagerInterface db
    ) {
        return new ProjectsManager(manager, issuesApiClient, db, storage);
    }

    @UserScope
    @Provides
    public WorkflowDiscovery providesWorkflowDiscovery(
        ProjectsApiClient manager,
        IssuesApiClient issuesApiClient
    ) {
        return new WorkflowDiscovery(issuesApiClient, manager);
    }

    @UserScope
    @Provides
    public WorkflowDiscoveryManager providesWorkflowDiscoveryManager(
        DBManagerInterface db,
        WorkflowDiscovery workflowDiscovery,
        QueueManager queue
    ) {
        return new WorkflowDiscoveryManager(workflowDiscovery, db, queue);
    }

    @UserScope
    @Provides
    public JobsHandler jobsHandler(
        WorkflowDiscoveryManager discoveryManager,
        NotificationsManager notifManager,
        TransitionManager transitionManager
    ) {
        JobsHandler handler = new JobsHandler();
        handler.addJobHandler(WorkflowDiscoveryJob.JOB_TYPE, new WorkflowJobHandler(discoveryManager, notifManager));
        handler.addJobHandler(
            IssueTransitionJob.JOB_TYPE,
            new IssueTransitionJobHandler(transitionManager, notifManager)
        );

        return handler;
    }

    @UserScope
    @Provides
    public TransitionManager providesTransitionManager(
        DBManagerInterface dbManager,
        IssuesManager issuesManager,
        QueueManager queueManager
    ) {
        return new TransitionManager(dbManager, issuesManager, queueManager);
    }
}
