package com.fullmob.jiraboard.di.components;


import com.fullmob.jiraboard.analyzer.BoardAnalyzer;
import com.fullmob.jiraboard.app.FullmobAppInterface;
import com.fullmob.jiraboard.di.modules.MainModule;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.printable.PrintableImageGenerator;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.managers.notifications.NotificationsManager;
import com.fullmob.jiraboard.managers.queue.QueueManager;
import com.fullmob.jiraboard.managers.security.EncrypterInterface;
import com.fullmob.jiraboard.managers.serializers.SerializerInterface;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.managers.storage.LocalStorageInterface;
import com.fullmob.jiraboard.processors.ImageProcessor;
import com.fullmob.jiraboard.providers.BitmapsProvider;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MainModule.class})
public interface MainComponent {
    LocalStorageInterface getLocalStorage();

    SerializerInterface getSerializer();

    QueueManager getQueueManager();

    PrintableImageGenerator getQRBitmapGenerator();

    NotificationsManager getNotificationsManager();

    ImageProcessor getImageProcessor();

    BoardAnalyzer getBoardAnalyzer();

    BitmapsProvider getBitmapsProvider();

    FullmobAppInterface getApp();

    SecuredImagesManagerInterface getSecuredImagesManagerInterface();

    EncryptedStorage getEncryptedStorage();

    EncrypterInterface getEncryptionManager();

    DBManagerInterface getDb();

    ApiComponent plus();

}
