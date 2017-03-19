package com.fullmob.jiraboard.ui.home.statuses;

import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.ui.AbstractPresenter;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import rx.functions.Action1;

public class StatusesScreenPresenter extends AbstractPresenter<StatusesView> {

    private final ProjectsManager projectsManager;
    private final IssuesManager issuesManager;

    public StatusesScreenPresenter(StatusesView view, ProjectsManager projectsManager, IssuesManager issuesManager) {
        super(view);
        this.projectsManager = projectsManager;
        this.issuesManager = issuesManager;
    }

    public void onViewCreated() {
        getView().showLoading();
        projectsManager.findAllStatusesInCurrentProject().subscribe(new Action1<HashSet<UIIssueStatus>>() {
            @Override
            public void call(HashSet<UIIssueStatus> uiIssueStatuses) {
                getView().renderIssueStatuses(processIssueStatuses(uiIssueStatuses));
                getView().hideLoading();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                getView().showErrorOccurred(throwable);
                getView().hideLoading();
            }
        });
    }

    private List<UIIssueStatus> processIssueStatuses(HashSet<UIIssueStatus> uiIssueStatuses) {
        List<UIIssueStatus> issueStatuses = new ArrayList<>(uiIssueStatuses);
        Collections.sort(issueStatuses, new Comparator<UIIssueStatus>() {
            @Override
            public int compare(UIIssueStatus status1, UIIssueStatus status2) {
                int val1 = valForColor(status1.getStatusCategory().getColorName());
                int val2 = valForColor(status2.getStatusCategory().getColorName());

                return (val1 == val2) ? status1.getName().compareTo(status2.getName()) : val1 - val2;
            }

            private int valForColor(String colorName) {
                if (colorName.toLowerCase().equals("yellow")) {
                    return 1;
                } else if (colorName.toLowerCase().equals("green")) {
                    return 2;
                }

                return 0;
            }
        });

        return issueStatuses;
    }
}
