package com.fullmob.jiraboard.ui.issuetypes;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.ui.BaseView;
import com.fullmob.jiraboard.ui.models.IssueTypesAndWorkflows;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIProject;

public interface IssueTypesView extends BaseView {
    void showErrorOccurred();
    void renderWorkflowAndTypes(IssueTypesAndWorkflows issueTypesAndWorkflows);
    void promptForIssueId(UIProject uiProject, UIIssueType issueType);

    void showDialogError(String message);

    void showInvalidIssue();

    void showInvalidIssueType(String expectedType, String foundType);

    void showSuccess(Issue issue, UIIssueType issueType, UIProject uiProject);

    void showDialogLoading();

    void hideDialogLoading();

    void clearDialogError();
}
