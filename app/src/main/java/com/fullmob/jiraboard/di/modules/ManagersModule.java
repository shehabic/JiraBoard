package com.fullmob.jiraboard.di.modules;


import com.fullmob.jiraapi.managers.JiraCloudUserManager;
import com.fullmob.jiraboard.managers.storage.LocalStorageInterface;
import com.fullmob.jiraboard.managers.user.UserManager;

import dagger.Module;
import dagger.Provides;

@Module
public class ManagersModule {
    @Provides
    public UserManager providesUserManager(JiraCloudUserManager userManager, LocalStorageInterface localStorage) {
        return new UserManager(userManager, localStorage);
    }
}
