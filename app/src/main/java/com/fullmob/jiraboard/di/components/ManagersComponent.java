package com.fullmob.jiraboard.di.components;


import com.fullmob.jiraboard.di.modules.LoginScreenModule;
import com.fullmob.jiraboard.di.modules.ManagersModule;
import com.fullmob.jiraboard.managers.user.UserManager;

import dagger.Subcomponent;

@Subcomponent(modules = {ManagersModule.class})
public interface ManagersComponent {
    UserManager getUserManager();
    LoginScreenComponent plus(LoginScreenModule loginScreenModule);
}
