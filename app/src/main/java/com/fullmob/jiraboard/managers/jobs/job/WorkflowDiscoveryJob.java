package com.fullmob.jiraboard.managers.jobs.job;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraboard.managers.jobs.QueueJobInterface;

public class WorkflowDiscoveryJob implements QueueJobInterface, Parcelable {
    public static final String JOB_TYPE = "job_type_workflow";
    private final String jobKey;
    private final String issueKey;
    private final String workflowKey;
    private final String projectId;
    private final String issueTypeId;

    public WorkflowDiscoveryJob(String jobKey, String issueKey, String workflowKey, String projectId, String issueTypeId) {
        this.jobKey = jobKey;
        this.issueKey = issueKey;
        this.workflowKey = workflowKey;
        this.projectId = projectId;
        this.issueTypeId = issueTypeId;
    }

    @Override
    public String getType() {
        return JOB_TYPE;
    }

    @Override
    public String getJobUniqueKey() {
        return jobKey;
    }

    public static String getJobType() {
        return JOB_TYPE;
    }

    public String getJobKey() {
        return jobKey;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public String getWorkflowKey() {
        return workflowKey;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getIssueTypeId() {
        return issueTypeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jobKey);
        dest.writeString(this.issueKey);
        dest.writeString(this.workflowKey);
        dest.writeString(this.projectId);
        dest.writeString(this.issueTypeId);
    }

    protected WorkflowDiscoveryJob(Parcel in) {
        this.jobKey = in.readString();
        this.issueKey = in.readString();
        this.workflowKey = in.readString();
        this.projectId = in.readString();
        this.issueTypeId = in.readString();
    }

    public static final Creator<WorkflowDiscoveryJob> CREATOR = new Creator<WorkflowDiscoveryJob>() {
        @Override
        public WorkflowDiscoveryJob createFromParcel(Parcel source) {
            return new WorkflowDiscoveryJob(source);
        }

        @Override
        public WorkflowDiscoveryJob[] newArray(int size) {
            return new WorkflowDiscoveryJob[size];
        }
    };
}
