package com.fullmob.jiraboard.ui.home.tickets;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;

import java.util.List;

class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder> {
    private List<Issue> issues;
    private SecuredImagesManagerInterface secureImageLoader;
    private SearchResultsAdapter.Listener listener;

    SearchResultsAdapter(List<Issue> issues, SearchResultsAdapter.Listener listener, SecuredImagesManagerInterface imageLoader) {
        this.issues = issues;
        this.listener = listener;
        this.secureImageLoader = imageLoader;
    }

    @Override
    public SearchResultsAdapter.SearchResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchResultsAdapter.SearchResultsViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_search_result_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(SearchResultsAdapter.SearchResultsViewHolder holder, int position) {
        holder.issue = issues.get(position);
        holder.itemName.setText(holder.issue.getKey() + ": " + holder.issue.getIssueFields().getSummary());
        holder.itemStatusText.setText(holder.issue.getIssueFields().getStatus().getName());
        String colorName = holder.issue.getIssueFields().getStatus().getStatusCategory().getColorName();
        adjustStatusColor(colorName, holder);
        try {
            secureImageLoader.loadSVG(
                holder.issue.getIssueFields().getIssuetype().getIconUrl(),
                holder.issueTypeIcon.getContext(),
                holder.issueTypeIcon
            );
        } catch (Exception ignore) {
        }
        holder.bindEvents(listener);
    }

    private void adjustStatusColor(String colorName, SearchResultsViewHolder holder) {
        Drawable background;
        @ColorInt int color = holder.itemView.getContext().getResources().getColor(R.color.white);
        switch (colorName.toLowerCase()) {
            case "yellow":
                background = holder.itemView.getContext().getResources().getDrawable(R.drawable.status_color_yellow);
                color = holder.itemView.getContext().getResources().getColor(R.color.black);
                break;

            case "green":
                background = holder.itemView.getContext().getResources().getDrawable(R.drawable.status_color_green);
                break;

            default:
                background = holder.itemView.getContext().getResources().getDrawable(R.drawable.status_color_grey);
                break;
        }

        holder.itemStatusText.setBackgroundDrawable(background);
        holder.itemStatusText.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    void setIssues(List<Issue> issues) {
        this.issues = issues;
        notifyDataSetChanged();
    }

    class SearchResultsViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView itemName;
        TextView itemStatusText;
        AppCompatImageView issueTypeIcon;
        Issue issue;

        SearchResultsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.issueTypeIcon = (AppCompatImageView) itemView.findViewById(R.id.issue_type_icon);
            this.itemName = (TextView) itemView.findViewById(R.id.issue_name);
            this.itemStatusText = (TextView) itemView.findViewById(R.id.ticket_status);
        }

        void bindEvents(final SearchResultsAdapter.Listener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDiscoverTicketClicked(issue);
                }
            });
        }
    }

    public interface Listener {
        void onDiscoverTicketClicked(Issue issue);
    }
}