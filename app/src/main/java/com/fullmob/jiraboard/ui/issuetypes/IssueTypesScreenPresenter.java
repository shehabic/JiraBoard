package com.fullmob.jiraboard.ui.issuetypes;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.managers.workflow.WorkflowDiscoveryManager;
import com.fullmob.jiraboard.ui.AbstractPresenter;
import com.fullmob.jiraboard.ui.models.IssueTypesAndWorkflows;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIProject;
import com.fullmob.jiraboard.ui.models.UIWorkflowQueueJob;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

public class IssueTypesScreenPresenter extends AbstractPresenter<IssueTypesView> {

    private final ProjectsManager projectsManager;
    private final WorkflowDiscoveryManager workflowDiscoveryManager;
    private final IssuesManager issuesManager;
    private IssueTypesAndWorkflows issueTypesAndWorkflows;

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

    public void onViewCreated(IssueTypesAndWorkflows issueTypesAndWorkflows) {
        this.issueTypesAndWorkflows = issueTypesAndWorkflows;
    }

    public void onViewResumed() {
        if (issueTypesAndWorkflows == null) {
            loadProjectIssueTypes();
        } else {
            getView().renderWorkflowAndTypes(issueTypesAndWorkflows);
        }
    }

    private void loadProjectIssueTypes() {
        getView().showLoading();
        Subscription subscription = Observable.zip(
            projectsManager.findAllIssueTypesInCurrentProject(),
            projectsManager.findUniqueWorkflowsInCurrentProject(),
            projectsManager.findAllProcessedAndInProgressWorkflowsInCurrentProject().flatMap(
                new Func1<List<UIWorkflowQueueJob>, Observable<Map<String, HashSet<String>>>>() {
                    @Override
                    public Observable<Map<String, HashSet<String>>> call(List<UIWorkflowQueueJob> uiWorkflowQueueJobs) {
                        Map<String, HashSet<String>> workflows = new HashMap<>();
                        workflows.put(UIWorkflowQueueJob.STATUS_PENDING, new HashSet<String>());
                        workflows.put(UIWorkflowQueueJob.STATUS_PROCESSED, new HashSet<String>());
                        workflows.put(UIWorkflowQueueJob.STATUS_PROCESSING, new HashSet<String>());
                        workflows.put(UIWorkflowQueueJob.STATUS_FAILED, new HashSet<String>());
                        for (UIWorkflowQueueJob job : uiWorkflowQueueJobs) {
                            workflows.get(job.getDiscoveryStatus()).add(job.getWorkflowKey());
                        }

                        return Observable.just(workflows);
                    }
                }
            ),
            new Func3<List<UIIssueType>, HashSet<String>, Map<String, HashSet<String>>, IssueTypesAndWorkflows>() {
                @Override
                public IssueTypesAndWorkflows call(List<UIIssueType> uiIssueTypes, HashSet<String> uniqueWorkflows, Map<String, HashSet<String>> uiWorkflowQueueJobs) {
                    for (UIIssueType issueType : uiIssueTypes) {
                        String workflowKey = issueType.getWorkflowKey();
                        if (uiWorkflowQueueJobs.get(UIWorkflowQueueJob.STATUS_PROCESSED).contains(workflowKey)) {
                            issueType.setDiscoveryStatus(UIWorkflowQueueJob.STATUS_PROCESSED);
                        } else if (uiWorkflowQueueJobs.get(UIWorkflowQueueJob.STATUS_PENDING).contains(workflowKey)) {
                            issueType.setDiscoveryStatus(UIWorkflowQueueJob.STATUS_PENDING);
                        } else if (uiWorkflowQueueJobs.get(UIWorkflowQueueJob.STATUS_PROCESSING).contains(workflowKey)) {
                            issueType.setDiscoveryStatus(UIWorkflowQueueJob.STATUS_PROCESSING);
                        }
                    }

                    return new IssueTypesAndWorkflows(uiIssueTypes, uniqueWorkflows);
                }
            }
        )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<IssueTypesAndWorkflows>() {
            @Override
            public void call(IssueTypesAndWorkflows typesAndWorkflows) {
                issueTypesAndWorkflows = typesAndWorkflows;
                getView().hideLoading();
                getView().renderWorkflowAndTypes(typesAndWorkflows);
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

    public IssueTypesAndWorkflows getIssueTypesAndWorkflows() {
        return issueTypesAndWorkflows;
    }

    public void onNextClicked() {
        getView().proceedToHomeScreen();
    }
}
