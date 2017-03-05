package com.fullmob.jiraboard.di.components;


import com.fullmob.jiraboard.app.FullmobAppInterface;
import com.fullmob.jiraboard.di.modules.MainModule;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.security.EncrypterInterface;
import com.fullmob.jiraboard.managers.serializers.SerializerInterface;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.managers.storage.LocalStorageInterface;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MainModule.class})
public interface MainComponent {
    LocalStorageInterface getLocalStorage();

    SerializerInterface getSerializer();

    FullmobAppInterface getApp();

    EncryptedStorage getEncryptedStorage();

    EncrypterInterface getEncryptionManager();

    DBManagerInterface getDb();

    ApiComponent plus();
}
