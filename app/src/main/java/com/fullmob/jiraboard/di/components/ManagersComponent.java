package com.fullmob.jiraboard.di.components;


import com.fullmob.jiraboard.di.modules.CaptureBoardModule;
import com.fullmob.jiraboard.di.modules.IssueScreenModule;
import com.fullmob.jiraboard.di.modules.IssueTypesModule;
import com.fullmob.jiraboard.di.modules.LoginScreenModule;
import com.fullmob.jiraboard.di.modules.ManagersModule;
import com.fullmob.jiraboard.di.modules.ProjectsScreenModule;
import com.fullmob.jiraboard.di.modules.StatusesScreenModule;
import com.fullmob.jiraboard.di.modules.TicketsScreenModule;
import com.fullmob.jiraboard.di.modules.TransitionScreenModule;
import com.fullmob.jiraboard.di.modules.WorkflowDiscoveryModule;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.managers.user.UserManager;

import dagger.Subcomponent;

@Subcomponent(modules = {ManagersModule.class})
public interface ManagersComponent {
    UserManager getUserManager();
    ProjectsManager getProjManageR();
    LoginScreenComponent plus(LoginScreenModule loginScreenModule);
    ProjectsScreenComponent plusProjects(ProjectsScreenModule module);
    WorkflowDiscoveryComponent plusWorkflowDiscovery(WorkflowDiscoveryModule module);
    IssueTypesScreenComponent plusIssueTypesScreenComponent(IssueTypesModule module);
    CaptureBoardComponent plusCaptureBoardComponent(CaptureBoardModule module);
    TicketsScreenComponent plusTicketsScreenComponent(TicketsScreenModule module);
    StatusesScreenComponent plusStatusesScreenComponent(StatusesScreenModule module);
    IssueScreenComponent plusIssueScreenComponent(IssueScreenModule module);
    TransitionsScreenComponent plusTransitionsScreenComponent(TransitionScreenModule modules);
}
