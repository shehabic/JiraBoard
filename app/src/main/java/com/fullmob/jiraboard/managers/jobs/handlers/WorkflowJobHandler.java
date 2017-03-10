package com.fullmob.jiraboard.managers.jobs.handlers;

import com.fullmob.graphlib.discovery.DiscoveryStatus;
import com.fullmob.jiraboard.managers.jobs.JobHandlerInterface;
import com.fullmob.jiraboard.managers.notifications.NotificationsManager;
import com.fullmob.jiraboard.managers.workflow.WorkflowDiscoveryManager;
import com.fullmob.jiraboard.ui.models.UIWorkflowQueueJob;

import io.reactivex.functions.Consumer;

public class WorkflowJobHandler implements JobHandlerInterface {
    private final WorkflowDiscoveryManager discoveryManager;
    private final NotificationsManager notificationsManager;

    public WorkflowJobHandler(
        WorkflowDiscoveryManager discoveryManager,
        NotificationsManager notificationsManager
    ) {
        this.discoveryManager = discoveryManager;
        this.notificationsManager = notificationsManager;
    }

    @Override
    public void handleJob(String queueJobKey) {
        discoverWorkflow(queueJobKey);
    }

    private void discoverWorkflow(String queueJobKey) {
        final UIWorkflowQueueJob job = discoveryManager.getDiscoveryJob(queueJobKey);

        try {
            if (job.getDiscoveryStatus().equals(UIWorkflowQueueJob.STATUS_PENDING)) {
                discoveryManager.startWorkflowDiscovery(job).subscribe(new Consumer<DiscoveryStatus>() {
                    @Override
                    public void accept(DiscoveryStatus discoveryStatus) throws Exception {
                        updateJobStatusNotification(discoveryStatus, job);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateJobStatusNotification(DiscoveryStatus discoveryStatus, UIWorkflowQueueJob job) {
        notificationsManager.createDiscoveryNotificationItem(discoveryStatus);
    }
}
