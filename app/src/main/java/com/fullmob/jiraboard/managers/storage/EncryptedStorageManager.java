package com.fullmob.jiraboard.managers.storage;

import com.fullmob.jiraboard.managers.security.EncrypterInterface;
import com.fullmob.jiraboard.ui.models.UIProject;

public class EncryptedStorageManager implements EncryptedStorage {

    private static final String JIRA_KEY_USERNAME = "jira_username";
    private static final String JIRA_KEY_PASSWORD = "jira_password";
    private static final String JIRA_KEY_SUBDOMAIN = "jira_subdomain";
    private static final String JIRA_DEFAULT_PROJECT = "jira_default_project";
    private static final String JIRA_LAST_SEARCH_QUERY = "jira_last_search_";

    private final LocalStorageInterface localStorage;
    private final EncrypterInterface encrypter;

    public EncryptedStorageManager(
        LocalStorageInterface localStorageInterface,
        EncrypterInterface encrypter
    ) {
        this.localStorage = localStorageInterface;
        this.encrypter = encrypter;
    }

    @Override
    public String getPassword() {
        String encPassword = localStorage.getString(JIRA_KEY_PASSWORD);
        try {
            return encPassword != null ? encrypter.decrypt(JIRA_KEY_PASSWORD, encPassword) : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getUsername() {
        return localStorage.getString(JIRA_KEY_USERNAME);
    }

    @Override
    public String getSubDomain() {
        return localStorage.getString(JIRA_KEY_SUBDOMAIN);
    }

    @Override
    public String getDefaultProject() {
        return getDefaultProject(null);
    }

    private String getDefaultProject(String defaultValue) {
        return localStorage.getString(JIRA_DEFAULT_PROJECT, defaultValue);
    }

    @Override
    public void saveSubDomain(String subDomain) {
        localStorage.putString(JIRA_KEY_SUBDOMAIN, subDomain);
    }

    @Override
    public void saveUsername(String user) {
        localStorage.putString(JIRA_KEY_USERNAME, user);
    }

    @Override
    public void savePassword(String password) {
        try {
            localStorage.putString(JIRA_KEY_PASSWORD, encrypter.encrypt(JIRA_KEY_PASSWORD, password));
        } catch (Exception e) {
        }
    }

    @Override
    public void deletePassword() {
        localStorage.delete(JIRA_KEY_PASSWORD);
    }

    @Override
    public void deleteDefaultProjecT() {
        localStorage.delete(JIRA_DEFAULT_PROJECT);
    }

    @Override
    public void saveDefaultProject(UIProject project) {
        localStorage.putString(JIRA_DEFAULT_PROJECT, project.getId());
    }

    @Override
    public String getLastSavedSearch() {
        return localStorage.getString(getSearchKey(), "");
    }

    private String getSearchKey() {
        return JIRA_LAST_SEARCH_QUERY + getDefaultProject("_000000");
    }

    @Override
    public void saveLastSearch(String searchText) {
        localStorage.putString(getSearchKey(), searchText);
    }
}
