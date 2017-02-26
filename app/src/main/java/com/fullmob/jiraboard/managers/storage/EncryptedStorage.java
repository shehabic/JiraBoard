package com.fullmob.jiraboard.managers.storage;

public interface EncryptedStorage {
    String getUsername();
    String getPassword();
    String getSubDomain();
    void saveSubDomain(String subDomain);
    void saveUsername(String user);
    void savePassword(String password);
    void deletePassword();
}
