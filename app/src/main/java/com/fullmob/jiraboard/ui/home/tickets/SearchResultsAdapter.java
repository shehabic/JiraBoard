package com.fullmob.jiraboard.ui.home.tickets;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        LinearLayout view
            = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_search_result_item, parent, false);
        return new SearchResultsAdapter.SearchResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultsAdapter.SearchResultsViewHolder holder, int position) {
        holder.issue = issues.get(position);
        holder.itemName.setText(holder.issue.getKey() + ": " + holder.issue.getIssueFields().getSummary());
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
        AppCompatImageView issueTypeIcon;
        Issue issue;

        SearchResultsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.issueTypeIcon = (AppCompatImageView) itemView.findViewById(R.id.issue_type_icon);
            this.itemName = (TextView) itemView.findViewById(R.id.issue_name);
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