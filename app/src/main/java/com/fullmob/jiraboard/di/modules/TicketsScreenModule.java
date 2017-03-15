package com.fullmob.jiraboard.di.modules;

import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.ui.home.tickets.TicketsScreenPresenter;
import com.fullmob.jiraboard.ui.home.tickets.TicketsScreenView;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

@Module
public class TicketsScreenModule {
    private final WeakReference<TicketsScreenView> view;

    public TicketsScreenModule(TicketsScreenView view) {
        this.view = new WeakReference<>(view);
    }

    @Provides
    public TicketsScreenPresenter providesTicketsScreenPresenter(
        IssuesManager issuesManager,
        ProjectsManager projectsManager,
        EncryptedStorage encryptedStorage
    ) {
        return new TicketsScreenPresenter(view.get(), projectsManager, issuesManager, encryptedStorage);
    }
}
