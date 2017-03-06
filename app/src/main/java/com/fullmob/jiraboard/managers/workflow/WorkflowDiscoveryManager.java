package com.fullmob.jiraboard.managers.workflow;

import com.fullmob.graphlib.discovery.WorkflowDiscovery;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;

public class WorkflowDiscoveryManager {
    private final WorkflowDiscovery workflowDiscovery;
    private final DBManagerInterface dbManager;

    public WorkflowDiscoveryManager(WorkflowDiscovery workflowDiscovery, DBManagerInterface dbManager) {
        this.workflowDiscovery = workflowDiscovery;
        this.dbManager = dbManager;
    }
}
