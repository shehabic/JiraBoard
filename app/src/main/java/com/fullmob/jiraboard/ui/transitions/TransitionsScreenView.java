package com.fullmob.jiraboard.ui.transitions;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.transitions.TransitionSteps;
import com.fullmob.jiraboard.ui.BaseView;
import com.fullmob.jiraboard.ui.models.UITransitionItem;

import java.util.List;

/**
 * Created by shehabic on 26/03/2017.
 */
public interface TransitionsScreenView extends BaseView {

    void renderTransitions(List<UITransitionItem> transitionItemList);

    void showError(Throwable throwable);

    void showNoTransitionsAvailableForCurrentState();

    void showTransitionConfirmation(TransitionSteps steps, Issue issue, OnConfirmTransitionCallback callback);
}
