package com.fullmob.jiraboard.di.components;

import com.fullmob.jiraboard.di.modules.TransitionScreenModule;
import com.fullmob.jiraboard.ui.transitions.TransitionsActivity;

import dagger.Subcomponent;

/**
 * Created by shehabic on 26/03/2017.
 */

@Subcomponent(modules = {TransitionScreenModule.class})
public interface TransitionsScreenComponent {
    void inject(TransitionsActivity activity);
}
