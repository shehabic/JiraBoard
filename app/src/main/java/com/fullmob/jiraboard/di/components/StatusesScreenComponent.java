package com.fullmob.jiraboard.di.components;

import com.fullmob.jiraboard.di.modules.StatusesScreenModule;
import com.fullmob.jiraboard.ui.home.statuses.StatusesFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {StatusesScreenModule.class})
public interface StatusesScreenComponent {
    void inject(StatusesFragment statusesFragment);
}
