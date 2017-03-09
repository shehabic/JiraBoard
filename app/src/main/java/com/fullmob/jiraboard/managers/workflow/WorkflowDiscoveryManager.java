package com.fullmob.jiraboard.managers.workflow;

import com.fullmob.graphlib.discovery.DiscoveryStatus;
import com.fullmob.graphlib.discovery.WorkflowDiscovery;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.queue.QueueManager;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.io.IOException;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WorkflowDiscoveryManager {
    private final WorkflowDiscovery workflowDiscovery;
    private final DBManagerInterface dbManager;
    private final QueueManager queue;

    public WorkflowDiscoveryManager(WorkflowDiscovery workflowDiscovery, DBManagerInterface db, QueueManager queue) {
        this.workflowDiscovery = workflowDiscovery;
        this.dbManager = db;
        this.queue = queue;
    }

    public void scheduleDiscoveryTask(Issue issue, UIProject uiProject, UIIssueType issueType) {
        dbManager.queueWorkflowDiscoveryTicket(issue, uiProject, issueType);
        queue.enqueueWorkflowDiscoveryJob(issue, issueType);
    }

    public void startWorkflowDiscovery(String issueKey, String projectId, String workflowKey) throws IOException {
        workflowDiscovery.discoverWorkFlow(issueKey).subscribeOn(Schedulers.io())
            .subscribe(new Consumer<DiscoveryStatus>() {
                @Override
                public void accept(DiscoveryStatus discoveryStatus) throws Exception {
//                    updateDiscoveryStatus(issue, issueType, discoveryStatus);
                }
            });
    }
}
