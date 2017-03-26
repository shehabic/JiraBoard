package com.fullmob.jiraboard.ui.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shehabic on 26/03/2017.
 */
public class UIIssueTransitionGroups {
    private final List<UIIssueTransition> directTransitions;
    private final List<UIIssueTransition> furtherTransitions;

    public UIIssueTransitionGroups() {
        directTransitions = new ArrayList<>();
        furtherTransitions = new ArrayList<>();
    }

    public List<UIIssueTransition> getDirectTransitions() {
        return directTransitions;
    }

    public List<UIIssueTransition> getFurtherTransitions() {
        return furtherTransitions;
    }
}
