package com.fullmob.jiraboard.di.modules;

import com.fullmob.jiraboard.analyzer.BoardAnalyzer;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.processors.ImageProcessor;
import com.fullmob.jiraboard.providers.BitmapsProvider;
import com.fullmob.jiraboard.ui.home.cameraboard.CaptureBoardPresenter;
import com.fullmob.jiraboard.ui.home.cameraboard.CaptureBoardView;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

@Module
public class CaptureBoardModule {

    private final WeakReference<CaptureBoardView> view;

    public CaptureBoardModule(CaptureBoardView view) {
        this.view = new WeakReference<>(view);
    }

    @Provides
    public CaptureBoardPresenter providesCaptureBoardPresenter(
        DBManagerInterface dbManager,
        ImageProcessor processor,
        BoardAnalyzer boardAnalyzer,
        EncryptedStorage encryptedStorage,
        BitmapsProvider bitmapsProvider
    ) {
        return new CaptureBoardPresenter(
            view.get(),
            processor,
            boardAnalyzer,
            dbManager,
            encryptedStorage,
            bitmapsProvider
        );
    }
}
