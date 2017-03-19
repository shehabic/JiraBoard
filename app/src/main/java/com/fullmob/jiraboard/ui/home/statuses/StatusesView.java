package com.fullmob.jiraboard.ui.home.statuses;

import com.fullmob.jiraboard.ui.BaseView;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;

import java.util.List;

public interface StatusesView extends BaseView {
    void renderIssueStatuses(List<UIIssueStatus> uiIssueStatuses);

    void showErrorOccurred(Throwable throwable);
}
