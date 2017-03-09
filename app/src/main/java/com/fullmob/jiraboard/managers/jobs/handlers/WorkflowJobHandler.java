package com.fullmob.jiraboard.managers.jobs.handlers;

import android.util.Log;

import com.fullmob.jiraboard.managers.jobs.JobHandlerInterface;
import com.fullmob.jiraboard.managers.jobs.job.WorkflowDiscoveryJob;
import com.fullmob.jiraboard.managers.workflow.WorkflowDiscoveryManager;
import com.google.gson.Gson;

public class WorkflowJobHandler implements JobHandlerInterface {
    private final WorkflowDiscoveryManager discoveryManager;

    public WorkflowJobHandler(WorkflowDiscoveryManager discoveryManager) {
        this.discoveryManager = discoveryManager;
    }

    @Override
    public void handleJob(String queueJob) {
        discoverWorkflow(new Gson().fromJson(queueJob, WorkflowDiscoveryJob.class));
    }

    private void discoverWorkflow(WorkflowDiscoveryJob queueJob) {
        Log.d("WORKFLOW", "JOB Key" + queueJob.getJobUniqueKey());
        Log.d("WORKFLOW", "JOB Type" + queueJob.getType());
        Log.d("WORKFLOW", "Issue Key" + queueJob.getIssueKey());
        Log.d("WORKFLOW", "Issue Type" + queueJob.getIssueTypeId());
        Log.d("WORKFLOW", "Workflow Key" + queueJob.getWorkflowKey());
        try {
            discoveryManager.startWorkflowDiscovery(queueJob.getIssueKey(), queueJob.getProjectId(), queueJob.getWorkflowKey());
        } catch (Exception e) {
            //@TODO: propagate exception upwards
        }
    }
}
