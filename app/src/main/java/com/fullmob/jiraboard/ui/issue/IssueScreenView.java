package com.fullmob.jiraboard.ui.issue;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.ui.BaseView;

/**
 * Created by shehabic on 25/03/2017.
 */
public interface IssueScreenView extends BaseView {
    void renderIssue(Issue issue);

    void onError(Throwable throwable);

    void renderTable(String tableHtml);
}
