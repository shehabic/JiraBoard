package com.fullmob.jiraboard.db.data;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class WorkflowDiscoveryTicket extends RealmObject {

    @Ignore
    public static final String STATUS_PENDING = "pending";
    @Ignore
    public static final String STATUS_PROCESSING = "processing";
    @Ignore
    public static final String STATUS_PROCESSED = "processed";
    @Ignore
    public static final String STATUS_FAILED = "failed";

    @Required
    private String key;

    private String subDomain;

    private String project;

    private String typeId;

    private String workflowKey;

    private String title;

    private String statusId;

    @Required
    private String discoveryStatus;

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getDiscoveryStatus() {
        return discoveryStatus;
    }

    public void setDiscoveryStatus(String discoveryStatus) {
        this.discoveryStatus = discoveryStatus;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getWorkflowKey() {
        return workflowKey;
    }

    public void setWorkflowKey(String workflowKey) {
        this.workflowKey = workflowKey;
    }
}
