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
    private String sourceStatusName;

    @Required
    private String linkId;

    @Required
    private String linkName;

    @Required
    private String targetStatusName;

    @Required
    private String targetStatusId;

    private String sourceStatusId;

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

    public String getSourceStatusName() {
        return sourceStatusName;
    }

    public void setSourceStatusName(String sourceStatus) {
        this.sourceStatusName = sourceStatus;
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

    public String getTargetStatusName() {
        return targetStatusName;
    }

    public void setTargetStatusName(String targetStatus) {
        this.targetStatusName = targetStatus;
    }

    public WorkflowDiscoveryQueueJob getWorkflowDiscoveryTicket() {
        return workflowDiscoveryTicket;
    }

    public void setWorkflowDiscoveryTicket(WorkflowDiscoveryQueueJob workflowDiscoveryTicket) {
        this.workflowDiscoveryTicket = workflowDiscoveryTicket;
    }

    public void setTargetStatusId(String targetStatusId) {
        this.targetStatusId = targetStatusId;
    }

    public String getTargetStatusId() {
        return targetStatusId;
    }

    public void setSourceStatusId(String sourceStatusId) {
        this.sourceStatusId = sourceStatusId;
    }

    public String getSourceStatusId() {
        return sourceStatusId;
    }
}
