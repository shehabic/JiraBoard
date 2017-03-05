package com.fullmob.jiraboard.ui.projects;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.di.components.ProjectsScreenComponent;
import com.fullmob.jiraboard.ui.BaseActivity;
import com.fullmob.jiraboard.ui.home.HomeActivity;
import com.fullmob.jiraboard.ui.login.LoginActivity;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectsActivity extends BaseActivity implements ProjectsView {

    private ProjectsAdapter adapter;

    @BindView(R.id.projects_recycler_view) RecyclerView projectsRecyclerView;

    @Inject ProjectsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectsScreenComponent comp = getApp().createProjectsScreenComponent(this);
        comp.inject(this);
        setContentView(R.layout.activity_projects);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            adapter = new ProjectsAdapter();
        } else {
            adapter = new ProjectsAdapter();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
}
