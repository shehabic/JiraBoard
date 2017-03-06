package com.fullmob.jiraboard.ui.issuetypes;

import com.fullmob.jiraboard.managers.projects.ProjManager;
import com.fullmob.jiraboard.managers.workflow.WorkflowDiscoveryManager;
import com.fullmob.jiraboard.ui.AbstractPresenter;
import com.fullmob.jiraboard.ui.models.IssueTypesAndWorkflows;
import com.fullmob.jiraboard.ui.models.UIIssueType;

import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class IssueTypesScreenPresenter extends AbstractPresenter<IssueTypesView> {

    private final ProjManager projManager;
    private final WorkflowDiscoveryManager workflowDiscoveryManager;

    public IssueTypesScreenPresenter(
        IssueTypesView view,
        ProjManager projManager,
        WorkflowDiscoveryManager workflowDiscoveryManager
    ) {
        super(view);
        this.projManager = projManager;
        this.workflowDiscoveryManager = workflowDiscoveryManager;
    }

    public void onViewResumed() {
        loadProjectIssueTypes();
    }

    private void loadProjectIssueTypes() {
        getView().showLoading();
        Subscription subscription = Observable.zip(
            projManager.findAllIssueTypesInCurrentProject(),
            projManager.findUniqueWorkflowsInCurrentProject(),
            new Func2<List<UIIssueType>, HashSet<String>, IssueTypesAndWorkflows>() {
                @Override
                public IssueTypesAndWorkflows call(List<UIIssueType> uiIssueTypes, HashSet<String> uniqueWorkflows) {
                    return new IssueTypesAndWorkflows(uiIssueTypes, uniqueWorkflows);
                }
            }
        )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<IssueTypesAndWorkflows>() {
            @Override
            public void call(IssueTypesAndWorkflows issueTypesAndWorkflows) {
                getView().hideLoading();
                getView().renderWorkflowAndTypes(issueTypesAndWorkflows);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                getView().hideLoading();
                getView().showErrorOccurred();
            }
        });
    }

    public void onViewPaused() {

    }

    public void onIssueTypeClicked(UIIssueType issueType) {

    }
}
