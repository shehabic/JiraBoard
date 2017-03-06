package com.fullmob.jiraboard.di.components;


import com.fullmob.jiraboard.di.modules.WorkflowDiscoveryModule;
import com.fullmob.jiraboard.services.WorkflowDiscoveryService;

import dagger.Subcomponent;

@Subcomponent(modules = {WorkflowDiscoveryModule.class})
public interface WorkflowDiscoveryComponent {
    void inject(WorkflowDiscoveryService workflowDiscoveryService);
}
