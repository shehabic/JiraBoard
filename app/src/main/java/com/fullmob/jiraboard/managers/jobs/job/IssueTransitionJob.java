package com.fullmob.jiraboard.managers.jobs.job;

import android.os.Parcel;
import android.os.Parcelable;

import com.fullmob.jiraboard.managers.jobs.QueueJobInterface;

public class IssueTransitionJob implements QueueJobInterface, Parcelable {
    public static final String JOB_TYPE = "job_type_issue_transition";
    private final String jobKey;
    private final String issueKey;
    private final String transitionSteps;
    private final String projectId;

    public IssueTransitionJob(String jobKey, String issueKey, String transitionSteps, String projectId) {
        this.jobKey = jobKey;
        this.issueKey = issueKey;
        this.transitionSteps = transitionSteps;
        this.projectId = projectId;
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

    public String getTransitionSteps() {
        return transitionSteps;
    }

    public String getProjectId() {
        return projectId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jobKey);
        dest.writeString(this.issueKey);
        dest.writeString(this.transitionSteps);
        dest.writeString(this.projectId);
    }

    protected IssueTransitionJob(Parcel in) {
        this.jobKey = in.readString();
        this.issueKey = in.readString();
        this.transitionSteps = in.readString();
        this.projectId = in.readString();
    }

    public static final Creator<IssueTransitionJob> CREATOR = new Creator<IssueTransitionJob>() {
        @Override
        public IssueTransitionJob createFromParcel(Parcel source) {
            return new IssueTransitionJob(source);
        }

        @Override
        public IssueTransitionJob[] newArray(int size) {
            return new IssueTransitionJob[size];
        }
    };
}
