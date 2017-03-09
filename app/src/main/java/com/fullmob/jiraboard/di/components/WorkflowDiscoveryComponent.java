package com.fullmob.jiraboard.di.components;


import com.fullmob.jiraboard.di.modules.WorkflowDiscoveryModule;
import com.fullmob.jiraboard.services.JobsRunnerService;

import dagger.Subcomponent;

@Subcomponent(modules = {WorkflowDiscoveryModule.class})
public interface WorkflowDiscoveryComponent {
    void inject(JobsRunnerService jobsRunnerService);
}
