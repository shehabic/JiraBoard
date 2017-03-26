package com.fullmob.jiraboard.ui.issue;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.ui.AbstractPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

/**
 * Created by shehabic on 25/03/2017.
 */
public class IssueScreenPresenter extends AbstractPresenter<IssueScreenView> {

    private final IssuesManager issuesManager;

    public IssueScreenPresenter(
        IssueScreenView view,
        IssuesManager issuesManager
    ) {
        super(view);
        this.issuesManager = issuesManager;
    }

    public void onViewResumed(Issue issue) {
        if (issue.getRenderedFields() == null) {
            issuesManager.getIssueForRendering(issue.getKey())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<Issue>>() {
                    @Override
                    public void accept(Response<Issue> response) throws Exception {
                        getView().renderIssue(response.body());
                        getView().hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().hideLoading();
                        getView().onError(throwable);
                    }
                });
            getView().showLoading();
            getView().hideIssueView();
        } else {
            getView().renderIssue(issue);
            getView().hideLoading();
        }
    }

    public void onTableClicked(String tableHtml) {
        getView().renderTable(tableHtml);
    }
}
