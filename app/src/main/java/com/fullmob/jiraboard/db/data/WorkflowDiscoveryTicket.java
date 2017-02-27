package com.fullmob.jiraboard.db.data;

import com.fullmob.jiraboard.data.Project;

import io.realm.RealmObject;

public class WorkflowDiscoveryTicket extends RealmObject {
    private JiraSubDomain subDomain;
    private Project project;
    private String key;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
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
}
