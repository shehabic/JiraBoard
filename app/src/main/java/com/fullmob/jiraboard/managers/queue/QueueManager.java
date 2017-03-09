package com.fullmob.jiraboard.managers.queue;

import android.os.Bundle;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.managers.jobs.job.WorkflowDiscoveryJob;
import com.fullmob.jiraboard.services.JobsRunnerService;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.google.gson.Gson;

public class QueueManager {

    private final FirebaseJobDispatcher dispatcher;

    public QueueManager(FirebaseJobDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void enqueueWorkflowDiscoveryJob(Issue issue, UIIssueType issueType) {
        String key = createWorkflowJobKey(issue, issueType);

        Bundle extras = new Bundle();
        WorkflowDiscoveryJob job = new WorkflowDiscoveryJob(key, issue.getKey(), issueType.getWorkflowKey(), issueType.getProjectId(), issueType.getId());

        extras.putString(JobsRunnerService.EXTRA_JOB_PAYLOAD, new Gson().toJson(job));
        extras.putString(JobsRunnerService.EXTRA_JOB_TYPE, job.getType());

        extras.setClassLoader(WorkflowDiscoveryJob.class.getClassLoader());
        Job myJob = dispatcher.newJobBuilder()
            .setService(JobsRunnerService.class)
            .setRecurring(false)
            .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
            .setTrigger(Trigger.executionWindow(0, 20))
            .setReplaceCurrent(true)
            .setTag(key)
            .setConstraints(Constraint.ON_ANY_NETWORK)
            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
            .setExtras(extras)
            .build();
        dispatcher.mustSchedule(myJob);
    }

    private String createWorkflowJobKey(Issue issue, UIIssueType type) {
        return String.format("%s_%s_%s", type.getWorkflowKey(), issue.getKey(), type.getProjectId());
    }
}
