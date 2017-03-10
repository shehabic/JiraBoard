package com.fullmob.jiraboard.managers.db;

import com.fullmob.graphlib.TransitionLink;
import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.models.Project;
import com.fullmob.jiraboard.db.data.WorkflowDiscoveryQueueJob;
import com.fullmob.jiraboard.ui.models.SubDomain;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIProject;
import com.fullmob.jiraboard.ui.models.UIWorkflowQueueJob;

import java.util.HashSet;
import java.util.List;

import rx.Observable;

public interface DBManagerInterface {
    Observable<SubDomain> saveSubDomain(String subDomain);

    List<UIProject> findAllProjects(String domain);

    List<UIProject> addProjectsToSubDomain(String subDomain, List<Project> projects);

    void saveProject(UIProject project);
    void saveDiscoveryJob(WorkflowDiscoveryQueueJob ticket);
    List<UIProject> getProjects(String subDomain);
    List<UIProject> getProjectsAsyc(String subDomain);

    List<UIIssueType> findProjectIssueTypes(String projectId);

    HashSet<String> findProjectWorkflows(String projectId);

    UIProject getProject(String projectId);

    UIWorkflowQueueJob queueWorkflowDiscoveryTicket(Issue issue, UIProject uiProject, UIIssueType issueType);

    UIWorkflowQueueJob getUIWorkflowDiscoveryJob(String queueJobKey);

    WorkflowDiscoveryQueueJob findWorkflowQueueJob(String key);

    void updateWorkflowQueueJob(UIWorkflowQueueJob uiJob);

    void addVertex(TransitionLink link, String jobKey);
}
