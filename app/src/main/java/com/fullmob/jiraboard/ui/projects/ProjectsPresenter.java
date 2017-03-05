package com.fullmob.jiraboard.ui.projects;

import com.fullmob.jiraboard.db.data.JiraProject;
import com.fullmob.jiraboard.managers.projects.ProjManager;
import com.fullmob.jiraboard.ui.AbstractPresenter;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ProjectsPresenter extends AbstractPresenter<ProjectsView> {
    private final ProjManager projManager;

    public ProjectsPresenter(ProjectsView view, ProjManager projManager) {
        super(view);
        this.projManager = projManager;
    }

    public void onViewPaused() {

    }

    public void onViewResumed() {
        projManager.findAllJiraProjects()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<UIProject>>() {
                @Override
                public void call(List<UIProject> jiraProjects) {
                    if (jiraProjects.size() == 0) {
                        getView().showNoProjectsFoundError();
                    } else {
                        getView().renderProjects(jiraProjects);
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    throwable.printStackTrace();
                    getView().showErrorOccurred();
                }
            });
    }

    public void onProjectSelected(JiraProject jiraProject) {
        projManager.saveDefaultProject(jiraProject);
        getView().proceedToMainScreen();
    }
}
