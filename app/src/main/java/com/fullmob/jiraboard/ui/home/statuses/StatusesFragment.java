package com.fullmob.jiraboard.ui.home.statuses;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.ui.BaseFragment;
import com.fullmob.jiraboard.ui.models.PrintableIssueStatuses;
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
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

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
        issueStatuses.setItemAnimator(new DefaultItemAnimator());
//        issueStatuses.addItemDecoration(new DividerItemDecoration(getBaseActivity(), LinearLayoutManager.VERTICAL));
        issueStatuses.setAdapter(adapter);
        actionModeCallback = new ActionModeCallback();

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

    @Override
    public void onIconClicked(UIIssueStatus issueStatus, int position) {
        if (actionMode == null) {
            actionMode = getBaseActivity().startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    @Override
    public void onRowLongClicked(UIIssueStatus issueStatus, int position) {
        enableActionMode(position);
    }

    public interface OnIssueStatusesInteractor {
        void onPrintStatusRequested(UIIssueStatus issueStatus);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = getBaseActivity().startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            getBaseActivity().setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_print:
                    // delete all the selected messages
                    printStatuses();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
            actionMode = null;
            issueStatuses.post(new Runnable() {
                @Override
                public void run() {
                    adapter.resetAnimationIndex();
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }

    private void printStatuses() {
        adapter.resetAnimationIndex();
        PrintableIssueStatuses printableIssueStatuses = new PrintableIssueStatuses();
        printableIssueStatuses.statuses.addAll(adapter.getSelectedItems());
        getBaseActivity().printStatuses(printableIssueStatuses);
    }
}
