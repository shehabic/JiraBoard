package com.fullmob.jiraboard.ui.issue;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullmob.jiraapi.models.Issue;
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
    @BindView(R.id.webview) WebView webView;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.issue_assignee) TextView assignee;
    @BindView(R.id.project) TextView project;
    @BindView(R.id.issue_key) TextView issueKey;
    @BindView(R.id.project_icon) ImageView projectIcon;
    @BindView(R.id.assignee_image) CircleImageView assigneeImage;
    @BindView(R.id.issue_type_icon) ImageView issueTypeIcon;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar) AppBarLayout appBarLayout;
    @BindView(R.id.title) TextView title;
    private float maxSize = 0f;

    @OnClick(R.id.fab) void onFabClick() {
        printIssue(issue);
    }
    @OnClick(R.id.close_bottom_sheet) void closeBottomSheetBtn() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Inject IssueScreenPresenter presenter;
    @Inject SecuredImagesManagerInterface imageLoader;

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
            private final float factor = 0.4f;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                scrollRate = 1f - Math.abs(verticalOffset / (float) appBarLayout.getTotalScrollRange());
                issueSummary.setAlpha(scrollRate);
                textSize = (factor * maxSize) + (scrollRate * factor * maxSize);
                if (maxSize > 0f) {
                    issueSummary.setTextSize(textSize);
                    Log.d("TEXT_SIZE_INITIAL", String.valueOf(textSize));
                } else {
                    maxSize = issueSummary.getTextSize();
                    Log.d("TEXT_SIZE_INITIAL", String.valueOf(maxSize));
                }
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
        IssueFields field = issue.getIssueFields();
        issueSummary.setText(issue.getIssueFields().getSummary());
        title.setText(issue.getIssueFields().getSummary());
        String html = issue.getRenderedFields().getDescription();
        html = html.replaceAll("</?(div|tbody)[^>]*>", "").trim().replace("\n", "");
        DrawTableLinkSpan drawTableLinkSpan = new DrawTableLinkSpan();
        drawTableLinkSpan.setTextSize(120f);
        drawTableLinkSpan.setTextColor(getResources().getColor(R.color.colorPrimary));
        drawTableLinkSpan.setTableLinkText("Tap to view the Table");
        issueDescription.setDrawTableLinkSpan(drawTableLinkSpan);
        issueDescription.setClickableTableSpan(new ClickableTableSpanImpl());
        issueDescription.setHtml(html, new HtmlHttpImageGetter(issueDescription));
        issueKey.setText(issue.getKey());
        project.setText(field.getProject().getName());
        assignee.setText(field.getAssignee().getName());
        imageLoader.loadImage(field.getAssignee().getAvatarUrls().get48x48(), this, assigneeImage);
        imageLoader.loadSVG(field.getProject().getAvatarUrls().get48x48(), this, projectIcon);
        imageLoader.loadSVG(field.getIssuetype().getIconUrl(), this, issueTypeIcon);
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
        if (bottomSheet.isShown()) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            finish();
        }
    }
}
