package com.fullmob.jiraboard.services;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.fullmob.jiraboard.app.FullmobAppInterface;
import com.fullmob.jiraboard.managers.jobs.JobsHandler;

import javax.inject.Inject;

public class JobsRunnerService extends JobService {

    public static final String EXTRA_QUEUE_JOB_KEY = "queue_job_key";
    public static final String EXTRA_JOB_TYPE = "queue_job_type";

    @Inject JobsHandler jobsHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        injectDependencies();
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        String queueJobKey = job.getExtras() != null ? job.getExtras().getString(EXTRA_QUEUE_JOB_KEY) : null;
        String jobType = job.getExtras() != null ? job.getExtras().getString(EXTRA_JOB_TYPE) : null;
        if (queueJobKey != null && jobType != null) {
            jobsHandler.handleJob(jobType, queueJobKey);
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    private void injectDependencies() {
        getApp().createWorkflowDiscoveryComponent().inject(this);
    }

    public FullmobAppInterface getApp() {
        return (FullmobAppInterface) getApplication();
    }
}
