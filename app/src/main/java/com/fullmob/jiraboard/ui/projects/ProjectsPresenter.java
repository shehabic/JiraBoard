package com.fullmob.jiraboard.ui.projects;

import com.fullmob.jiraboard.managers.projects.ProjectsManager;
import com.fullmob.jiraboard.ui.AbstractPresenter;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ProjectsPresenter extends AbstractPresenter<ProjectsView> {
    private final ProjectsManager projectsManager;

    public ProjectsPresenter(ProjectsView view, ProjectsManager projectsManager) {
        super(view);
        this.projectsManager = projectsManager;
    }

    public void onViewPaused() {

    }

    public void onViewResumed() {
        getView().showLoading();
        projectsManager.findAllJiraProjects()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<UIProject>>() {
                @Override
                public void call(List<UIProject> jiraProjects) {
                    getView().hideLoading();
                    if (jiraProjects.size() == 0) {
                        getView().showNoProjectsFoundError();
                    } else {
                        getView().renderProjects(jiraProjects);
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    getView().hideLoading();
                    throwable.printStackTrace();
                    getView().showErrorOccurred();
                }
            });
    }

    public void onProjectSelected(UIProject project) {
        projectsManager.saveDefaultProject(project);
        getView().proceedToIssuesType();
    }
}
