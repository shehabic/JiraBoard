package com.fullmob.jiraboard.ui.transitions;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.managers.issues.IssuesManager;
import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.transitions.TransitionManager;
import com.fullmob.jiraboard.transitions.TransitionStep;
import com.fullmob.jiraboard.ui.AbstractPresenter;
import com.fullmob.jiraboard.ui.models.UIIssueTransition;
import com.fullmob.jiraboard.ui.models.UIIssueTransitionGroups;
import com.fullmob.jiraboard.ui.models.UITransitionItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shehabic on 26/03/2017.
 */
public class TransitionsScreenPresenter extends AbstractPresenter<TransitionsScreenView> {

    private final IssuesManager issuesManager;
    private final ProjectsManager projectsManager;
    private final TransitionManager transitionManager;

    public TransitionsScreenPresenter(
        TransitionsScreenView view,
        IssuesManager issuesManager,
        ProjectsManager projectsManager,
        TransitionManager transitionManager
    ) {
        super(view);
        this.transitionManager = transitionManager;
        this.issuesManager = issuesManager;
        this.projectsManager = projectsManager;
    }

    public void onViewResumed(Issue issue) {
        fetchPossibleTransitions(issue);
    }

    private void fetchPossibleTransitions(final Issue issue) {
        getView().showLoading();
        issuesManager.getPossibleTransitions(issue)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<UIIssueTransitionGroups>() {
                @Override
                public void accept(UIIssueTransitionGroups uiIssueTransitionGroups) throws Exception {
                    prepareAndRenderTransitions(uiIssueTransitionGroups, issue);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    getView().hideLoading();
                    getView().showError(throwable);
                }
            });

    }

    private void prepareAndRenderTransitions(UIIssueTransitionGroups transitionGroups, Issue issue) {
        if (transitionGroups.getDirectTransitions().size() == 0) {
            combineServerTransitionsAndLocalOnes(transitionGroups, issue);
        } else {
            finalizeTransitionsAndRender(transitionGroups);
        }
    }

    private void finalizeTransitionsAndRender(UIIssueTransitionGroups transitionGroups) {
        List<UITransitionItem> items = new ArrayList<>();
        if (transitionGroups.getDirectTransitions().size() > 0) {
            items.add(new UITransitionItem(UITransitionItem.TRANSITION_HEADER_DIRECT));
            for (UIIssueTransition uiIssueTransition : transitionGroups.getDirectTransitions()) {
                items.add(new UITransitionItem(UITransitionItem.TRANSITION_ITEM, uiIssueTransition));
            }
        }
        if (transitionGroups.getFurtherTransitions().size() > 0) {
            items.add(new UITransitionItem(UITransitionItem.TRANSITION_HEADER_FURTHER));
            for (UIIssueTransition uiIssueTransition : transitionGroups.getFurtherTransitions()) {
                items.add(new UITransitionItem(UITransitionItem.TRANSITION_ITEM, uiIssueTransition));
            }
        }
        getView().hideLoading();
        if (items.size() > 0) {
            getView().renderTransitions(items);
        } else {
            getView().showNoTransitionsAvailableForCurrentState();
        }
    }

    private void combineServerTransitionsAndLocalOnes(final UIIssueTransitionGroups transitionGroups, Issue issue) {
        issuesManager.fetchPossibleTransitionsFromServer(issue, issue.getIssueFields().getStatus())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<UIIssueTransitionGroups>() {
                @Override
                public void accept(UIIssueTransitionGroups uiIssueTransitionGroups) throws Exception {
                    finalizeTransitionsAndRender(uiIssueTransitionGroups);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    getView().showError(throwable);
                }
            });
    }

    public void onTransitionRequested(Issue issue, UITransitionItem issueStatus) {
        List<TransitionStep> steps = transitionManager.findShortestPath(issue, issueStatus);
        getView().showTransitionConfirmation(steps);
    }

    public void onConfirmTransition(List<TransitionStep> steps) {

    }
}
