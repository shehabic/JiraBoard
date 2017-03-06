package com.fullmob.jiraboard.ui.models;

import java.util.HashSet;
import java.util.List;

public class IssueTypesAndWorkflows {
    private HashSet<String> workflows;
    private List<UIIssueType> issueTypes;

    public IssueTypesAndWorkflows(List<UIIssueType> uiIssueTypes, HashSet<String> uniqueWorkflows) {
        this.issueTypes = uiIssueTypes;
        this.workflows = uniqueWorkflows;
    }

    public HashSet<String> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(HashSet<String> workflows) {
        this.workflows = workflows;
    }

    public List<UIIssueType> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(List<UIIssueType> issueTypes) {
        this.issueTypes = issueTypes;
    }
}
