package com.fullmob.jiraboard.ui.issue;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.models.issue.Component;
import com.fullmob.jiraapi.models.issue.FixVersion;
import com.fullmob.jiraapi.models.issue.IssueFields;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.ui.BaseActivity;

import org.sufficientlysecure.htmltextview.ClickableTableSpan;
import org.sufficientlysecure.htmltextview.DrawTableLinkSpan;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class IssueActivity extends BaseActivity implements IssueScreenView {

    public final static String EXTRA_ISSUE = "issue";

    private Issue issue;
    private BottomSheetBehavior bottomSheetBehavior;
    @BindView(R.id.issue_summary) TextView issueSummary;
    @BindView(R.id.issue_description) HtmlTextView issueDescription;
    @BindView(R.id.bottom_sheet) View bottomSheet;
    @BindView(R.id.section_issue_description) View sectionIssueDescription;
    @BindView(R.id.webview) WebView webView;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.issue_assignee) TextView assignee;
    @BindView(R.id.project) TextView project;
    @BindView(R.id.issue_key) TextView issueKey;
    @BindView(R.id.project_icon) ImageView projectIcon;
    @BindView(R.id.issue_type_icon2) ImageView issueTypeIcon2;
    @BindView(R.id.reporter_image) ImageView reporterImage;
    @BindView(R.id.issue_priority_icon) ImageView issuePriorityIcon;
    @BindView(R.id.assignee_image) CircleImageView assigneeImage;
    @BindView(R.id.issue_type_icon) ImageView issueTypeIcon;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar) AppBarLayout appBarLayout;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.issue_view) View issueView;
    @BindView(R.id.issue_labels) TextView issueLabels;
    @BindView(R.id.issue_components) TextView issueComponents;
    @BindView(R.id.issue_reporter) TextView issueReporter;
    @BindView(R.id.issue_priority) TextView issuePriority;
    @BindView(R.id.issue_fix_version) TextView issueFixVersion;
    @BindView(R.id.issue_created) TextView issueCreated;
    @BindView(R.id.issue_updated) TextView issueUpdated;
    @BindView(R.id.issue_status) TextView issueStatus;
    @BindView(R.id.issue_type)
    TextView issueType;

    private float maxSize = 0f;

    @OnClick(R.id.section_issue_status) void onIssueTransit() {
        presenter.onIssueTransitionRequired(issue);
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        printIssue(issue);
    }

    @OnClick(R.id.close_bottom_sheet)
    void closeBottomSheetBtn() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Inject
    IssueScreenPresenter presenter;
    @Inject
    SecuredImagesManagerInterface imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        ButterKnife.bind(this);
        getApp().createIssueScreenComponent(this).inject(this);
        if (savedInstanceState != null) {
            issue = savedInstanceState.getParcelable(EXTRA_ISSUE);
        } else {
            issue = getIntent().getParcelableExtra(EXTRA_ISSUE);
        }
    }

    private void initUI() {
        issueSummary.setText(issue.getIssueFields().getSummary());
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    fab.show();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            private float percent;
            private float scrollRate;
            private float textSize;
            private float minSize;
            private final float factor = 0.2f;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                scrollRate = 1f - Math.abs(verticalOffset / (float) appBarLayout.getTotalScrollRange());
                issueSummary.setAlpha(scrollRate);
                textSize = (minSize) + (scrollRate * (maxSize - minSize));
                if (maxSize > 0f) {
                    issueSummary.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                } else {
                    maxSize = issueSummary.getTextSize();
                    minSize = title.getTextSize();
                }
                Log.d("SCROLL_RATE", String.valueOf(scrollRate));
                if (scrollRate > factor) {
                    title.setAlpha(0f);
                } else {
                    percent = Math.abs((factor - scrollRate) / factor);
                    title.setAlpha(percent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUI();
        presenter.onViewResumed(issue);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_ISSUE, issue);
    }

    @Override
    public void renderIssue(Issue issue) {
        issueView.setVisibility(View.VISIBLE);
        IssueFields field = issue.getIssueFields();
        issueSummary.setText(issue.getIssueFields().getSummary());
        title.setText(issue.getIssueFields().getSummary());
        renderDescription(issue);
        issueKey.setText(issue.getKey());
        project.setText(field.getProject().getName());
        if (field.getAssignee() != null) {
            assignee.setText(field.getAssignee().getDisplayName());
            imageLoader.loadImage(field.getAssignee().getAvatarUrls().get48x48(), this, assigneeImage);
        } else {
            assignee.setText(getString(R.string.none));
        }
        imageLoader.loadSVG(field.getProject().getAvatarUrls().get48x48(), this, projectIcon);
        imageLoader.loadSVG(field.getIssuetype().getIconUrl(), this, issueTypeIcon);
        imageLoader.loadSVG(field.getIssuetype().getIconUrl(), this, issueTypeIcon2);
        imageLoader.loadImage(field.getReporter().getAvatarUrls().get48x48(), this, reporterImage);
        imageLoader.loadSVG(field.getPriority().getIconUrl(), this, issuePriorityIcon);
        issueType.setText(field.getIssuetype().getName());
        issueReporter.setText(field.getReporter().getDisplayName());
        issuePriority.setText(field.getPriority().getName());

        if (field.getLabels() != null) {
            issueLabels.setText("");
            for (String label : field.getLabels()) {
                String existingLabel = issueLabels.getText().toString().trim();
                if (existingLabel.length() > 0) {
                    existingLabel += ", ";
                }
                issueLabels.setText(existingLabel + "" + label);
            }
        } else {
            issueLabels.setText("--");
        }
        if (field.getComponents() != null) {
            issueComponents.setText("");
            for (Component component : field.getComponents()) {
                String existingComponents = issueComponents.getText().toString().trim();
                if (existingComponents.length() > 0) {
                    existingComponents += ", ";
                }
                issueComponents.setText(existingComponents + "" + component.getName());
            }
        } else {
            issueComponents.setText("--");
        }
        if (field.getFixVersions() != null) {
            issueFixVersion.setText("");
            for (FixVersion fixVersion : field.getFixVersions()) {
                String existingFixVersions = issueFixVersion.getText().toString().trim();
                if (existingFixVersions.length() > 0) {
                    existingFixVersions += ", ";
                }
                issueFixVersion.setText(existingFixVersions + "" + fixVersion.getName());
            }
        } else {
            issueFixVersion.setText("--");
        }
        issueCreated.setText(issue.getRenderedFields().getCreated());
        issueUpdated.setText(
            issue.getRenderedFields().getUpdated() != null
                ? issue.getRenderedFields().getUpdated()
                : issue.getRenderedFields().getCreated()
        );
        issueStatus.setText(field.getStatus().getName());
        adjustStatus(issueStatus, field.getStatus().getStatusCategory().getColorName());
    }

    private void renderDescription(Issue issue) {
        final String html = issue.getRenderedFields().getDescription()
            .replaceAll("</?(div|tbody)[^>]*>", "")
            .trim()
            .replace("\t", "");
        try {
            DrawTableLinkSpan drawTableLinkSpan = new DrawTableLinkSpan();
            drawTableLinkSpan.setTextSize(120f);
            drawTableLinkSpan.setTextColor(getResources().getColor(R.color.colorPrimary));
            drawTableLinkSpan.setTableLinkText("Tap to view details");
            issueDescription.setDrawTableLinkSpan(drawTableLinkSpan);
            issueDescription.setClickableTableSpan(new ClickableTableSpanImpl());
            issueDescription.setHtml(html, new HtmlHttpImageGetter(issueDescription));
        } catch (Exception e) {
            issueDescription.setText("Tap to view details");
            issueDescription.setTextColor(getResources().getColor(R.color.colorPrimary));
            sectionIssueDescription.setClickable(true);
            sectionIssueDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onTableClicked(html);
                }
            });
        }
    }

    private void adjustStatus(TextView textView, String colorName) {
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

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            issueDescription.setClickableTableSpan(null);
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void renderTable(String tableHtml) {

        tableHtml = tableHtml.replace("\n", "<br>");
        tableHtml = "<html><head><style>"
            + " th{ background:#DDD; padding: 3px; border:1px solid #999; }"
            + " table { border:1px solid #999; border:1px solid #999; border-collapse: collapse; }"
            + " td { padding: 3px; border:1px solid #999; } "
            + "</style></head><body>"
            + tableHtml
            + "</body></html>";
        webView.loadData(tableHtml, "Text/HTML", "UTF-8");
        fab.setVisibility(View.INVISIBLE);
        Observable.timer(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });

    }

    @Override
    public void hideIssueView() {
        issueView.setVisibility(View.GONE);
    }

    private class ClickableTableSpanImpl extends ClickableTableSpan {
        @Override
        public ClickableTableSpan newInstance() {
            return new ClickableTableSpanImpl();
        }

        @Override
        public void onClick(View widget) {
            presenter.onTableClicked(getTableHtml());
        }
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED
            || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING
            ) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            finish();
        }
    }
}
