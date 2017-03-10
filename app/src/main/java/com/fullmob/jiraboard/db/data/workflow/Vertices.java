package com.fullmob.jiraboard.db.data.workflow;

import com.fullmob.jiraboard.db.data.WorkflowDiscoveryQueueJob;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class Vertices extends RealmObject {

    @Required
    private String issueType;

    @Required
    private String projectId;

    @Required
    private String subDomain;

    @Required
    private String sourceStatus;

    @Required
    private String linkId;

    @Required
    private String linkName;

    @Required
    private String targetStatus;

    private WorkflowDiscoveryQueueJob workflowDiscoveryTicket;

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
    }

    public WorkflowDiscoveryQueueJob getWorkflowDiscoveryTicket() {
        return workflowDiscoveryTicket;
    }

    public void setWorkflowDiscoveryTicket(WorkflowDiscoveryQueueJob workflowDiscoveryTicket) {
        this.workflowDiscoveryTicket = workflowDiscoveryTicket;
    }
}
