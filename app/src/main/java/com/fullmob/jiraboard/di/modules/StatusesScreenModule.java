package com.fullmob.jiraboard.di.modules;

import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.ui.home.statuses.StatusesScreenPresenter;
import com.fullmob.jiraboard.ui.home.statuses.StatusesView;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

@Module
public class StatusesScreenModule {

    private final WeakReference<StatusesView> view;

    public StatusesScreenModule(StatusesView view) {
        this.view = new WeakReference<StatusesView>(view);
    }

    @Provides
    public StatusesScreenPresenter providesStatusesScreenPresenter(
        ProjectsManager projectsManager,
        IssuesManager issuesManager
    ) {
        return new StatusesScreenPresenter(view.get(), projectsManager, issuesManager);
    }
}
