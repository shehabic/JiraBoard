package com.fullmob.jiraboard.di.modules;

import com.fullmob.jiraboard.analyzer.BoardAnalyzer;
import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.managers.serializers.SerializerInterface;
import com.fullmob.jiraboard.processors.ImageProcessor;
import com.fullmob.jiraboard.providers.BitmapsProvider;
import com.fullmob.jiraboard.transitions.TransitionManager;
import com.fullmob.jiraboard.ui.home.captureboard.CaptureBoardPresenter;
import com.fullmob.jiraboard.ui.home.captureboard.CaptureBoardView;

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
        ImageProcessor processor,
        BoardAnalyzer boardAnalyzer,
        ProjectsManager projectsManager,
        BitmapsProvider bitmapsProvider,
        IssuesManager issuesManager,
        SerializerInterface serializer,
        TransitionManager transitionManager
    ) {
        return new CaptureBoardPresenter(
            view.get(),
            processor,
            boardAnalyzer,
            projectsManager,
            bitmapsProvider,
            issuesManager,
            serializer,
            transitionManager
        );
    }
}
