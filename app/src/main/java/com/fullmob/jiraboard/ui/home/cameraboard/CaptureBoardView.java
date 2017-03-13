package com.fullmob.jiraboard.ui.home.cameraboard;

import com.fullmob.jiraboard.data.Board;
import com.fullmob.jiraboard.ui.BaseView;

public interface CaptureBoardView extends BaseView {
    void showOutput(Board project);

    void showErrorOccurred();
}
