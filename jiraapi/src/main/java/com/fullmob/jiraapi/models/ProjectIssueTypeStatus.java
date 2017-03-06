package com.fullmob.jiraapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraapi.models.issue.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProjectIssueTypeStatus implements Parcelable {
    @SerializedName("self")
    @Expose
    private String self;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("subtask")
    @Expose
    private boolean subtask;

    @SerializedName("statuses")
    @Expose
    private List<Status> statuses = new ArrayList<>();

    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;

    @SerializedName("avatarId")
    @Expose
    private String avatarId;

    // Ignore
    private String projectId;

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

    public boolean isSubtask() {
        return subtask;
    }

    public void setSubtask(boolean subtask) {
        this.subtask = subtask;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public ProjectIssueTypeStatus() {
    }

    public ProjectIssueTypeStatus clone() {
        ProjectIssueTypeStatus projectIssueTypeStatus = new ProjectIssueTypeStatus();
        projectIssueTypeStatus.setName(name);
        projectIssueTypeStatus.setProjectId(projectId);
        projectIssueTypeStatus.setId(id);
        projectIssueTypeStatus.setSelf(self);
        projectIssueTypeStatus.setSubtask(subtask);
        projectIssueTypeStatus.setIconUrl(iconUrl);
        projectIssueTypeStatus.setAvatarId(avatarId);
        for (Status status : statuses) {
            projectIssueTypeStatus.getStatuses().add(status.clone());
        }

        return projectIssueTypeStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.subtask ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.statuses);
        dest.writeString(this.iconUrl);
        dest.writeString(this.avatarId);
        dest.writeString(this.projectId);
    }

    protected ProjectIssueTypeStatus(Parcel in) {
        this.self = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.subtask = in.readByte() != 0;
        this.statuses = in.createTypedArrayList(Status.CREATOR);
        this.iconUrl = in.readString();
        this.avatarId = in.readString();
        this.projectId = in.readString();
    }

    public static final Creator<ProjectIssueTypeStatus> CREATOR = new Creator<ProjectIssueTypeStatus>() {
        @Override
        public ProjectIssueTypeStatus createFromParcel(Parcel source) {
            return new ProjectIssueTypeStatus(source);
        }

        @Override
        public ProjectIssueTypeStatus[] newArray(int size) {
            return new ProjectIssueTypeStatus[size];
        }
    };
}
