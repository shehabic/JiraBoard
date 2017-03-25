package com.fullmob.jiraboard.di.components;

import com.fullmob.jiraboard.di.modules.IssueScreenModule;
import com.fullmob.jiraboard.ui.issue.IssueActivity;

import dagger.Subcomponent;

/**
 * Created by shehabic on 25/03/2017.
 */
@Subcomponent(modules = {IssueScreenModule.class})
public interface IssueScreenComponent {
    void inject(IssueActivity issueActivity);
}
