package com.fullmob.jiraboard.managers.queue;

import android.os.Bundle;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.fullmob.jiraboard.managers.jobs.job.WorkflowDiscoveryJob;
import com.fullmob.jiraboard.services.JobsRunnerService;
import com.fullmob.jiraboard.ui.models.UIWorkflowQueueJob;

public class QueueManager {

    private final FirebaseJobDispatcher dispatcher;

    public QueueManager(FirebaseJobDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void enqueueWorkflowDiscoveryJob(UIWorkflowQueueJob queueJob) {
        Bundle extras = new Bundle();

        extras.putString(JobsRunnerService.EXTRA_QUEUE_JOB_KEY, queueJob.getJobKey());
        extras.putString(JobsRunnerService.EXTRA_JOB_TYPE, WorkflowDiscoveryJob.JOB_TYPE);

        extras.setClassLoader(WorkflowDiscoveryJob.class.getClassLoader());
        Job myJob = dispatcher.newJobBuilder()
            .setService(JobsRunnerService.class)
            .setRecurring(false)
            .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
            .setTrigger(Trigger.executionWindow(0, 20))
            .setReplaceCurrent(true)
            .setTag(queueJob.getJobKey())
            .setConstraints(Constraint.ON_ANY_NETWORK)
            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
            .setExtras(extras)
            .build();
        dispatcher.mustSchedule(myJob);
    }
}
