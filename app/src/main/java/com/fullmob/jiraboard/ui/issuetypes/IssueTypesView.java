package com.fullmob.jiraboard.ui.issuetypes;

import com.fullmob.jiraboard.ui.BaseView;
import com.fullmob.jiraboard.ui.models.IssueTypesAndWorkflows;

public interface IssueTypesView extends BaseView {
    void showErrorOccurred();
    void renderWorkflowAndTypes(IssueTypesAndWorkflows issueTypesAndWorkflows);
}
