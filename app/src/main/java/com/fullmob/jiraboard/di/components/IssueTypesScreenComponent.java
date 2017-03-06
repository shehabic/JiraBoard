package com.fullmob.jiraboard.di.components;

import com.fullmob.jiraboard.di.modules.IssueTypesModule;
import com.fullmob.jiraboard.ui.issuetypes.IssueTypesActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {IssueTypesModule.class})
public interface IssueTypesScreenComponent {
    void inject(IssueTypesActivity issueTypesActivity);
}
