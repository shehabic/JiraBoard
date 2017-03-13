package com.fullmob.jiraboard.ui.home.cameraboard;

import android.net.Uri;

import com.fullmob.jiraboard.analyzer.BoardAnalyzer;
import com.fullmob.jiraboard.data.Board;
import com.fullmob.jiraboard.managers.db.DBManagerInterface;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
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

public class CaptureBoardPresenter extends AbstractPresenter<CaptureBoardView> {

    private final ImageProcessor imageProcessor;
    private final BoardAnalyzer boardAnalyzer;
    private final DBManagerInterface dbManager;
    private final EncryptedStorage encryptedStorage;
    private final BitmapsProvider bitmapsProvider;

    public CaptureBoardPresenter(
        CaptureBoardView view,
        ImageProcessor imageProcessor,
        BoardAnalyzer boardAnalyzer,
        DBManagerInterface dbManager,
        EncryptedStorage encryptedStorage,
        BitmapsProvider bitmapsProvider
    ) {
        super(view);
        this.imageProcessor = imageProcessor;
        this.boardAnalyzer = boardAnalyzer;
        this.dbManager = dbManager;
        this.encryptedStorage = encryptedStorage;
        this.bitmapsProvider = bitmapsProvider;
    }

    public void onImageReceived(Uri photoUri, int rotate) {
        String projectId = encryptedStorage.getDefaultProject();
        UIProject project = dbManager.getProject(projectId);
        Board board = new Board(project.getKey());
        try {
            board.setBitmap(bitmapsProvider.getImageByUri(photoUri));
            if (rotate != 0) {
                board.setBitmap(imageProcessor.rotateImage(board.getBitmap(), rotate));
            }
        } catch (Exception e) {
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
        getView().showLoading();
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

    private void showOutput(Board board) {
        getView().showOutput(board);
    }
}
