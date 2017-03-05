package com.fullmob.jiraboard.ui.projects;

import com.fullmob.jiraboard.ui.BaseView;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.List;

public interface ProjectsView extends BaseView {
    void renderProjects(List<UIProject> jiraProjects);

    void goBackToLoginScreen();

    void showNoProjectsFoundError();

    void showErrorOccurred();

    void proceedToMainScreen();
}
