package com.fullmob.jiraboard.di.modules;

import com.fullmob.jiraboard.app.FullmobAppInterface;
import com.fullmob.jiraboard.app.FullmobDiApp;
import com.fullmob.jiraboard.managers.security.EncrypterInterface;
import com.fullmob.jiraboard.managers.security.EncryptionManager;
import com.fullmob.jiraboard.managers.serializers.GsonSerializer;
import com.fullmob.jiraboard.managers.serializers.SerializerInterface;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.managers.storage.EncryptedStorageManager;
import com.fullmob.jiraboard.managers.storage.LocalStorageInterface;
import com.fullmob.jiraboard.managers.storage.LocalStorageManager;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class MainModule {
    private final FullmobDiApp app;

    public MainModule(FullmobDiApp app) {
        this.app = app;
    }

    @Provides
    public FullmobAppInterface providesFullmobAppInterface() {
        return app;
    }

    @Provides
    public SerializerInterface providesSerializerInterface() {
        GsonBuilder builder = new GsonBuilder();

        return new GsonSerializer(builder.create());
    }

    @Provides
    public LocalStorageInterface localStorageInterface(SerializerInterface serializer) {
        return new LocalStorageManager(serializer, app.getContext());
    }

    @Provides
    public EncrypterInterface encryptionManager() {
        return new EncryptionManager(app.getContext());
    }

    @Provides
    public EncryptedStorage providesEncryptedStorage(EncrypterInterface encrypter, LocalStorageInterface localStorage) {
        return new EncryptedStorageManager(localStorage, encrypter);
    }
}
