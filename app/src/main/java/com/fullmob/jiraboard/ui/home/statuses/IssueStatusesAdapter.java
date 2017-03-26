package com.fullmob.jiraboard.ui.home.statuses;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;

import java.util.List;

class IssueStatusesAdapter extends RecyclerView.Adapter<IssueStatusesAdapter.SearchResultsViewHolder> {
    private List<UIIssueStatus> issues;
    private IssueStatusesAdapter.Listener listener;

    IssueStatusesAdapter(
        List<UIIssueStatus> issues,
        IssueStatusesAdapter.Listener listener
    ) {
        this.issues = issues;
        this.listener = listener;
    }

    @Override
    public IssueStatusesAdapter.SearchResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IssueStatusesAdapter.SearchResultsViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_status_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(IssueStatusesAdapter.SearchResultsViewHolder holder, int position) {
        holder.issueStatus = issues.get(position);
        holder.itemName.setText(holder.issueStatus.getName());
        String colorName = holder.issueStatus.getStatusCategory().getColorName();
        adjustStatusColor(colorName, holder);
        holder.bindEvents(listener);
    }

    private void adjustStatusColor(String colorName, SearchResultsViewHolder holder) {
        Drawable background;
        switch (colorName.toLowerCase()) {
            case "yellow":
                background = holder.itemView.getContext().getResources().getDrawable(R.drawable.circle_yellow);
                break;

            case "green":
                background = holder.itemView.getContext().getResources().getDrawable(R.drawable.circle_green);
                break;

            default:
                background = holder.itemView.getContext().getResources().getDrawable(R.drawable.circle_blue);
                break;
        }

        holder.itemStatusColor.setBackgroundDrawable(background);
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    void setStatuses(List<UIIssueStatus> issues) {
        this.issues = issues;
        notifyDataSetChanged();
    }

    class SearchResultsViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView itemName;
        View itemStatusColor;
        View printStatus;
        UIIssueStatus issueStatus;

        SearchResultsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemName = (TextView) itemView.findViewById(R.id.issue_name);
            this.printStatus = itemView.findViewById(R.id.print_status_icon);
            this.itemStatusColor = itemView.findViewById(R.id.issue_status_color);
        }

        void bindEvents(final IssueStatusesAdapter.Listener listener) {
            printStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPrintStatusClicked(issueStatus);
                }
            });
        }
    }

    public interface Listener {
        void onPrintStatusClicked(UIIssueStatus issueStatus);
    }
}