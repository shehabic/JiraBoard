
package com.fullmob.jiraboard.db.data;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class JiraIssueType extends RealmObject {

    @Index
    @Required
    private String projectId;

    @Required
    private String id;

    private String self;

    @Required
    private String name;

    private Boolean subtask;

    @Index
    private String workflowKey;

    private String iconUrl;

    private String avatarId;

    private RealmList<JiraIssueStatus> statuses = new RealmList<>();

    public JiraIssueType() {
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSubtask() {
        return subtask;
    }

    public void setSubtask(Boolean subtask) {
        this.subtask = subtask;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public RealmList<JiraIssueStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(RealmList<JiraIssueStatus> statuses) {
        this.statuses = statuses;
    }

    public String getWorkflowKey() {
        return workflowKey;
    }

    public void setWorkflowKey(String workflowKey) {
        this.workflowKey = workflowKey;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }
}
