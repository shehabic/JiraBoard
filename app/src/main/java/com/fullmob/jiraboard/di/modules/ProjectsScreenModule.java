package com.fullmob.jiraboard.di.modules;

import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.ui.projects.ProjectsPresenter;
import com.fullmob.jiraboard.ui.projects.ProjectsView;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

@Module
public class ProjectsScreenModule {
    private final WeakReference<ProjectsView> view;

    public ProjectsScreenModule(ProjectsView view) {
        this.view = new WeakReference<>(view);
    }

    @Provides
    public ProjectsPresenter providesProjectsPresenter(ProjectsManager projectsManager) {
        return new ProjectsPresenter(view.get(), projectsManager);
    }
}
