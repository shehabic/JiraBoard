package com.fullmob.jiraboard.transitions;

/**
 * Created by shehabic on 30/03/2017.
 */

public class TransitionStatus {
    private int totalSteps = 0;
    private int completedSteps = 0;
    private TransitionJob job;
    private String currentStatus;
    private String color;
    private boolean issueFailed = false;

    public TransitionStatus() {
    }

    public TransitionStatus(TransitionJob job) {
        totalSteps = job.getTransitionSteps().getSteps().size();
        this.job = job;
    }

    public void incrementCompletedSteps() {
        completedSteps++;
    }

    public boolean isCompleted() {
        return totalSteps <= completedSteps;
    }

    public void updateState(String status, String color) {
        this.currentStatus = status;
        this.color = color;
    }

    public TransitionJob getJob() {
        return job;
    }

    public void setJob(TransitionJob job) {
        this.job = job;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean issueFailed() {
        return issueFailed;
    }

    public void setIssueFailed(boolean issueFailed) {
        this.issueFailed = issueFailed;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public int getCompletedSteps() {
        return completedSteps;
    }
}
