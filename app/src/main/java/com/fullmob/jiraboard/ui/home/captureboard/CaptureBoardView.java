package com.fullmob.jiraboard.ui.home.captureboard;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.data.Board;
import com.fullmob.jiraboard.transitions.TransitionSteps;
import com.fullmob.jiraboard.ui.BaseView;
import com.fullmob.jiraboard.ui.models.CapturedBoardListItem;
import com.fullmob.jiraboard.ui.transitions.OnConfirmTransitionCallback;

import java.util.List;

public interface CaptureBoardView extends BaseView {

    void onAnalysisCompleted(Board project);

    void showErrorOccurred();

    void renderAnalysisResult(List<CapturedBoardListItem> boardResult);

    void showTransitionConfirmation(TransitionSteps steps, Issue issue, OnConfirmTransitionCallback callback);

    void showToast(String msg);
}
