
package com.fullmob.jiraapi.models.issue;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraapi.models.Project;
import com.fullmob.jiraapi.models.Resolution;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class IssueFields implements Parcelable {

    @SerializedName("issuetype")
    @Expose
    private Issuetype issuetype;
    @SerializedName("project")
    @Expose
    private Project project;
    @SerializedName("fixVersions")
    @Expose
    private List<FixVersion> fixVersions = null;
    @SerializedName("resolution")
    @Expose
    private Resolution resolution;
    @SerializedName("resolutiondate")
    @Expose
    private String resolutiondate;
    @SerializedName("workratio")
    @Expose
    private Integer workratio;
    @SerializedName("lastViewed")
    @Expose
    private String lastViewed;
    @SerializedName("watches")
    @Expose
    private Watches watches;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("priority")
    @Expose
    private Priority priority;
    @SerializedName("labels")
    @Expose
    private List<String> labels = null;
    @SerializedName("timeestimate")
    @Expose
    private Integer timeestimate;
    @SerializedName("aggregatetimeoriginalestimate")
    @Expose
    private Integer aggregatetimeoriginalestimate;
    @SerializedName("versions")
    @Expose
    private List<Version> versions = null;
    @SerializedName("issuelinks")
    @Expose
    private List<Issuelink> issuelinks = null;
    @SerializedName("assignee")
    @Expose
    private Assignee assignee;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("components")
    @Expose
    private List<Component> components = null;
    @SerializedName("timeoriginalestimate")
    @Expose
    private Integer timeoriginalestimate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("timetracking")
    @Expose
    private Timetracking timetracking;
    @SerializedName("attachment")
    @Expose
    private List<Object> attachment = null;
    @SerializedName("aggregatetimeestimate")
    @Expose
    private Integer aggregatetimeestimate;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("creator")
    @Expose
    private com.fullmob.jiraapi.models.issue.Creator creator;
    @SerializedName("subtasks")
    @Expose
    private List<Object> subtasks = null;
    @SerializedName("reporter")
    @Expose
    private Reporter reporter;
    @SerializedName("aggregateprogress")
    @Expose
    private Aggregateprogress aggregateprogress;
    @SerializedName("progress")
    @Expose
    private Progress progress;
    @SerializedName("comment")
    @Expose
    private IssueCommentField issueCommentField;
    @SerializedName("worklog")
    @Expose
    private Worklog worklog;

    public Issuetype getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(Issuetype issuetype) {
        this.issuetype = issuetype;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<FixVersion> getFixVersions() {
        return fixVersions;
    }

    public void setFixVersions(List<FixVersion> fixVersions) {
        this.fixVersions = fixVersions;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public String getResolutiondate() {
        return resolutiondate;
    }

    public void setResolutiondate(String resolutiondate) {
        this.resolutiondate = resolutiondate;
    }

    public Integer getWorkratio() {
        return workratio;
    }

    public void setWorkratio(Integer workratio) {
        this.workratio = workratio;
    }

    public String getLastViewed() {
        return lastViewed;
    }

    public void setLastViewed(String lastViewed) {
        this.lastViewed = lastViewed;
    }

    public Watches getWatches() {
        return watches;
    }

    public void setWatches(Watches watches) {
        this.watches = watches;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }


    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Integer getTimeestimate() {
        return timeestimate;
    }

    public void setTimeestimate(Integer timeestimate) {
        this.timeestimate = timeestimate;
    }

    public Integer getAggregatetimeoriginalestimate() {
        return aggregatetimeoriginalestimate;
    }

    public void setAggregatetimeoriginalestimate(Integer aggregatetimeoriginalestimate) {
        this.aggregatetimeoriginalestimate = aggregatetimeoriginalestimate;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public List<Issuelink> getIssuelinks() {
        return issuelinks;
    }

    public void setIssuelinks(List<Issuelink> issuelinks) {
        this.issuelinks = issuelinks;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public Integer getTimeoriginalestimate() {
        return timeoriginalestimate;
    }

    public void setTimeoriginalestimate(Integer timeoriginalestimate) {
        this.timeoriginalestimate = timeoriginalestimate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Timetracking getTimetracking() {
        return timetracking;
    }

    public void setTimetracking(Timetracking timetracking) {
        this.timetracking = timetracking;
    }

    public List<Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<Object> attachment) {
        this.attachment = attachment;
    }

    public Integer getAggregatetimeestimate() {
        return aggregatetimeestimate;
    }

    public void setAggregatetimeestimate(Integer aggregatetimeestimate) {
        this.aggregatetimeestimate = aggregatetimeestimate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public com.fullmob.jiraapi.models.issue.Creator getCreator() {
        return creator;
    }

    public void setCreator(com.fullmob.jiraapi.models.issue.Creator creator) {
        this.creator = creator;
    }

    public List<Object> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Object> subtasks) {
        this.subtasks = subtasks;
    }

    public Reporter getReporter() {
        return reporter;
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    public Aggregateprogress getAggregateprogress() {
        return aggregateprogress;
    }

    public void setAggregateprogress(Aggregateprogress aggregateprogress) {
        this.aggregateprogress = aggregateprogress;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public IssueCommentField getIssueCommentField() {
        return issueCommentField;
    }

    public void setIssueCommentField(IssueCommentField issueCommentField) {
        this.issueCommentField = issueCommentField;
    }

    public Worklog getWorklog() {
        return worklog;
    }

    public void setWorklog(Worklog worklog) {
        this.worklog = worklog;
    }

    public IssueFields() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.issuetype, flags);
        dest.writeParcelable(this.project, flags);
        dest.writeTypedList(this.fixVersions);
        dest.writeParcelable(this.resolution, flags);
        dest.writeString(this.resolutiondate);
        dest.writeValue(this.workratio);
        dest.writeString(this.lastViewed);
        dest.writeParcelable(this.watches, flags);
        dest.writeString(this.created);
        dest.writeParcelable(this.priority, flags);
        dest.writeStringList(this.labels);
        dest.writeValue(this.timeestimate);
        dest.writeValue(this.aggregatetimeoriginalestimate);
        dest.writeTypedList(this.versions);
        dest.writeTypedList(this.issuelinks);
        dest.writeParcelable(this.assignee, flags);
        dest.writeString(this.updated);
        dest.writeParcelable(this.status, flags);
        dest.writeTypedList(this.components);
        dest.writeValue(this.timeoriginalestimate);
        dest.writeString(this.description);
        dest.writeParcelable(this.timetracking, flags);
        dest.writeList(this.attachment);
        dest.writeValue(this.aggregatetimeestimate);
        dest.writeString(this.summary);
        dest.writeParcelable(this.creator, flags);
        dest.writeList(this.subtasks);
        dest.writeParcelable(this.reporter, flags);
        dest.writeParcelable(this.aggregateprogress, flags);
        dest.writeParcelable(this.progress, flags);
        dest.writeParcelable(this.issueCommentField, flags);
        dest.writeParcelable(this.worklog, flags);
    }

    protected IssueFields(Parcel in) {
        this.issuetype = in.readParcelable(Issuetype.class.getClassLoader());
        this.project = in.readParcelable(Project.class.getClassLoader());
        this.fixVersions = in.createTypedArrayList(FixVersion.CREATOR);
        this.resolution = in.readParcelable(Resolution.class.getClassLoader());
        this.resolutiondate = in.readString();
        this.workratio = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastViewed = in.readString();
        this.watches = in.readParcelable(Watches.class.getClassLoader());
        this.created = in.readString();
        this.priority = in.readParcelable(Priority.class.getClassLoader());
        this.labels = in.createStringArrayList();
        this.timeestimate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.aggregatetimeoriginalestimate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.versions = in.createTypedArrayList(Version.CREATOR);
        this.issuelinks = in.createTypedArrayList(Issuelink.CREATOR);
        this.assignee = in.readParcelable(Assignee.class.getClassLoader());
        this.updated = in.readString();
        this.status = in.readParcelable(Status.class.getClassLoader());
        this.components = in.createTypedArrayList(Component.CREATOR);
        this.timeoriginalestimate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.description = in.readString();
        this.timetracking = in.readParcelable(Timetracking.class.getClassLoader());
        this.attachment = new ArrayList<Object>();
        in.readList(this.attachment, Object.class.getClassLoader());
        this.aggregatetimeestimate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.summary = in.readString();
        this.creator = in.readParcelable(com.fullmob.jiraapi.models.issue.Creator.class.getClassLoader());
        this.subtasks = new ArrayList<Object>();
        in.readList(this.subtasks, Object.class.getClassLoader());
        this.reporter = in.readParcelable(Reporter.class.getClassLoader());
        this.aggregateprogress = in.readParcelable(Aggregateprogress.class.getClassLoader());
        this.progress = in.readParcelable(Progress.class.getClassLoader());
        this.issueCommentField = in.readParcelable(IssueCommentField.class.getClassLoader());
        this.worklog = in.readParcelable(Worklog.class.getClassLoader());
    }

    public static final Creator<IssueFields> CREATOR = new Creator<IssueFields>() {
        @Override
        public IssueFields createFromParcel(Parcel source) {
            return new IssueFields(source);
        }

        @Override
        public IssueFields[] newArray(int size) {
            return new IssueFields[size];
        }
    };
}
