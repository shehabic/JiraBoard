package com.fullmob.jiraboard.managers.workflow;

import com.fullmob.graphlib.TransitionLink;
import com.fullmob.graphlib.discovery.DiscoveryStatus;
import com.fullmob.graphlib.discovery.WorkflowDiscovery;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.db.data.WorkflowDiscoveryQueueJob;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.queue.QueueManager;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIProject;
import com.fullmob.jiraboard.ui.models.UIWorkflowQueueJob;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WorkflowDiscoveryManager {
    private static final int MAX_ATTEMPTS = 3;
    private final WorkflowDiscovery workflowDiscovery;
    private final DBManagerInterface dbManager;
    private final QueueManager queue;

    public WorkflowDiscoveryManager(WorkflowDiscovery workflowDiscovery, DBManagerInterface db, QueueManager queue) {
        this.workflowDiscovery = workflowDiscovery;
        this.dbManager = db;
        this.queue = queue;
    }

    public void scheduleDiscoveryTask(Issue issue, UIProject uiProject, UIIssueType issueType) {
        UIWorkflowQueueJob queueJob = dbManager.queueWorkflowDiscoveryTicket(issue, uiProject, issueType);
        queue.enqueueWorkflowDiscoveryJob(queueJob);
    }

    public Observable<DiscoveryStatus> startWorkflowDiscovery(final UIWorkflowQueueJob job) throws IOException {
        return workflowDiscovery.discoverWorkFlow(job.getKey()).subscribeOn(Schedulers.io())
            .doOnNext(new Consumer<DiscoveryStatus>() {
                @Override
                public void accept(DiscoveryStatus discoveryStatus) throws Exception {
                    updateDiscoveryStatus(job, discoveryStatus, null);
                }
            })
            .doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    updateDiscoveryStatus(job, null, throwable);
                }
            });
    }

    void updateDiscoveryStatus(UIWorkflowQueueJob uiJob, DiscoveryStatus status, Throwable error) {
        if (error != null) {
            markJobFailed(uiJob);
            reScheduleDiscoveryTask(uiJob);
        } else if (status != null && status.isCompleted()) {
            uiJob.setDiscoveryStatus(WorkflowDiscoveryQueueJob.STATUS_PROCESSED);
        } else {
            uiJob.setDiscoveryStatus(WorkflowDiscoveryQueueJob.STATUS_PROCESSING);
        }
        dbManager.updateWorkflowQueueJob(uiJob);
        if (status != null && status.isCompleted()) {
            createWorkflowVertices(status, uiJob);
        }
    }

    void createWorkflowVertices(DiscoveryStatus status, UIWorkflowQueueJob uiJob) {
        for (TransitionLink link : status.getVertices()) {
            dbManager.addVertex(link, uiJob.getJobKey());
        }
    }

    public UIWorkflowQueueJob getDiscoveryJob(String queueJobKey) {
        return dbManager.getUIWorkflowDiscoveryJob(queueJobKey);
    }

    void markJobFailed(UIWorkflowQueueJob job) {
        job.setDiscoveryStatus(UIWorkflowQueueJob.STATUS_FAILED);
        job.setAttempts(job.getAttempts() + 1);
        dbManager.updateWorkflowQueueJob(job);
    }

    void reScheduleDiscoveryTask(UIWorkflowQueueJob job) {
        if (job.getAttempts() < MAX_ATTEMPTS) {
            queue.enqueueWorkflowDiscoveryJob(job);
        }
    }
}
