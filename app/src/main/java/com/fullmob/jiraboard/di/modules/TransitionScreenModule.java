package com.fullmob.jiraboard.di.modules;

import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.ui.transitions.TransitionsScreenPresenter;
import com.fullmob.jiraboard.ui.transitions.TransitionsScreenView;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shehabic on 26/03/2017.
 */

@Module
public class TransitionScreenModule {
    private final WeakReference<TransitionsScreenView> view;

    public TransitionScreenModule(TransitionsScreenView view) {
        this.view = new WeakReference<>(view);
    }

    @Provides
    public TransitionsScreenPresenter providesTransitionsScreenPresenter(
        IssuesManager issuesManager,
        ProjectsManager projectsManager
    ) {
        return new TransitionsScreenPresenter(view.get(), issuesManager, projectsManager);
    }
}
