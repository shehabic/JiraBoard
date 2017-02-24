
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
    @SerializedName("timespent")
    @Expose
    private CustomField timespent;
    @SerializedName("project")
    @Expose
    private Project project;
    @SerializedName("fixVersions")
    @Expose
    private List<FixVersion> fixVersions = null;
    @SerializedName("customfield_11200")
    @Expose
    private String customfield11200;
    @SerializedName("customfield_11002")
    @Expose
    private CustomField customfield;
    @SerializedName("aggregatetimespent")
    @Expose
    private CustomField aggregatetimespent;
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
    @SerializedName("customfield_10020")
    @Expose
    private CustomField customfield10020;
    @SerializedName("customfield_10021")
    @Expose
    private String customfield10021;
    @SerializedName("priority")
    @Expose
    private Priority priority;
    @SerializedName("customfield_10100")
    @Expose
    private String customfield10100;
    @SerializedName("customfield_10024")
    @Expose
    private CustomField customfield10024;
    @SerializedName("customfield_10300")
    @Expose
    private CustomField customfield10300;
    @SerializedName("labels")
    @Expose
    private List<String> labels = null;
    @SerializedName("customfield_10301")
    @Expose
    private CustomField customfield10301;
    @SerializedName("customfield_10016")
    @Expose
    private CustomField customfield10016;
    @SerializedName("customfield_11502")
    @Expose
    private CustomField customfield11502;
    @SerializedName("customfield_10017")
    @Expose
    private CustomField customfield10017;
    @SerializedName("customfield_11501")
    @Expose
    private CustomField customfield11501;
    @SerializedName("customfield_10018")
    @Expose
    private CustomField customfield10018;
    @SerializedName("customfield_10019")
    @Expose
    private CustomField customfield10019;
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
    @SerializedName("customfield_10000")
    @Expose
    private String customfield10000;
    @SerializedName("aggregateprogress")
    @Expose
    private Aggregateprogress aggregateprogress;
    @SerializedName("environment")
    @Expose
    private CustomField environment;
    @SerializedName("duedate")
    @Expose
    private CustomField duedate;
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

    public Object getTimespent() {
        return timespent;
    }

    public void setTimespent(CustomField timespent) {
        this.timespent = timespent;
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

    public String getCustomfield11200() {
        return customfield11200;
    }

    public void setCustomfield11200(String customfield11200) {
        this.customfield11200 = customfield11200;
    }

    public CustomField getCustomfield() {
        return customfield;
    }

    public void setCustomfield(CustomField customfield) {
        this.customfield = customfield;
    }

    public Object getAggregatetimespent() {
        return aggregatetimespent;
    }

    public void setAggregatetimespent(CustomField aggregatetimespent) {
        this.aggregatetimespent = aggregatetimespent;
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

    public Object getCustomfield10020() {
        return customfield10020;
    }

    public void setCustomfield10020(CustomField customfield10020) {
        this.customfield10020 = customfield10020;
    }

    public String getCustomfield10021() {
        return customfield10021;
    }

    public void setCustomfield10021(String customfield10021) {
        this.customfield10021 = customfield10021;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getCustomfield10100() {
        return customfield10100;
    }

    public void setCustomfield10100(String customfield10100) {
        this.customfield10100 = customfield10100;
    }

    public Object getCustomfield10024() {
        return customfield10024;
    }

    public void setCustomfield10024(CustomField customfield10024) {
        this.customfield10024 = customfield10024;
    }

    public Object getCustomfield10300() {
        return customfield10300;
    }

    public void setCustomfield10300(CustomField customfield10300) {
        this.customfield10300 = customfield10300;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Object getCustomfield10301() {
        return customfield10301;
    }

    public void setCustomfield10301(CustomField customfield10301) {
        this.customfield10301 = customfield10301;
    }

    public Object getCustomfield10016() {
        return customfield10016;
    }

    public void setCustomfield10016(CustomField customfield10016) {
        this.customfield10016 = customfield10016;
    }

    public Object getCustomfield11502() {
        return customfield11502;
    }

    public void setCustomfield11502(CustomField customfield11502) {
        this.customfield11502 = customfield11502;
    }

    public Object getCustomfield10017() {
        return customfield10017;
    }

    public void setCustomfield10017(CustomField customfield10017) {
        this.customfield10017 = customfield10017;
    }

    public Object getCustomfield11501() {
        return customfield11501;
    }

    public void setCustomfield11501(CustomField customfield11501) {
        this.customfield11501 = customfield11501;
    }

    public Object getCustomfield10018() {
        return customfield10018;
    }

    public void setCustomfield10018(CustomField customfield10018) {
        this.customfield10018 = customfield10018;
    }

    public Object getCustomfield10019() {
        return customfield10019;
    }

    public void setCustomfield10019(CustomField customfield10019) {
        this.customfield10019 = customfield10019;
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

    public String getCustomfield10000() {
        return customfield10000;
    }

    public void setCustomfield10000(String customfield10000) {
        this.customfield10000 = customfield10000;
    }

    public Aggregateprogress getAggregateprogress() {
        return aggregateprogress;
    }

    public void setAggregateprogress(Aggregateprogress aggregateprogress) {
        this.aggregateprogress = aggregateprogress;
    }

    public Object getEnvironment() {
        return environment;
    }

    public void setEnvironment(CustomField environment) {
        this.environment = environment;
    }

    public Object getDuedate() {
        return duedate;
    }

    public void setDuedate(CustomField duedate) {
        this.duedate = duedate;
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
        dest.writeParcelable(this.timespent, flags);
        dest.writeParcelable(this.project, flags);
        dest.writeTypedList(this.fixVersions);
        dest.writeString(this.customfield11200);
        dest.writeParcelable(this.customfield, flags);
        dest.writeParcelable(this.aggregatetimespent, flags);
        dest.writeParcelable(this.resolution, flags);
        dest.writeString(this.resolutiondate);
        dest.writeValue(this.workratio);
        dest.writeString(this.lastViewed);
        dest.writeParcelable(this.watches, flags);
        dest.writeString(this.created);
        dest.writeParcelable(this.customfield10020, flags);
        dest.writeString(this.customfield10021);
        dest.writeParcelable(this.priority, flags);
        dest.writeString(this.customfield10100);
        dest.writeParcelable(this.customfield10024, flags);
        dest.writeParcelable(this.customfield10300, flags);
        dest.writeStringList(this.labels);
        dest.writeParcelable(this.customfield10301, flags);
        dest.writeParcelable(this.customfield10016, flags);
        dest.writeParcelable(this.customfield11502, flags);
        dest.writeParcelable(this.customfield10017, flags);
        dest.writeParcelable(this.customfield11501, flags);
        dest.writeParcelable(this.customfield10018, flags);
        dest.writeParcelable(this.customfield10019, flags);
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
        dest.writeString(this.customfield10000);
        dest.writeParcelable(this.aggregateprogress, flags);
        dest.writeParcelable(this.environment, flags);
        dest.writeParcelable(this.duedate, flags);
        dest.writeParcelable(this.progress, flags);
        dest.writeParcelable(this.issueCommentField, flags);
        dest.writeParcelable(this.worklog, flags);
    }

    protected IssueFields(Parcel in) {
        this.issuetype = in.readParcelable(Issuetype.class.getClassLoader());
        this.timespent = in.readParcelable(CustomField.class.getClassLoader());
        this.project = in.readParcelable(Project.class.getClassLoader());
        this.fixVersions = in.createTypedArrayList(FixVersion.CREATOR);
        this.customfield11200 = in.readString();
        this.customfield = in.readParcelable(CustomField.class.getClassLoader());
        this.aggregatetimespent = in.readParcelable(CustomField.class.getClassLoader());
        this.resolution = in.readParcelable(Resolution.class.getClassLoader());
        this.resolutiondate = in.readString();
        this.workratio = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastViewed = in.readString();
        this.watches = in.readParcelable(Watches.class.getClassLoader());
        this.created = in.readString();
        this.customfield10020 = in.readParcelable(CustomField.class.getClassLoader());
        this.customfield10021 = in.readString();
        this.priority = in.readParcelable(Priority.class.getClassLoader());
        this.customfield10100 = in.readString();
        this.customfield10024 = in.readParcelable(CustomField.class.getClassLoader());
        this.customfield10300 = in.readParcelable(CustomField.class.getClassLoader());
        this.labels = in.createStringArrayList();
        this.customfield10301 = in.readParcelable(CustomField.class.getClassLoader());
        this.customfield10016 = in.readParcelable(CustomField.class.getClassLoader());
        this.customfield11502 = in.readParcelable(CustomField.class.getClassLoader());
        this.customfield10017 = in.readParcelable(CustomField.class.getClassLoader());
        this.customfield11501 = in.readParcelable(CustomField.class.getClassLoader());
        this.customfield10018 = in.readParcelable(CustomField.class.getClassLoader());
        this.customfield10019 = in.readParcelable(CustomField.class.getClassLoader());
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
        this.customfield10000 = in.readString();
        this.aggregateprogress = in.readParcelable(Aggregateprogress.class.getClassLoader());
        this.environment = in.readParcelable(CustomField.class.getClassLoader());
        this.duedate = in.readParcelable(CustomField.class.getClassLoader());
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
