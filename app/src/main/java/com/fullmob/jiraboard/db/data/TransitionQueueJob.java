package com.fullmob.jiraboard.db.data;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Required;

/**
 * Created by shehabic on 30/03/2017.
 */
public class TransitionQueueJob extends RealmObject {
    @Ignore
    public static final String STATUS_PENDING = "pending";
    @Ignore
    public static final String STATUS_PROCESSING = "processing";
    @Ignore
    public static final String STATUS_PROCESSED = "processed";
    @Ignore
    public static final String STATUS_FAILED = "failed";

    @Required
    private String jobKey;

    @Required
    private String issueKey;

    private String subDomain;

    @Required
    private String projectId;

    @Required
    private String transitionSteps;

    @Required
    private String currentState;

    @Required
    private String status;

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    private int attempts;

    public TransitionQueueJob() {
        this.attempts = 0;
    }

    public String getJobKey() {
        return jobKey;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTransitionSteps() {
        return transitionSteps;
    }

    public void setTransitionSteps(String transitionSteps) {
        this.transitionSteps = transitionSteps;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
