package com.fullmob.jiraboard.di.components;

import com.fullmob.jiraboard.di.modules.HomeScreenModule;
import com.fullmob.jiraboard.ui.home.HomeActivity;

import dagger.Subcomponent;

/**
 * Created by shehabic on 02/04/2017.
 */
@Subcomponent(modules = {HomeScreenModule.class})
public interface HomeScreenComponent {
    void inject(HomeActivity homeActivity);
}
