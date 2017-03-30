package com.fullmob.jiraboard.managers.jobs.handlers;

import com.fullmob.jiraboard.managers.jobs.JobHandlerInterface;
import com.fullmob.jiraboard.managers.notifications.NotificationsManager;
import com.fullmob.jiraboard.transitions.TransitionJob;
import com.fullmob.jiraboard.transitions.TransitionManager;
import com.fullmob.jiraboard.transitions.TransitionStatus;

import io.reactivex.functions.Consumer;

public class IssueTransitionJobHandler implements JobHandlerInterface {
    private final TransitionManager transitionManager;
    private final NotificationsManager notificationsManager;

    public IssueTransitionJobHandler(
        TransitionManager transitionManager,
        NotificationsManager notificationsManager
    ) {
        this.transitionManager = transitionManager;
        this.notificationsManager = notificationsManager;
    }

    @Override
    public void handleJob(String queueJobKey) {
        discoverWorkflow(queueJobKey);
    }

    private void discoverWorkflow(String queueJobKey) {
        final TransitionJob job = transitionManager.getTransitionJob(queueJobKey);

        try {
            if (job.getStatus().equals(TransitionJob.STATUS_PENDING)) {
                transitionManager.startTransition(job).subscribe(new Consumer<TransitionStatus>() {
                    @Override
                    public void accept(TransitionStatus status) throws Exception {
                        updateJobStatusNotification(status);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        notificationsManager.cancelNotification(
                            NotificationsManager.TRANSITION_NOTIF_ID_PREFIX,
                            Integer.valueOf(job.getIssueKey())
                        );
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateJobStatusNotification(TransitionStatus status) {
        notificationsManager.createTransitionNotificationItem(status);
    }
}
