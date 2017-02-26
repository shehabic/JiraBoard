package com.fullmob.jiraboard.managers.security;

public interface EncrypterInterface {
    boolean init();

    String encrypt(String key, String plainText) throws Exception;

    String decrypt(String key, String cipherText) throws Exception;
}
