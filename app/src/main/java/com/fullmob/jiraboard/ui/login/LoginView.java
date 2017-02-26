package com.fullmob.jiraboard.ui.login;

import com.fullmob.jiraboard.ui.BaseView;

public interface LoginView extends BaseView {
    void proceedToHomeScreen();

    void showInvalidCredentials();

    void showNetworkError();

    void showUnknownError();

    void preFillEmail(String email);

    void preFillSubDomain(String subDomain);

    void showLoading();

    void hideLoading();
}
