package com.fullmob.jiraboard.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.app.FullmobAppInterface;
import com.fullmob.jiraboard.transitions.TransitionSteps;
import com.fullmob.jiraboard.ui.issue.IssueActivity;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;
import com.fullmob.jiraboard.ui.printing.PrintingActivity;
import com.fullmob.jiraboard.ui.transitions.OnConfirmTransitionCallback;
import com.fullmob.jiraboard.ui.transitions.TransitionListAdapter;

public class BaseActivity extends AppCompatActivity {

    public FullmobAppInterface getApp() {
        return (FullmobAppInterface) getApplication();
    }

    public void showLoading() {
        if (findViewById(R.id.loader_layout) != null) {
            findViewById(R.id.loader_layout).setVisibility(View.VISIBLE);
        }
    }

    public void hideLoading() {
        if (findViewById(R.id.loader_layout) != null) {
            findViewById(R.id.loader_layout).setVisibility(View.GONE);
        }
    }

    public boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void printStatus(UIIssueStatus issueStatus) {
        Intent i = new Intent(this, PrintingActivity.class);
        i.putExtra(PrintingActivity.EXTRA_PAYLOAD_TYPE, PrintingActivity.EXTRA_TYPE_ISSUE_STATUS);
        i.putExtra(PrintingActivity.EXTRA_PAYLOAD, issueStatus);
        i.setExtrasClassLoader(UIIssueStatus.class.getClassLoader());
        startActivity(i);
    }

    public void printIssue(Issue issue) {
        Intent i = new Intent(this, PrintingActivity.class);
        i.putExtra(PrintingActivity.EXTRA_PAYLOAD_TYPE, PrintingActivity.EXTRA_TYPE_ISSUE);
        i.putExtra(PrintingActivity.EXTRA_PAYLOAD, issue);
        i.setExtrasClassLoader(Issue.class.getClassLoader());
        startActivity(i);
    }

    public void openIssue(Issue issue) {
        Intent i = new Intent(this, IssueActivity.class);
        i.putExtra(IssueActivity.EXTRA_ISSUE, issue);
        i.setExtrasClassLoader(Issue.class.getClassLoader());
        startActivity(i);
    }

    public void showError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void adjustStatusField(TextView textView, String colorName) {
        Drawable background;
        @ColorInt int color = getResources().getColor(R.color.white);
        switch (colorName.toLowerCase()) {
            case "yellow":
                background = getResources().getDrawable(R.drawable.status_color_yellow);
                color = getResources().getColor(R.color.black);
                break;

            case "green":
                background = getResources().getDrawable(R.drawable.status_color_green);
                break;

            default:
                background = getResources().getDrawable(R.drawable.status_color_grey);
                break;
        }

        textView.setBackgroundDrawable(background);
        textView.setTextColor(color);
    }

    public void showTransitionConfirmation(
        final TransitionSteps steps,
        final Issue issue,
        final OnConfirmTransitionCallback callback
    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        TransitionListAdapter adapter = new TransitionListAdapter(steps, issue);
        View dialogView = inflater.inflate(R.layout.dialog_transition_confirmation, null);
        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.transitions_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        builder.setView(dialogView);
        final AlertDialog confirmationDialog = builder.show();
        dialogView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onConfirmTransition(steps, issue);
                confirmationDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.dismiss();
            }
        });


    }
}
