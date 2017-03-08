package com.fullmob.jiraboard.managers.workflow;

import com.fullmob.graphlib.discovery.WorkflowDiscovery;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIProject;

public class WorkflowDiscoveryManager {
    private final WorkflowDiscovery workflowDiscovery;
    private final DBManagerInterface dbManager;

    public WorkflowDiscoveryManager(WorkflowDiscovery workflowDiscovery, DBManagerInterface dbManager) {
        this.workflowDiscovery = workflowDiscovery;
        this.dbManager = dbManager;
    }

    public void scheduleDiscoveryTask(Issue issue, UIProject uiProject, UIIssueType issueType) {

    }
}
