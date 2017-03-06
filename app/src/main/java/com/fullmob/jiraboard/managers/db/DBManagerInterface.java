package com.fullmob.jiraboard.managers.db;

import com.fullmob.jiraapi.models.Project;
import com.fullmob.jiraboard.db.data.WorkflowDiscoveryTicket;
import com.fullmob.jiraboard.ui.models.SubDomain;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.HashSet;
import java.util.List;

import rx.Observable;

public interface DBManagerInterface {
    Observable<SubDomain> saveSubDomain(String subDomain);

    List<UIProject> findAllProjects(String domain);

    List<UIProject> addProjectsToSubDomain(String subDomain, List<Project> projects);

    void saveProject(UIProject project);
    void saveDiscoveryJob(WorkflowDiscoveryTicket ticket);
    List<UIProject> getProjects(String subDomain);
    List<UIProject> getProjectsAsyc(String subDomain);

    List<UIIssueType> findProjectIssueTypes(String projectId);

    HashSet<String> findProjectWorkflows(String projectId);
}
