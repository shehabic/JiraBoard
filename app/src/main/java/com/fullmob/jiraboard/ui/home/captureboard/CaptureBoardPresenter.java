package com.fullmob.jiraboard.ui.home.captureboard;

import android.net.Uri;

import com.fullmob.jiraboard.analyzer.BoardAnalyzer;
import com.fullmob.jiraboard.data.Board;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.processors.ImageProcessor;
import com.fullmob.jiraboard.providers.BitmapsProvider;
import com.fullmob.jiraboard.ui.AbstractPresenter;
import com.fullmob.jiraboard.ui.models.UIProject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

public class CaptureBoardPresenter extends AbstractPresenter<CaptureBoardView> {

    private final ImageProcessor imageProcessor;
    private final BoardAnalyzer boardAnalyzer;
    private final BitmapsProvider bitmapsProvider;
    private final ProjectsManager projectsManager;

    public CaptureBoardPresenter(
        CaptureBoardView view,
        ImageProcessor imageProcessor,
        BoardAnalyzer boardAnalyzer,
        ProjectsManager projectsManager,
        BitmapsProvider bitmapsProvider
    ) {
        super(view);
        this.imageProcessor = imageProcessor;
        this.boardAnalyzer = boardAnalyzer;
        this.projectsManager = projectsManager;
        this.bitmapsProvider = bitmapsProvider;
    }

    public void onImageReceived(final Uri photoUri, final int rotate) {
        projectsManager.getCurrentProject().subscribe(new Action1<UIProject>() {
            @Override
            public void call(UIProject uiProject) {
                processBoard(photoUri, rotate, uiProject);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                getView().showErrorOccurred();
                getView().hideLoading();
            }
        });
        getView().showLoading();
    }

    private void processBoard(Uri photoUri, int rotate, UIProject project) {
        Board board = new Board(project.getKey());
        try {
            board.setBitmap(bitmapsProvider.getImageByUri(photoUri));
            if (rotate != 0) {
                board.setBitmap(imageProcessor.rotateImage(board.getBitmap(), rotate));
            }
        } catch (Exception e) {
            getView().hideLoading();
            getView().showErrorOccurred();
            e.printStackTrace();
            return;
        }

        processBoard(board)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Board>() {
                @Override
                public void accept(Board board) throws Exception {
                    getView().showOutput(board);
                    getView().hideLoading();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    throwable.printStackTrace();
                    getView().hideLoading();
                }
            });
    }

    private Observable<Board> processBoard(final Board board) {
        return Observable.create(new ObservableOnSubscribe<Board>() {
            @Override
            public void subscribe(ObservableEmitter<Board> e) throws Exception {
                imageProcessor.autoAdjustImage(board);
                boardAnalyzer.analyzeProjectFromImage(board);
                e.onNext(board);
            }
        });
    }
}
