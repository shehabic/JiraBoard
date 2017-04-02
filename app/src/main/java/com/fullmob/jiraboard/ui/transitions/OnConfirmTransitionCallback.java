package com.fullmob.jiraboard.ui.transitions;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.transitions.TransitionSteps;

/**
 * Created by shehabic on 02/04/2017.
 */
public interface OnConfirmTransitionCallback {
    void onConfirmTransition(TransitionSteps steps, Issue issue);
}
