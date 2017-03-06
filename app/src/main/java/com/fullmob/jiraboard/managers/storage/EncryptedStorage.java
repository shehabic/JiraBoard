package com.fullmob.jiraboard.managers.storage;

import com.fullmob.jiraboard.ui.models.UIProject;

public interface EncryptedStorage {
    String getUsername();
    String getPassword();
    String getSubDomain();
    String getDefaultProject();
    void saveSubDomain(String subDomain);
    void saveUsername(String user);
    void savePassword(String password);
    void deletePassword();
    void saveDefaultProject(UIProject jiraProject);

}
