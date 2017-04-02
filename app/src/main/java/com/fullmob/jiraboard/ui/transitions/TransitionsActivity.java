package com.fullmob.jiraboard.ui.transitions;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.ui.BaseActivity;
import com.fullmob.jiraboard.ui.models.UITransitionItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransitionsActivity extends BaseActivity implements TransitionsScreenView, TransitionsAdapter.Listener {

    public final static String EXTRA_ISSUE = "issue";

    @BindView(R.id.transitions) RecyclerView transitionsList;
    @BindView(R.id.issue_type_icon) ImageView issueTypeIcon;
    @BindView(R.id.issue_key) TextView issueKey;
    @BindView(R.id.no_transitions_available) TextView noTransitionsAvailable;
    @BindView(R.id.issue_status) TextView issueStatus;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Issue issue;
    private TransitionsAdapter adapter;

    @Inject TransitionsScreenPresenter presenter;
    @Inject SecuredImagesManagerInterface imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transitions);
        ButterKnife.bind(this);
        getApp().createTransitionsScreenComponent(this).inject(this);
        if (savedInstanceState != null) {
            issue = savedInstanceState.getParcelable(EXTRA_ISSUE);
        } else {
            issue = getIntent().getParcelableExtra(EXTRA_ISSUE);
        }
        initUI();
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        toolbar.setTitle(issue.getIssueFields().getSummary());
        renderIssue();
        transitionsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransitionsAdapter(new ArrayList<UITransitionItem>(), this);
        transitionsList.setAdapter(adapter);
    }

    private void renderIssue() {
        issueKey.setText(issue.getKey());
        imageLoader.loadSVG(issue.getIssueFields().getIssuetype().getIconUrl(), this, issueTypeIcon);
        adjustStatusField(issueStatus, issue.getIssueFields().getStatus().getStatusCategory().getColorName());
        issueStatus.setText(issue.getIssueFields().getStatus().getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onViewResumed(issue);
    }

    @Override
    public void onTransitionSelected(UITransitionItem issueStatus) {
        presenter.onTransitionRequested(issue, issueStatus);
    }

    @Override
    public void renderTransitions(List<UITransitionItem> transitionItemList) {
        adapter.setStatuses(transitionItemList);
        transitionsList.setVisibility(View.VISIBLE);
        noTransitionsAvailable.setVisibility(View.GONE);
    }

    @Override
    public void showNoTransitionsAvailableForCurrentState() {
        transitionsList.setVisibility(View.GONE);
        noTransitionsAvailable.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home/back button
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_ISSUE, issue);
    }
}
