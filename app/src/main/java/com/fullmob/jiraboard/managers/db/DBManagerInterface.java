package com.fullmob.jiraboard.managers.db;

import com.fullmob.jiraboard.db.data.JiraProject;
import com.fullmob.jiraboard.db.data.WorkflowDiscoveryTicket;

public interface DBManagerInterface {
    void saveProject(JiraProject project);
    void saveDiscoveryJob(WorkflowDiscoveryTicket ticket);
}
