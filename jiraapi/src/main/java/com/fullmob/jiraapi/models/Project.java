
package com.fullmob.jiraapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraapi.models.issue.Issuetype;
import com.fullmob.jiraapi.models.issue.Version;
import com.fullmob.jiraapi.models.project.Lead;
import com.fullmob.jiraapi.models.project.Roles;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Project implements Parcelable {

    @SerializedName("expand")
    @Expose
    private String expand;
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("lead")
    @Expose
    private Lead lead;
    @SerializedName("components")
    @Expose
    private List<Object> components = null;
    @SerializedName("issueTypes")
    @Expose
    private List<Issuetype> issueTypes = null;
    @SerializedName("assigneeType")
    @Expose
    private String assigneeType;
    @SerializedName("versions")
    @Expose
    private List<Version> versions = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("roles")
    @Expose
    private Roles roles;
    @SerializedName("avatarUrls")
    @Expose
    private AvatarUrls avatarUrls;
    @SerializedName("projectTypeKey")
    @Expose
    private String projectTypeKey;
    @Expose
    private List<ProjectIssueTypeStatus> issueTypeStatuses;

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public List<Object> getComponents() {
        return components;
    }

    public void setComponents(List<Object> components) {
        this.components = components;
    }

    public List<Issuetype> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(List<Issuetype> issueTypes) {
        this.issueTypes = issueTypes;
    }

    public String getAssigneeType() {
        return assigneeType;
    }

    public void setAssigneeType(String assigneeType) {
        this.assigneeType = assigneeType;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    public void setAvatarUrls(AvatarUrls avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    public void setProjectTypeKey(String projectTypeKey) {
        this.projectTypeKey = projectTypeKey;
    }

    public List<ProjectIssueTypeStatus> getIssueTypeStatuses() {
        return issueTypeStatuses;
    }

    public void setIssueTypeStatuses(List<ProjectIssueTypeStatus> issueTypeStatuses) {
        this.issueTypeStatuses = issueTypeStatuses;
    }

    public Project() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.expand);
        dest.writeString(this.self);
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.description);
        dest.writeParcelable(this.lead, flags);
        dest.writeList(this.components);
        dest.writeTypedList(this.issueTypes);
        dest.writeString(this.assigneeType);
        dest.writeTypedList(this.versions);
        dest.writeString(this.name);
        dest.writeParcelable(this.roles, flags);
        dest.writeParcelable(this.avatarUrls, flags);
        dest.writeString(this.projectTypeKey);
        dest.writeTypedList(this.issueTypeStatuses);
    }

    protected Project(Parcel in) {
        this.expand = in.readString();
        this.self = in.readString();
        this.id = in.readString();
        this.key = in.readString();
        this.description = in.readString();
        this.lead = in.readParcelable(Lead.class.getClassLoader());
        this.components = new ArrayList<Object>();
        in.readList(this.components, Object.class.getClassLoader());
        this.issueTypes = in.createTypedArrayList(Issuetype.CREATOR);
        this.assigneeType = in.readString();
        this.versions = in.createTypedArrayList(Version.CREATOR);
        this.name = in.readString();
        this.roles = in.readParcelable(Roles.class.getClassLoader());
        this.avatarUrls = in.readParcelable(AvatarUrls.class.getClassLoader());
        this.projectTypeKey = in.readString();
        this.issueTypeStatuses = in.createTypedArrayList(ProjectIssueTypeStatus.CREATOR);
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel source) {
            return new Project(source);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
}
