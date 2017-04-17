package com.fullmob.jiraboard.di.components;

import com.fullmob.jiraboard.di.modules.ApiModule;
import com.fullmob.jiraboard.di.scopes.UserScope;
import com.fullmob.jiraboard.managers.user.AuthenticationManager;

import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = {ApiModule.class})
public interface ApiComponent {
    AuthenticationManager getAuthenticationManager();

    ManagersComponent plus();
}
