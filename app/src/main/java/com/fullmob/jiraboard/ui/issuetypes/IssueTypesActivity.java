package com.fullmob.jiraboard.ui.issuetypes;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.ui.BaseActivity;
import com.fullmob.jiraboard.ui.models.IssueTypesAndWorkflows;
import com.fullmob.jiraboard.ui.models.UIIssueType;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IssueTypesActivity extends BaseActivity implements IssueTypesView, IssueTypesAdapter.Listener {

    @BindView(R.id.issue_types_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.total_issue_types) TextView totalIssueTypes;
    @BindView(R.id.total_workflows) TextView totalWorkFlows;

    @Inject IssueTypesScreenPresenter presenter;
    @Inject SecuredImagesManagerInterface securedImagesManagerInterface;

    private IssueTypesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_types);
        ButterKnife.bind(this);
        getApp().createIssueTypesComponent(this).inject(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new IssueTypesAdapter(new ArrayList<UIIssueType>(), this, securedImagesManagerInterface);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showErrorOccurred() {

    }

    @Override
    public void renderWorkflowAndTypes(IssueTypesAndWorkflows issueTypesAndWorkflows) {
        totalWorkFlows.setText(String.valueOf(issueTypesAndWorkflows.getWorkflows().size()));
        totalIssueTypes.setText(String.valueOf(issueTypesAndWorkflows.getIssueTypes().size()));
        adapter.setIssueTypes(issueTypesAndWorkflows.getIssueTypes());
    }

    @Override
    public void onClick(UIIssueType issueType) {
        presenter.onIssueTypeClicked(issueType);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onViewResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onViewResumed();
    }
}
