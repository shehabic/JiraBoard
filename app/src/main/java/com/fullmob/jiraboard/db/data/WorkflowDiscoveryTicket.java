package com.fullmob.jiraboard.db.data;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class WorkflowDiscoveryTicket extends RealmObject {
    @Required
    private String key;

    private JiraSubDomain subDomain;

    private JiraProject project;

    @Required
    private String typeId;

    @Required
    private String workflowKey;

    private String title;
    private String status;
    private String statusId;
    private String discoveryStatus;

    public JiraSubDomain getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(JiraSubDomain subDomain) {
        this.subDomain = subDomain;
    }

    public JiraProject getProject() {
        return project;
    }

    public void setProject(JiraProject project) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
