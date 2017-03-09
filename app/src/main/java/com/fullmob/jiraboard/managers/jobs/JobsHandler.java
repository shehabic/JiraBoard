package com.fullmob.jiraboard.managers.jobs;

import java.util.HashMap;
import java.util.Map;

public class JobsHandler {
    private Map<String, JobHandlerInterface> jobHandlers;

    public JobsHandler() {
        jobHandlers = new HashMap<>();
    }

    public void addJobHandler(String jobType, JobHandlerInterface jobHandler) {
        jobHandlers.put(jobType, jobHandler);
    }

    public void handleJob(String jobType, String queueJob) {
        if (jobHandlers.containsKey(jobType)) {
            jobHandlers.get(jobType).handleJob(queueJob);
        } else {
            // @TODO: log error
        }
    }
}
