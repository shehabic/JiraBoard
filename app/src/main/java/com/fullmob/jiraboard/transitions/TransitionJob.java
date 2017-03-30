package com.fullmob.jiraboard.transitions;

/**
 * Created by shehabic on 30/03/2017.
 */
public class TransitionJob {

    public static final String STATUS_PENDING = "pending";

    public static final String STATUS_PROCESSING = "processing";

    public static final String STATUS_PROCESSED = "processed";

    public static final String STATUS_FAILED = "failed";

    private String jobKey;

    private String issueKey;

    private String subDomain;

    private String projectId;

    private String transitionStepsSerialized;

    private TransitionSteps transitionSteps;

    private String currentState;

    private String status;

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    private int attempts;

    public TransitionJob() {
        attempts = 0;
        status = STATUS_PENDING;
        transitionSteps = new TransitionSteps();
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

    public String getTransitionStepsSerialized() {
        return transitionStepsSerialized;
    }

    public void setTransitionStepsSerialized(String transitionStepsSerialized) {
        this.transitionStepsSerialized = transitionStepsSerialized;
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

    public TransitionSteps getTransitionSteps() {
        return transitionSteps;
    }

    public void setTransitionSteps(TransitionSteps transitionSteps) {
        this.transitionSteps = transitionSteps;
    }

    public TransitionJob clone() {
        TransitionJob job = new TransitionJob();
        job.setCurrentState(currentState);
        job.setAttempts(attempts);
        job.setProjectId(projectId);
        job.setJobKey(getJobKey());
        job.setIssueKey(getIssueKey());
        job.setSubDomain(getSubDomain());
        job.setTransitionStepsSerialized(getTransitionStepsSerialized());
        job.getTransitionSteps().getSteps().addAll(getTransitionSteps().getSteps());

        return job;
    }
}
