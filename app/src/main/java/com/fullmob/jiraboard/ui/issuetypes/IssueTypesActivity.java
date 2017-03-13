package com.fullmob.jiraboard.ui.issuetypes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.ui.BaseActivity;
import com.fullmob.jiraboard.ui.home.HomeActivity;
import com.fullmob.jiraboard.ui.models.IssueTypesAndWorkflows;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IssueTypesActivity extends BaseActivity implements IssueTypesView, IssueTypesAdapter.Listener {

    private static final String EXTRA_ISSUE_TYPES_AND_WORKFLOWS = "issue_types_and_workflows";
    @BindView(R.id.issue_types_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.total_issue_types) TextView totalIssueTypes;
    @BindView(R.id.total_workflows) TextView totalWorkFlows;
    @BindView(R.id.nextBtn) View nextButton;
    @OnClick(R.id.nextBtn) void onClick() {
        presenter.onNextClicked();
    }

    @Inject IssueTypesScreenPresenter presenter;
    @Inject SecuredImagesManagerInterface securedImagesManagerInterface;

    private IssueTypesAdapter adapter;
    private AlertDialog alertDialog;
    private DialogViewHolder dialogVHolder;

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
        IssueTypesAndWorkflows issueTypesAndWorkflows = null;
        if (savedInstanceState != null) {
            issueTypesAndWorkflows = savedInstanceState.getParcelable(EXTRA_ISSUE_TYPES_AND_WORKFLOWS);
        }
        presenter.onViewCreated(issueTypesAndWorkflows);
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
    public void promptForIssueId(final UIProject uiProject, final UIIssueType issueType) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogVHolder = new DialogViewHolder();
        dialogVHolder.dialogView = inflater.inflate(R.layout.ticket_prompt_view, null);
        dialogVHolder.textView = (TextView) dialogVHolder.dialogView.findViewById(R.id.please_enter_ticket_id_for_discovery);
        String template = getString(R.string.enter_ticket_key_for_discover, issueType.getName(), uiProject.getName());
        Spanned sp;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sp = Html.fromHtml(template,Html.FROM_HTML_MODE_LEGACY);
        } else {
            sp = Html.fromHtml(template);
        }
        dialogVHolder.dialogErrorText = (TextView) dialogVHolder.dialogView.findViewById(R.id.dialog_error_text);
        dialogVHolder.loaderView = dialogVHolder.dialogView.findViewById(R.id.dialog_loader);
        dialogVHolder.textView.setText(sp);
        dialogVHolder.projectKeyText = (TextView) dialogVHolder.dialogView.findViewById(R.id.project_key);
        dialogVHolder.projectKeyText.setText((uiProject.getKey() + "-"));
        dialogVHolder.ticketKey = (EditText) dialogVHolder.dialogView.findViewById(R.id.ticket_key);
        dialogVHolder.discoverButton = (Button) dialogVHolder.dialogView.findViewById(R.id.discover_ticket);
        dialogVHolder.cancelButton = (Button) dialogVHolder.dialogView.findViewById(R.id.cancel);
        dialogVHolder.buttonsView = dialogVHolder.dialogView.findViewById(R.id.ticket_key_buttons);
        dialogVHolder.discoverButton.findViewById(R.id.discover_ticket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dialogVHolder.ticketKey.getText().toString().trim().isEmpty()) {
                    presenter.onDiscoveryTicketEntered(issueType, uiProject, dialogVHolder.ticketKey.getText().toString());
                }
            }
        });
        clearDialogError();
        dialogVHolder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        dialogVHolder.loaderView.setVisibility(View.GONE);
        dialogBuilder.setView(dialogVHolder.dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    @Override
    public void clearDialogError() {
        dialogVHolder.dialogErrorText.setText("");
        dialogVHolder.dialogErrorText.setVisibility(View.GONE);
    }

    @Override
    public void proceedToHomeScreen() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void showDialogError(String message) {
        dialogVHolder.dialogErrorText.setText(("!" + message));
        dialogVHolder.dialogErrorText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInvalidIssue() {
        showDialogError(getString(R.string.invalid_issue_key));
    }

    @Override
    public void showInvalidIssueType(String expectedType, String foundType) {
        String template = "!" + getString(R.string.invalid_issue_type, expectedType, foundType);
        Spanned sp;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sp = Html.fromHtml(template,Html.FROM_HTML_MODE_LEGACY);
        } else {
            sp = Html.fromHtml(template);
        }
        dialogVHolder.dialogErrorText.setText(sp);
        dialogVHolder.dialogErrorText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSuccess(final Issue issue, final UIIssueType issueType, final UIProject uiProject) {
        alertDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.workflow_discovery);
        String template = getString(
            R.string.discovery_ticket_confirmation,
            issue.getKey() + ": " + issue.getIssueFields().getSummary(),
            uiProject.getName()
        );
        Spanned sp;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sp = Html.fromHtml(template,Html.FROM_HTML_MODE_LEGACY);
        } else {
            sp = Html.fromHtml(template);
        }
        builder.setMessage(sp);
        builder.setPositiveButton(R.string.discover, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.onDiscoveryConfirmed(issue, issueType, uiProject);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.show();
    }

    @Override
    public void showDialogLoading() {
        dialogVHolder.loaderView.setVisibility(View.VISIBLE);
        dialogVHolder.buttonsView.setVisibility(View.GONE);
    }

    @Override
    public void hideDialogLoading() {
        dialogVHolder.loaderView.setVisibility(View.GONE);
        dialogVHolder.buttonsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDiscoverTicketClicked(UIIssueType issueType) {
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

    @Override
    public void onBackPressed() {
        if (dialogVHolder != null && alertDialog != null && alertDialog.isShowing()) {
            if (dialogVHolder.loaderView.getVisibility() != View.VISIBLE) {
                alertDialog.dismiss();
                alertDialog.cancel();
                alertDialog.hide();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_ISSUE_TYPES_AND_WORKFLOWS, presenter.getIssueTypesAndWorkflows());
    }

    static class DialogViewHolder {
        View dialogView;
        TextView textView;
        TextView projectKeyText;
        EditText ticketKey;
        Button discoverButton;
        Button cancelButton;
        View buttonsView;
        View loaderView;
        TextView dialogErrorText;
    }
}
