package com.fullmob.jiraboard.ui.home.captureboard;

import android.net.Uri;
import android.util.Log;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.responses.SearchResults;
import com.fullmob.jiraboard.BuildConfig;
import com.fullmob.jiraboard.analyzer.BoardAnalyzer;
import com.fullmob.jiraboard.analyzer.BoardResult;
import com.fullmob.jiraboard.analyzer.BoardStatusItem;
import com.fullmob.jiraboard.data.Board;
import com.fullmob.jiraboard.data.Column;
import com.fullmob.jiraboard.data.Ticket;
import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.managers.serializers.SerializerInterface;
import com.fullmob.jiraboard.processors.ImageProcessor;
import com.fullmob.jiraboard.providers.BitmapsProvider;
import com.fullmob.jiraboard.transitions.TransitionManager;
import com.fullmob.jiraboard.transitions.TransitionSteps;
import com.fullmob.jiraboard.ui.AbstractPresenter;
import com.fullmob.jiraboard.ui.models.CapturedBoardListItem;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;
import com.fullmob.jiraboard.ui.models.UIProject;
import com.fullmob.jiraboard.ui.transitions.OnConfirmTransitionCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import rx.functions.Action1;

public class CaptureBoardPresenter extends AbstractPresenter<CaptureBoardView> implements OnConfirmTransitionCallback {

    private final ImageProcessor imageProcessor;
    private final BoardAnalyzer boardAnalyzer;
    private final BitmapsProvider bitmapsProvider;
    private final ProjectsManager projectsManager;
    private final IssuesManager issuesManager;
    private final SerializerInterface serializer;
    private boolean ticketsFetched;
    private BoardResult boardResult;
    private TransitionManager transitionManager;

    public CaptureBoardPresenter(
        CaptureBoardView view,
        ImageProcessor imageProcessor,
        BoardAnalyzer boardAnalyzer,
        ProjectsManager projectsManager,
        BitmapsProvider bitmapsProvider,
        IssuesManager issuesManager,
        SerializerInterface serializer,
        TransitionManager transitionManager
    ) {
        super(view);
        this.imageProcessor = imageProcessor;
        this.boardAnalyzer = boardAnalyzer;
        this.projectsManager = projectsManager;
        this.bitmapsProvider = bitmapsProvider;
        this.issuesManager = issuesManager;
        this.serializer = serializer;
        this.transitionManager = transitionManager;
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

    void processBoard(Uri photoUri, int rotate, UIProject project) {
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
        ticketsFetched = false;
        boardResult = null;
        processBoard(board)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Board>() {
                @Override
                public void accept(Board board) throws Exception {
                    if (board.isAnalysisCompleted()) {
                        getView().onAnalysisCompleted(board);
                        getView().hideLoading();
                    } else if (!ticketsFetched) {
                        boardResult = new BoardResult();
                        boardResult.columns.addAll(board.getColumns());
                        boardResult.tickets.addAll(board.getTickets());
                        prefetchStatuses(board.getColumns());
                        prefetchTickets(board.getTickets());
                        ticketsFetched = true;
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    throwable.printStackTrace();
                    getView().hideLoading();
                }
            });
    }

    void prefetchStatuses(final Collection<Column> columns) {
        projectsManager.findAllStatusesInCurrentProject()
            .subscribeOn(rx.schedulers.Schedulers.io())
            .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe(new Action1<HashSet<UIIssueStatus>>() {
                @Override
                public void call(HashSet<UIIssueStatus> uiIssueStatuses) {
                    HashMap<String, UIIssueStatus> nameToStatus = new HashMap<>();
                    for (UIIssueStatus status : uiIssueStatuses) {
                        nameToStatus.put(status.getName(), status);
                    }
                    for (Column column : columns) {
                        String colName = getColumnName(column);
                        UIIssueStatus status = nameToStatus.get(colName);
                        if (status != null) {
                            boardResult.columnsToStatuses.put(colName, status);
                            boardResult.statuses.add(status);
                        }
                    }
                    if (boardResult.isComplete()) {
                        renderBoardResults();
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    getView().showErrorOccurred();
                }
            });
    }

    String getColumnName(Column column) {
        BoardStatusItem item = new BoardStatusItem("");
        if (column.text != null) {
            if (column.text.startsWith("{")) {
                item = serializer.deSerialize(column.text, BoardStatusItem.class);
            } else {
                item = new BoardStatusItem(column.text);
            }
        }

        return item.name;
    }

    void prefetchTickets(final List<Ticket> tickets) {
        issuesManager.findTickets(tickets)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Response<SearchResults>>() {
                @Override
                public void accept(Response<SearchResults> searchResultsResponse) throws Exception {
                    boardResult.issues.addAll(searchResultsResponse.body().getIssues());
                    for (Issue issue : boardResult.issues) {
                        boardResult.keysToIssues.put(issue.getKey(), issue);
                    }
                    if (boardResult.isComplete()) {
                        renderBoardResults();
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    getView().showErrorOccurred();
                }
            });
    }

    void renderBoardResults() {
        for (Column col : boardResult.columns) {
            UIIssueStatus status = boardResult.columnsToStatuses.get(getColumnName(col));
            boardResult.statusesAndIssues.put(status, new ArrayList<Issue>());
            for (Ticket ticket : col.tickets) {
                Issue issue = boardResult.keysToIssues.get(ticket.text);
                if (issue != null) {
                    boardResult.statusesAndIssues.get(status).add(issue);
                }
            }
        }
        List<CapturedBoardListItem> items = new ArrayList<>();
        for (Map.Entry<UIIssueStatus, List<Issue>> entry : boardResult.statusesAndIssues.entrySet()) {
            if (entry.getValue().size() > 0) {
                items.add(createBoardListColumnItem(entry));
                for (Issue issue : entry.getValue()) {
                    CapturedBoardListItem item = createBoardListIssueItem(issue, entry.getKey());
                    item.setColumn(entry.getKey());
                    items.add(item);
                }
            }
        }
        getView().renderAnalysisResult(items);
    }

    CapturedBoardListItem createBoardListIssueItem(Issue issue, UIIssueStatus boardStatus) {
        CapturedBoardListItem item = new CapturedBoardListItem();
        item.setType(CapturedBoardListItem.RowType.ISSUE);
        item.setStatusOnBoard(boardStatus);
        item.setIssue(issue);
        item.setInGoodStatus(issue.getIssueFields().getStatus().getName().equals(boardStatus.getName()));

        return item;
    }

    CapturedBoardListItem createBoardListColumnItem(Map.Entry<UIIssueStatus, List<Issue>> entry) {
        CapturedBoardListItem item = new CapturedBoardListItem();
        item.setType(CapturedBoardListItem.RowType.STATUS);
        item.setColumn(entry.getKey());

        return item;
    }

    Observable<Board> processBoard(final Board board) {
        return Observable.create(new ObservableOnSubscribe<Board>() {
            @Override
            public void subscribe(ObservableEmitter<Board> e) throws Exception {
                imageProcessor.autoAdjustImage(board);
                boardAnalyzer.analyzeProjectFromImage(board, e);
                board.setAnalysisCompleted();
                e.onNext(board);
            }
        });
    }

    void onFixItemTransitionRequested(CapturedBoardListItem item) {
        TransitionSteps steps = transitionManager.getTransitionSteps(item.getIssue(), item.getColumn().getName());
        onTransitionRequested(item.getIssue(), steps);
    }

    private void onTransitionRequested(Issue issue, TransitionSteps steps) {
        getView().showTransitionConfirmation(steps, issue, this);
    }

    @Override
    public void onConfirmTransition(TransitionSteps steps, Issue issue) {
        if (BuildConfig.DEBUG && !issue.getIssueFields().getSummary().startsWith("TST")) {
            getView().showToast("Can only move issues prefixed with TST in debug mode");
        } else {
            Log.d("TRANSITION", issue.getKey());
            transitionManager.scheduleTransitionJob(issue, steps);
        }
    }
}
