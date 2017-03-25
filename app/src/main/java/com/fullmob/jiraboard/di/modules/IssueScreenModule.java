package com.fullmob.jiraboard.di.modules;

import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.ui.issue.IssueScreenPresenter;
import com.fullmob.jiraboard.ui.issue.IssueScreenView;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

@Module
public class IssueScreenModule {
    private final WeakReference<IssueScreenView> view;


    public IssueScreenModule(IssueScreenView view) {
        this.view = new WeakReference<>(view);
    }

    @Provides
    public IssueScreenPresenter providesIssueScreenPresenter(IssuesManager issuesManager) {
        return new IssueScreenPresenter(view.get(), issuesManager);
    }
}
