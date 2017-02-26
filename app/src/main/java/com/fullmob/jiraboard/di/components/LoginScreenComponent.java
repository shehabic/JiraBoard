package com.fullmob.jiraboard.di.components;


import com.fullmob.jiraboard.di.modules.LoginScreenModule;
import com.fullmob.jiraboard.ui.login.LoginActivity;
import com.fullmob.jiraboard.ui.login.LoginPresenter;

import dagger.Subcomponent;

@Subcomponent(modules = {LoginScreenModule.class})
public interface LoginScreenComponent {
    LoginPresenter getLoginPresenter();

    void inject(LoginActivity activity);
}
