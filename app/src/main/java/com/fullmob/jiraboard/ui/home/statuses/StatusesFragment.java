package com.fullmob.jiraboard.ui.home.statuses;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.ui.BaseFragment;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusesFragment extends BaseFragment implements StatusesView, IssueStatusesAdapter.Listener {

    public static String TAG = StatusesFragment.class.getSimpleName();
    private OnIssueStatusesInteractor listener;

    private IssueStatusesAdapter adapter;

    @BindView(R.id.issue_statuses)
    RecyclerView issueStatuses;

    @BindView(R.id.error)
    TextView errorMessage;

    @Inject StatusesScreenPresenter presenter;

    public StatusesFragment() {
    }

    public static StatusesFragment newInstance() {
        return new StatusesFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().getApp().createStatusesScreenComponent(this).inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statuses, container, false);
        ButterKnife.bind(this, view);
        adapter = new IssueStatusesAdapter(new ArrayList<UIIssueStatus>(), this);
        issueStatuses.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        issueStatuses.setHasFixedSize(true);
        issueStatuses.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIssueStatusesInteractor) {
            listener = (OnIssueStatusesInteractor) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewCreated();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void renderIssueStatuses(List<UIIssueStatus> uiIssueStatuses) {
        adapter.setStatuses(uiIssueStatuses);
    }

    @Override
    public void showErrorOccurred(Throwable throwable) {

    }

    @Override
    public void onPrintStatusClicked(UIIssueStatus issueStatus) {
        if (listener != null) {
            listener.onPrintStatusRequested(issueStatus);
        }
    }

    public interface OnIssueStatusesInteractor {
        void onPrintStatusRequested(UIIssueStatus issueStatus);
    }
}
