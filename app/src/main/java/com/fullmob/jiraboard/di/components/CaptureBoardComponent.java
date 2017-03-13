package com.fullmob.jiraboard.di.components;

import com.fullmob.jiraboard.di.modules.CaptureBoardModule;
import com.fullmob.jiraboard.ui.home.captureboard.CaptureBoardFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {CaptureBoardModule.class})
public interface CaptureBoardComponent {
    void inject(CaptureBoardFragment fragment);
}
