package com.fullmob.jiraboard.di.components;

import com.fullmob.jiraboard.di.modules.ApiModule;
import com.fullmob.jiraboard.managers.user.AuthenticationManager;

import dagger.Subcomponent;

@Subcomponent(modules = {ApiModule.class})
public interface ApiComponent {
    AuthenticationManager getAuthenticationManager();

    ManagersComponent plus();
}
