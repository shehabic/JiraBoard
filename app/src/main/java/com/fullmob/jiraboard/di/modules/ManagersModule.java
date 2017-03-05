package com.fullmob.jiraboard.di.modules;


import com.fullmob.jiraapi.managers.JiraCloudUserManager;
import com.fullmob.jiraapi.managers.ProjectsManager;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.projects.ProjManager;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
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

    @Provides
    public ProjManager providesProjManager(ProjectsManager manager, EncryptedStorage storage, DBManagerInterface db) {
        return new ProjManager(manager, db, storage);
    }
}
