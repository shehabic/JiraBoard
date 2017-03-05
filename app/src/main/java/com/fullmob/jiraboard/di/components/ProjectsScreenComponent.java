package com.fullmob.jiraboard.di.components;

import com.fullmob.jiraboard.di.modules.ProjectsScreenModule;
import com.fullmob.jiraboard.ui.projects.ProjectsActivity;
import com.fullmob.jiraboard.ui.projects.ProjectsPresenter;

import dagger.Subcomponent;

@Subcomponent(modules = {ProjectsScreenModule.class})
public interface ProjectsScreenComponent {
    ProjectsPresenter getProjectsPresenter();

    void inject(ProjectsActivity activity);
}
