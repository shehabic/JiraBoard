package com.fullmob.jiraboard.analyzer;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.data.Column;
import com.fullmob.jiraboard.data.Ticket;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardResult {
    public final List<Issue> issues = new ArrayList<>();
    public final List<UIIssueStatus> statuses = new ArrayList<>();
    public final Map<String, UIIssueStatus> columnsToStatuses = new HashMap<>();
    public final Map<String, Issue> keysToIssues = new HashMap<>();
    public final Map<UIIssueStatus, List<Issue>> statusesAndIssues = new HashMap<>();
    public final List<Column> columns = new ArrayList<>();
    public final List<Ticket> tickets = new ArrayList<>();

    public boolean isComplete() {
        return issues.size() > 0 &&
            statuses.size() > 0 &&
            columnsToStatuses.size() > 0 &&
            keysToIssues.size() > 0 &&
            columns.size() > 0 &&
            tickets.size() > 0;
    }
}
