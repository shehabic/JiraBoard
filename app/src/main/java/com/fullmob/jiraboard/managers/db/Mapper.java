package com.fullmob.jiraboard.managers.db;


import android.support.annotation.NonNull;

import com.fullmob.jiraapi.models.AvatarUrls;
import com.fullmob.jiraapi.models.Project;
import com.fullmob.jiraboard.db.data.JiraAvatarUrls;
import com.fullmob.jiraboard.db.data.JiraProject;
import com.fullmob.jiraboard.ui.models.UIAvatarUrls;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public JiraProject createJiraProjectFromProject(Project proj, String subDomain) {
        return createJiraProjectFromProject(new JiraProject(), proj, subDomain);
    }

    public JiraProject createJiraProjectFromProject(JiraProject jiraProject, Project proj, String subDomain) {
        jiraProject.setSubDomain(subDomain);
        jiraProject.setName(proj.getName());
        jiraProject.setId(proj.getId());
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
        uiProject.setId(project.getId());
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
}
