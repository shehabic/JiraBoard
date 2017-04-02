package com.fullmob.jiraboard.di.modules;

import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.ui.home.HomeScreenPresenter;
import com.fullmob.jiraboard.ui.home.HomeScreenView;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shehabic on 02/04/2017.
 */

@Module
public class HomeScreenModule {
    private final WeakReference<HomeScreenView> view;

    public HomeScreenModule(HomeScreenView view) {
        this.view = new WeakReference<>(view);
    }

    @Provides
    public HomeScreenPresenter providesHomeScreenPresenter(DBManagerInterface db, EncryptedStorage storage) {
        return new HomeScreenPresenter(view.get(), storage, db);
    }
}
