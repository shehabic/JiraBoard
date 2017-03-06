package com.fullmob.jiraboard.di.modules;

import com.fullmob.jiraboard.managers.projects.ProjManager;
import com.fullmob.jiraboard.managers.workflow.WorkflowDiscoveryManager;
import com.fullmob.jiraboard.ui.issuetypes.IssueTypesScreenPresenter;
import com.fullmob.jiraboard.ui.issuetypes.IssueTypesView;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

@Module
public class IssueTypesModule {
    private final WeakReference<IssueTypesView> view;

    public IssueTypesModule(IssueTypesView view) {
        this.view = new WeakReference<>(view);
    }

    @Provides
    public IssueTypesScreenPresenter providesIssueTypesScreenPresenter(
        ProjManager projManager,
        WorkflowDiscoveryManager workflowDiscoveryManager
    ) {
        return new IssueTypesScreenPresenter(view.get(), projManager, workflowDiscoveryManager);
    }

}
