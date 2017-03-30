package com.fullmob.jiraboard.managers.db;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fullmob.jiraapi.models.AvatarUrls;
import com.fullmob.jiraapi.models.Project;
import com.fullmob.jiraapi.models.ProjectIssueTypeStatus;
import com.fullmob.jiraapi.models.issue.Status;
import com.fullmob.jiraapi.models.issue.StatusCategory;
import com.fullmob.jiraapi.requests.issue.Transition;
import com.fullmob.jiraboard.db.data.JiraAvatarUrls;
import com.fullmob.jiraboard.db.data.JiraIssueStatus;
import com.fullmob.jiraboard.db.data.JiraIssueType;
import com.fullmob.jiraboard.db.data.JiraProject;
import com.fullmob.jiraboard.db.data.JiraStatusCategory;
import com.fullmob.jiraboard.db.data.TransitionQueueJob;
import com.fullmob.jiraboard.db.data.WorkflowDiscoveryQueueJob;
import com.fullmob.jiraboard.db.data.workflow.Vertices;
import com.fullmob.jiraboard.transitions.TransitionJob;
import com.fullmob.jiraboard.ui.models.UIAvatarUrls;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;
import com.fullmob.jiraboard.ui.models.UIIssueTransition;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIProject;
import com.fullmob.jiraboard.ui.models.UIStatusCategory;
import com.fullmob.jiraboard.ui.models.UIWorkflowQueueJob;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

public class Mapper {

    public JiraProject createJiraProjectFromProject(Project proj, String subDomain) {
        return createJiraProjectFromProject(new JiraProject(), proj, subDomain);
    }

    public JiraProject createJiraProjectFromProject(JiraProject jiraProject, Project proj, String subDomain) {
        jiraProject.setSubDomain(subDomain);
        jiraProject.setName(proj.getName());
        jiraProject.setJiraId(proj.getId());
        jiraProject.setKey(proj.getKey());
        jiraProject.setDescription(proj.getDescription());
        jiraProject.setAssigneeType(proj.getAssigneeType());
        jiraProject.setSelf(proj.getSelf());

        return jiraProject;
    }

    public JiraAvatarUrls createJiraAvatarUrlsFromAvatarUrls(AvatarUrls avatarUrls) {
        JiraAvatarUrls jiraAvatarUrls = new JiraAvatarUrls();
        return fillJiraAvatarUrls(avatarUrls, jiraAvatarUrls);
    }

    @NonNull
    public JiraAvatarUrls fillJiraAvatarUrls(AvatarUrls avatarUrls, JiraAvatarUrls jiraAvatarUrls) {
        jiraAvatarUrls.set16x16(avatarUrls.get16x16());
        jiraAvatarUrls.set24x24(avatarUrls.get24x24());
        jiraAvatarUrls.set32x32(avatarUrls.get32x32());
        jiraAvatarUrls.set48x48(avatarUrls.get48x48());

        return jiraAvatarUrls;
    }

    public UIProject createUIProjectFromDB(JiraProject project) {
        UIProject uiProject = new UIProject();
        uiProject.setSubDomain(project.getSubDomain());
        uiProject.setName(project.getName());
        uiProject.setId(project.getJiraId());
        uiProject.setAssigneeType(project.getAssigneeType());
        uiProject.setDescription(project.getDescription());
        uiProject.setAvatarUrls(createUIAvatarUrlsFromDB(project.getAvatarUrls()));
        uiProject.setProjectTypeKey(project.getProjectTypeKey());
        uiProject.setKey(project.getKey());

        return uiProject;
    }

    public UIAvatarUrls createUIAvatarUrlsFromDB(JiraAvatarUrls avatarUrls) {
        UIAvatarUrls uiAvatarUrls = new UIAvatarUrls();
        uiAvatarUrls.set16x16(avatarUrls.get16x16());
        uiAvatarUrls.set24x24(avatarUrls.get24x24());
        uiAvatarUrls.set32x32(avatarUrls.get32x32());
        uiAvatarUrls.set48x48(avatarUrls.get48x48());

        return uiAvatarUrls;
    }

    public UIAvatarUrls createUIAvatarUrlsFromDB(AvatarUrls avatarUrls) {
        UIAvatarUrls uiAvatarUrls = new UIAvatarUrls();
        uiAvatarUrls.set16x16(avatarUrls.get16x16());
        uiAvatarUrls.set24x24(avatarUrls.get24x24());
        uiAvatarUrls.set32x32(avatarUrls.get32x32());
        uiAvatarUrls.set48x48(avatarUrls.get48x48());

        return uiAvatarUrls;
    }

    public List<UIProject> createUIProjectListFromDbList(List<JiraProject> input) {
        List<UIProject> projects = new ArrayList<>();
        for (JiraProject inputProj : input) {
            projects.add(createUIProjectFromDB(inputProj));
        }

        return projects;
    }

    public UIProject createUIProjectFromApiProject(Project project, String subDomain) {
        UIProject uiProject = new UIProject();
        uiProject.setSubDomain(subDomain);
        uiProject.setName(project.getName());
        uiProject.setId(project.getId());
        uiProject.setAssigneeType(project.getAssigneeType());
        uiProject.setDescription(project.getDescription());
        uiProject.setAvatarUrls(createUIAvatarUrlsFromDB(project.getAvatarUrls()));
        uiProject.setProjectTypeKey(project.getProjectTypeKey());
        uiProject.setKey(project.getKey());

        return uiProject;
    }

    public void fillJiraStatusCategory(JiraStatusCategory jiraStatusCategory, StatusCategory statusCategory) {
        jiraStatusCategory.setSelf(statusCategory.getSelf());
        jiraStatusCategory.setColorName(statusCategory.getColorName());
        jiraStatusCategory.setName(statusCategory.getName());
        jiraStatusCategory.setKey(statusCategory.getKey());
    }

    public void fillJiraIssueStatus(JiraIssueStatus jiraIssueStatus, Status status, Map<Integer, JiraStatusCategory> jiraStatusCategories) {
        jiraIssueStatus.setName(status.getName());
        jiraIssueStatus.setSelf(status.getSelf());
        jiraIssueStatus.setDescription(status.getDescription());
        jiraIssueStatus.setIconUrl(status.getIconUrl());
        if (status.getStatusCategory() != null) {
            jiraIssueStatus.setStatusCategory(jiraStatusCategories.get(status.getStatusCategory().getId()));
        }
    }

    public void fillJiraIssueType(JiraIssueType jiraIssueType, ProjectIssueTypeStatus typeStatus, Map<String, JiraIssueStatus> jiraIssueStatuses) {
        jiraIssueType.setName(typeStatus.getName());
        jiraIssueType.setId(typeStatus.getId());
        jiraIssueType.setSelf(typeStatus.getSelf());
        jiraIssueType.setSubtask(typeStatus.isSubtask());
        jiraIssueType.setProjectId(typeStatus.getProjectId());
        jiraIssueType.setIconUrl(typeStatus.getIconUrl());
        jiraIssueType.setAvatarId(typeStatus.getAvatarId());
        String workflowKey = "";
        for (Status status : typeStatus.getStatuses()) {
            workflowKey += status.getId() + " ";
            if (jiraIssueStatuses.containsKey(status.getId())) {
                jiraIssueType.getStatuses().add(jiraIssueStatuses.get(status.getId()));
            }
        }
        jiraIssueType.setWorkflowKey(workflowKey.trim().replace(" ", "-"));
    }

    public List<UIIssueType> convertJiraIssueTypesToUIIssueTypes(RealmResults<JiraIssueType> jiraIssueTypes) {
        List<UIIssueType> issueTypes = new ArrayList<>();
        if (jiraIssueTypes != null) {
            for (JiraIssueType jiraIssueType : jiraIssueTypes) {
                issueTypes.add(convertJiraIssueTypeToUIIssueType(jiraIssueType));
            }
        }

        return issueTypes;
    }

    public UIIssueType convertJiraIssueTypeToUIIssueType(JiraIssueType jiraIssueType) {
        UIIssueType uiIssueType = new UIIssueType();
        uiIssueType.setName(jiraIssueType.getName());
        uiIssueType.setWorkflowKey(jiraIssueType.getWorkflowKey());
        uiIssueType.setSelf(jiraIssueType.getSelf());
        uiIssueType.setProjectId(jiraIssueType.getProjectId());
        uiIssueType.setSubtask(jiraIssueType.getSubtask());
        uiIssueType.setId(jiraIssueType.getId());
        uiIssueType.setIconUrl(jiraIssueType.getIconUrl());
        uiIssueType.setAvatarId(jiraIssueType.getAvatarId());

        return uiIssueType;
    }

    public UIWorkflowQueueJob createUIWorkflowQueueJob(WorkflowDiscoveryQueueJob job) {
        UIWorkflowQueueJob uiJob = new UIWorkflowQueueJob();

        uiJob.setAttempts(job.getAttempts());
        uiJob.setJobKey(job.getJobKey());
        uiJob.setKey(job.getKey());
        uiJob.setSubDomain(job.getSubDomain());
        uiJob.setTypeId(job.getTypeId());
        uiJob.setStatusId(job.getStatusId());
        uiJob.setDiscoveryStatus(job.getDiscoveryStatus());
        uiJob.setProject(job.getProject());
        uiJob.setWorkflowKey(job.getWorkflowKey());
        uiJob.setTitle(job.getTitle());

        return uiJob;
    }

    public void updateWorkflowDiscoveryQueueJob(UIWorkflowQueueJob uiJob, WorkflowDiscoveryQueueJob queueJob) {
        queueJob.setJobKey(uiJob.getJobKey());
        queueJob.setKey(uiJob.getKey());
        queueJob.setSubDomain(uiJob.getSubDomain());
        queueJob.setTypeId(uiJob.getTypeId());
        queueJob.setStatusId(uiJob.getStatusId());
        queueJob.setDiscoveryStatus(uiJob.getDiscoveryStatus());
        queueJob.setProject(uiJob.getProject());
        queueJob.setWorkflowKey(uiJob.getWorkflowKey());
        queueJob.setTitle(uiJob.getTitle());
    }

    public List<UIWorkflowQueueJob> convertWorkflowsToUIWorkflowJob(RealmResults<WorkflowDiscoveryQueueJob> workflowJobs) {
        List<UIWorkflowQueueJob> jobs = new ArrayList<>();
        for (WorkflowDiscoveryQueueJob workflowJob : workflowJobs) {
            jobs.add(createUIWorkflowQueueJob(workflowJob));
        }

        return jobs;
    }

    public HashSet<UIIssueStatus> createDistinctUIIssueStatuses(RealmResults<JiraIssueType> jiraIssueTypes) {
        HashSet<UIIssueStatus> issueStatuses = new HashSet<>();
        for (JiraIssueType jiraIssueType : jiraIssueTypes) {
            for (JiraIssueStatus status : jiraIssueType.getStatuses()) {
                issueStatuses.add(createIssueStatusFromJiraIssueStatus(status));
            }
        }

        return issueStatuses;
    }

    public UIIssueTransition createUIIssueTransitionsFromDirectTransition(Transition transition, @Nullable Status currentStatus) {
        UIIssueTransition uiIssueTransition = new UIIssueTransition();
        uiIssueTransition.viaId = transition.getId();
        uiIssueTransition.viaName = transition.getName();
        uiIssueTransition.toId = transition.getTo().getId();
        uiIssueTransition.toName = transition.getTo().getName();
        uiIssueTransition.toColor = transition.getTo().getStatusCategory().getColorName();
        if (currentStatus != null) {
            uiIssueTransition.fromId = currentStatus.getId();
            uiIssueTransition.fromName = currentStatus.getName();
            uiIssueTransition.fromColor = currentStatus.getStatusCategory().getColorName();
        }

        return uiIssueTransition;
    }

    public UIIssueTransition createUIIssueTransitionsFromDirectTransition(Transition transition) {
        return createUIIssueTransitionsFromDirectTransition(transition, null);
    }

    private UIIssueStatus createIssueStatusFromJiraIssueStatus(JiraIssueStatus status) {
        UIIssueStatus issueStatus = new UIIssueStatus();
        issueStatus.setName(status.getName());
        issueStatus.setId(status.getId());
        issueStatus.setIconUrl(status.getIconUrl());
        issueStatus.setSelf(status.getSelf());
        issueStatus.setDescription(status.getDescription());
        issueStatus.setStatusCategory(createUIStatusCategoryFromJiraStatusCategory(status.getStatusCategory()));

        return issueStatus;
    }

    private UIStatusCategory createUIStatusCategoryFromJiraStatusCategory(JiraStatusCategory statusCategory) {
        UIStatusCategory uiStatusCategory = new UIStatusCategory();
        uiStatusCategory.setSelf(statusCategory.getSelf());
        uiStatusCategory.setName(statusCategory.getName());
        uiStatusCategory.setId(statusCategory.getId());
        uiStatusCategory.setColorName(statusCategory.getColorName());
        uiStatusCategory.setKey(statusCategory.getKey());

        return uiStatusCategory;
    }

    public HashSet<UIIssueTransition> convertVerticesToDistinctUIIssueTransitions(RealmResults<Vertices> vertices) {
        HashSet<UIIssueTransition> transitions = new HashSet<>();
        for (Vertices vertex : vertices) {
            transitions.add(createTransitionFromVertex(vertex));
        }

        return transitions;
    }

    private UIIssueTransition createTransitionFromVertex(Vertices vertex) {
        UIIssueTransition transition = new UIIssueTransition();
        transition.fromId = vertex.getSourceStatusId();
        transition.fromName = vertex.getSourceStatusName();
        transition.toName = vertex.getTargetStatusName();
        transition.toId = vertex.getTargetStatusId();
        transition.viaId = vertex.getLinkId();
        transition.viaName = vertex.getLinkName();

        return transition;
    }

    public TransitionJob createTransitionJobFromTransitionQueueJob(TransitionQueueJob queueJob) {
        TransitionJob transitionJob = new TransitionJob();
        transitionJob.setAttempts(queueJob.getAttempts());
        transitionJob.setCurrentState(queueJob.getCurrentState());
        transitionJob.setIssueKey(queueJob.getIssueKey());
        transitionJob.setJobKey(queueJob.getJobKey());
        transitionJob.setProjectId(queueJob.getProjectId());
        transitionJob.setStatus(queueJob.getStatus());
        transitionJob.setSubDomain(queueJob.getSubDomain());
        transitionJob.setTransitionStepsSerialized(queueJob.getTransitionSteps());

        return transitionJob;
    }

    public void updateTransitionQueueJob(TransitionQueueJob queueJob, TransitionJob job) {
        queueJob.setAttempts(job.getAttempts());
        queueJob.setCurrentState(job.getCurrentState());
        queueJob.setStatus(job.getStatus());
        queueJob.setTransitionSteps(job.getTransitionStepsSerialized());
    }
}
