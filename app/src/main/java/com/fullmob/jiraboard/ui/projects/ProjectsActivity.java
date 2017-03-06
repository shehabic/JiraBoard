package com.fullmob.jiraboard.ui.projects;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.di.components.ProjectsScreenComponent;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.ui.BaseActivity;
import com.fullmob.jiraboard.ui.home.HomeActivity;
import com.fullmob.jiraboard.ui.issuetypes.IssueTypesActivity;
import com.fullmob.jiraboard.ui.login.LoginActivity;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectsActivity extends BaseActivity implements ProjectsView, ProjectsAdapter.Listener {

    private ProjectsAdapter adapter;

    @BindView(R.id.projects_recycler_view) RecyclerView projectsRecyclerView;

    @Inject ProjectsPresenter presenter;

    @Inject SecuredImagesManagerInterface securedImagesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectsScreenComponent comp = getApp().createProjectsScreenComponent(this);
        comp.inject(this);
        setContentView(R.layout.activity_projects);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            adapter = new ProjectsAdapter(this, securedImagesManager);
        } else {
            adapter = new ProjectsAdapter(this, securedImagesManager);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        projectsRecyclerView.setHasFixedSize(true);
        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        projectsRecyclerView.setAdapter(adapter);
        presenter.onViewResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onViewPaused();
    }

    @Override
    public void renderProjects(List<UIProject> jiraProjects) {
        adapter.setProjects(jiraProjects);
    }

    @Override
    public void showNoProjectsFoundError() {

    }

    @Override
    public void showErrorOccurred() {

    }

    @Override
    public void goBackToLoginScreen() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void proceedToMainScreen() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void proceedToIssuesType() {
        startActivity(new Intent(this, IssueTypesActivity.class));
    }

    @Override
    public void onClick(UIProject uiProject) {
        presenter.onProjectSelected(uiProject);
    }
}
