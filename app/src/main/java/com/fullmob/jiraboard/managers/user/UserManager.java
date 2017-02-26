package com.fullmob.jiraboard.managers.user;

import com.fullmob.jiraapi.managers.JiraCloudUserManager;
import com.fullmob.jiraboard.managers.storage.LocalStorageInterface;

public class UserManager {

    private final LocalStorageInterface localStorage;
    private final JiraCloudUserManager jiraUserManager;

    public UserManager(JiraCloudUserManager userManager, LocalStorageInterface localStorage) {
        this.jiraUserManager = userManager;
        this.localStorage = localStorage;
    }
}
