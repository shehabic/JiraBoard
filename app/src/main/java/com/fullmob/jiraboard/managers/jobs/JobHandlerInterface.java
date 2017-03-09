package com.fullmob.jiraboard.managers.jobs;

public interface JobHandlerInterface {
    void handleJob(String queueJobPayload);
}
