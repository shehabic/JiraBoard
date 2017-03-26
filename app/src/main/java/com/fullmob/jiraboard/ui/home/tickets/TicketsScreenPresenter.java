package com.fullmob.jiraboard.ui.home.tickets;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.responses.SearchResults;
import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;
import com.fullmob.jiraboard.ui.AbstractPresenter;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import rx.functions.Action1;

public class TicketsScreenPresenter extends AbstractPresenter<TicketsScreenView> {

    private final ProjectsManager projectsManager;
    private final IssuesManager issuesManager;
    private final EncryptedStorage encryptedStorage;

    public TicketsScreenPresenter(
        TicketsScreenView view,
        ProjectsManager projectsManager,
        IssuesManager issuesManager,
        EncryptedStorage encryptedStorage
    ) {
        super(view);
        this.projectsManager = projectsManager;
        this.issuesManager = issuesManager;
        this.encryptedStorage = encryptedStorage;
    }

    public void onViewResumed() {
        getView().setLastSearchText(encryptedStorage.getLastSavedSearch());
    }

    public void onSearchRequested(final String searchText) {
        getView().showLoading();
        projectsManager.getProject(encryptedStorage.getDefaultProject())
            .subscribeOn(rx.schedulers.Schedulers.io())
            .subscribe(new Action1<UIProject>() {
                @Override
                public void call(UIProject uiProject) {
                    searchForTicket(uiProject.getKey(), searchText);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    getView().showSearchError(throwable);
                    getView().hideLoading();
                }
            });
        onSearchQueryChanged(searchText);
    }

    private void searchForTicket(String projectKey, String searchText) {
        issuesManager.search(projectKey, searchText)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Response<SearchResults>>() {
                @Override
                public void accept(Response<SearchResults> response) throws Exception {
                    List<Issue> issues = new ArrayList<>();
                    if (response.body() != null) {
                        issues.addAll(response.body().getIssues());
                    }
                    getView().renderResultTicket(issues);
                    getView().hideLoading();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    getView().showSearchError(throwable);
                    getView().hideLoading();
                }
            });
    }

    public void onsSearchResultItemClicked(Issue issue) {
        getView().openIssueItem(issue);
    }

    public void onSearchQueryChanged(String searchText) {
        encryptedStorage.saveLastSearch(searchText);
    }
}
