package com.fullmob.jiraboard.ui.home.captureboard;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.ui.models.CapturedBoardListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shehabic on 28/03/2017.
 */
class CapturedBoardListAdapter extends RecyclerView.Adapter<CapturedBoardListAdapter.CapturedBoardViewHolder> {

    private static final int ITEM_TYPE_ISSUE = 1;
    private static final int ITEM_TYPE_STATUS = 2;
    private final SecuredImagesManagerInterface imageLoader;
    private CapturedBoardCallback listener;
    private final List<CapturedBoardListItem> items = new ArrayList<>();

    CapturedBoardListAdapter(
        List<CapturedBoardListItem> items,
        SecuredImagesManagerInterface imageLoader,
        CapturedBoardCallback listener
    ) {
        this.items.addAll(items);
        this.imageLoader = imageLoader;
        this.listener = listener;
    }

    @Override
    public CapturedBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CapturedBoardViewHolder(
            viewType == ITEM_TYPE_ISSUE
                ? LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_result_issue, parent, false)
                : LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_result_status, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(CapturedBoardViewHolder holder, int position) {
        holder.item = items.get(position);
        if (holder.item.getType().equals(CapturedBoardListItem.RowType.ISSUE)) {
            bindIssue(holder);
        } else {
            bindStatus(holder);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setResults(List<CapturedBoardListItem> boardResult) {
        items.clear();
        items.addAll(boardResult);
        notifyDataSetChanged();
    }

    static class CapturedBoardViewHolder extends RecyclerView.ViewHolder {
        CapturedBoardListItem item;
        TextView title;
        ImageView icon;
        View fixStatusIcon;
        TextView status;
        View issueStatusColor;

        CapturedBoardViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            status = (TextView) itemView.findViewById(R.id.status);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            fixStatusIcon = itemView.findViewById(R.id.fix_issue_icon);
            issueStatusColor = itemView.findViewById(R.id.issue_status_color);
        }
    }

    private void bindIssue(final CapturedBoardViewHolder viewHolder) {
        Issue issue = viewHolder.item.getIssue();
        viewHolder.title.setText(issue.getKey() + ": " + issue.getIssueFields().getSummary());
        viewHolder.status.setText(issue.getIssueFields().getStatus().getName());
        viewHolder.fixStatusIcon.setVisibility(viewHolder.item.isInGoodStatus() ? View.GONE : View.VISIBLE);
        Drawable background;
        @ColorInt int color = viewHolder.itemView.getContext().getResources().getColor(R.color.white);
        switch (issue.getIssueFields().getStatus().getStatusCategory().getColorName().toLowerCase()) {
            case "green":
                background = viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.status_color_green);
                break;

            case "yellow":
                background = viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.status_color_yellow);
                color = viewHolder.itemView.getContext().getResources().getColor(R.color.black);
                break;

            default:
                background = viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.status_color_blue);
                break;
        }
        viewHolder.status.setBackgroundDrawable(background);
        viewHolder.status.setTextColor(color);
        imageLoader.loadSVG(
            issue.getIssueFields().getIssuetype().getIconUrl(),
            viewHolder.itemView.getContext(),
            viewHolder.icon
        );
        viewHolder.fixStatusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onFixStatusClicked(viewHolder.item);
                }
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.fix_issue_icon) {
                    if (listener != null) {
                        listener.onFixStatusClicked(viewHolder.item);
                    }
                } else {
                    listener.onIssueClicked(viewHolder.item.getIssue());
                }
            }
        });
    }

    private void bindStatus(CapturedBoardViewHolder viewHolder) {
        viewHolder.title.setText(viewHolder.item.getColumn().getName());
        Drawable background = viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.circle_blue);
        switch (viewHolder.item.getColumn().getStatusCategory().getColorName()) {
            case "green":
                background = viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.circle_green);
                break;

            case "yellow":
                background = viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.circle_yellow);
                break;
        }
        viewHolder.issueStatusColor.setBackground(background);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType().equals(CapturedBoardListItem.RowType.ISSUE)
            ? ITEM_TYPE_ISSUE
            : ITEM_TYPE_STATUS;
    }

    public interface CapturedBoardCallback {
        void onFixStatusClicked(CapturedBoardListItem item);
        void onIssueClicked(Issue issue);
    }
}
