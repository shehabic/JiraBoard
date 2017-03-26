package com.fullmob.jiraboard.ui.transitions;

import com.fullmob.jiraboard.ui.BaseView;
import com.fullmob.jiraboard.ui.models.UITransitionItem;

import java.util.List;

/**
 * Created by shehabic on 26/03/2017.
 */
public interface TransitionsScreenView extends BaseView {

    void renderTransitions(List<UITransitionItem> transitionItemList);

    void showError(Throwable throwable);
}
