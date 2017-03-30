package com.fullmob.jiraboard.di.modules;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.fullmob.jiraboard.BuildConfig;
import com.fullmob.jiraboard.analyzer.BoardAnalyzer;
import com.fullmob.jiraboard.app.FullmobAppInterface;
import com.fullmob.jiraboard.app.FullmobDiApp;
import com.fullmob.jiraboard.managers.db.DBManager;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.images.GlideSecuredImagesLoader;
import com.fullmob.printable.generators.PrintableImageGenerator;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.managers.notifications.NotificationsManager;
import com.fullmob.jiraboard.managers.queue.QueueManager;
import com.fullmob.jiraboard.managers.security.EncrypterInterface;
import com.fullmob.jiraboard.managers.security.EncryptionManager;
import com.fullmob.jiraboard.managers.serializers.GsonSerializer;
import com.fullmob.jiraboard.managers.serializers.SerializerInterface;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.managers.storage.EncryptedStorageManager;
import com.fullmob.jiraboard.managers.storage.LocalStorageInterface;
import com.fullmob.jiraboard.managers.storage.LocalStorageManager;
import com.fullmob.jiraboard.processors.ImageProcessor;
import com.fullmob.jiraboard.providers.BitmapsProvider;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

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

    @Provides
    public DBManagerInterface providesDBManagerInterface(FullmobAppInterface app, SerializerInterface serializer) {
        Realm.init(app.getContext());


        RealmConfiguration.Builder builder = new RealmConfiguration.Builder();
        builder.schemaVersion(DBManager.DB_VERSION)
            .deleteRealmIfMigrationNeeded();
        Realm.setDefaultConfiguration(builder.build());

        return new DBManager(serializer);
    }

    @Provides
    public SecuredImagesManagerInterface providesSecuredImagesManagerInterface(EncryptedStorage encryptedStorage) {
        return new GlideSecuredImagesLoader(encryptedStorage);
    }

    @Provides
    public QueueManager providesQueueManager(FullmobAppInterface app) {
        return new QueueManager(
            new FirebaseJobDispatcher(
                new GooglePlayDriver(app.getContext())
            )
        );
    }

    @Provides
    public NotificationsManager providesNotificationsManager(FullmobAppInterface app) {
        return new NotificationsManager(app.getContext());
    }

    @Provides
    public BoardAnalyzer providesBoardAnalyzer() {
        return new BoardAnalyzer(BuildConfig.DEBUG);
    }

    @Provides
    public ImageProcessor providesImageProcessor(FullmobAppInterface app) {
        return new ImageProcessor(app.getContext());
    }

    @Provides
    public BitmapsProvider providesBitmapsProvider(FullmobAppInterface app) {
        return new BitmapsProvider(app.getContext());
    }

    @Provides
    public PrintableImageGenerator providesQRBitmapGenerator(FullmobAppInterface appInterface) {
        return new PrintableImageGenerator(appInterface.getContext());
    }
}
