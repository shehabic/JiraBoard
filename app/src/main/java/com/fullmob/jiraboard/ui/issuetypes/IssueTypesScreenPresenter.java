package com.fullmob.jiraboard.ui.issuetypes;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.managers.workflow.WorkflowDiscoveryManager;
import com.fullmob.jiraboard.ui.AbstractPresenter;
import com.fullmob.jiraboard.ui.models.IssueTypesAndWorkflows;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.HashSet;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class IssueTypesScreenPresenter extends AbstractPresenter<IssueTypesView> {

    private final ProjectsManager projectsManager;
    private final WorkflowDiscoveryManager workflowDiscoveryManager;
    private final IssuesManager issuesManager;

    public IssueTypesScreenPresenter(
        IssueTypesView view,
        ProjectsManager projectsManager,
        WorkflowDiscoveryManager workflowDiscoveryManager,
        IssuesManager issuesManager
    ) {
        super(view);
        this.projectsManager = projectsManager;
        this.workflowDiscoveryManager = workflowDiscoveryManager;
        this.issuesManager = issuesManager;
    }

    public void onViewResumed() {
        loadProjectIssueTypes();
    }

    private void loadProjectIssueTypes() {
        getView().showLoading();
        Subscription subscription = Observable.zip(
            projectsManager.findAllIssueTypesInCurrentProject(),
            projectsManager.findUniqueWorkflowsInCurrentProject(),
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

    public void onIssueTypeClicked(final UIIssueType issueType) {
        projectsManager.getProject(issueType.getProjectId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<UIProject>() {
                @Override
                public void call(UIProject uiProject) {
                    getView().promptForIssueId(uiProject, issueType);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    getView().showErrorOccurred();
                }
            });
    }

    public void onDiscoveryTicketEntered(final UIIssueType issueType, final UIProject uiProject, String ticketKey) {
        getView().showDialogLoading();
        getView().clearDialogError();
        issuesManager.getIssue(uiProject.getKey() + "-" + ticketKey.trim())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribeWith(new Observer<Response<Issue>>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(Response<Issue> response) {
                    getView().hideDialogLoading();
                    Issue issue = response.body();
                    if (issue == null) {
                        getView().showInvalidIssue();
                    } else if (!issue.getIssueFields().getIssuetype().getName().equals(issueType.getName())) {
                        getView().showInvalidIssueType(issueType.getName(), issue.getIssueFields().getIssuetype().getName());
                    } else {
                        getView().showSuccess(issue, issueType, uiProject);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    getView().hideDialogLoading();
                    getView().showInvalidIssue();
                }

                @Override
                public void onComplete() {

                }
            });
    }

    public void onDiscoveryConfirmed(Issue issue, UIIssueType issueType, UIProject uiProject) {
        workflowDiscoveryManager.scheduleDiscoveryTask(issue, uiProject, issueType);
    }
}
