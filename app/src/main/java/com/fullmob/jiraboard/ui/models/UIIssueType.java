package com.fullmob.jiraboard.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UIIssueType implements Parcelable {

    private String projectId;

    private String id;

    private String self;

    private String name;

    private Boolean subtask;

    private String workflowKey;

    private String iconUrl;

    private String avatarId;

    private String status;

    private String discoveryStatus = "";

    public UIIssueType() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiscoveryStatus() {
        return discoveryStatus;
    }

    public void setDiscoveryStatus(String discoveryStatus) {
        this.discoveryStatus = discoveryStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.projectId);
        dest.writeString(this.id);
        dest.writeString(this.self);
        dest.writeString(this.name);
        dest.writeValue(this.subtask);
        dest.writeString(this.workflowKey);
        dest.writeString(this.iconUrl);
        dest.writeString(this.avatarId);
        dest.writeString(this.status);
        dest.writeString(this.discoveryStatus);
    }

    protected UIIssueType(Parcel in) {
        this.projectId = in.readString();
        this.id = in.readString();
        this.self = in.readString();
        this.name = in.readString();
        this.subtask = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.workflowKey = in.readString();
        this.iconUrl = in.readString();
        this.avatarId = in.readString();
        this.status = in.readString();
        this.discoveryStatus = in.readString();
    }

    public static final Creator<UIIssueType> CREATOR = new Creator<UIIssueType>() {
        @Override
        public UIIssueType createFromParcel(Parcel source) {
            return new UIIssueType(source);
        }

        @Override
        public UIIssueType[] newArray(int size) {
            return new UIIssueType[size];
        }
    };
}
