package com.fullmob.jiraboard.ui.home.tickets;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.ui.BaseView;

import java.util.List;

public interface TicketsScreenView extends BaseView {
    void renderResultTicket(List<Issue> issues);
    void showSearchError(Throwable throwable);

    void showSearchResults();

    void hideSearchResults();

    void showError();

    void hideError();

    void openIssueItem(Issue issue);
}
