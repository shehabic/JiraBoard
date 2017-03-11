package com.fullmob.jiraboard.ui.issuetypes;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.ui.models.UIIssueType;
import com.fullmob.jiraboard.ui.models.UIWorkflowQueueJob;

import java.util.List;

class IssueTypesAdapter extends RecyclerView.Adapter<IssueTypesAdapter.IssuesViewHolder> {
    private List<UIIssueType> issueTypes;
    private SecuredImagesManagerInterface secureImageLoader;
    private Listener listener;

    IssueTypesAdapter(List<UIIssueType> issueTypes, Listener listener, SecuredImagesManagerInterface imageLoader) {
        this.issueTypes = issueTypes;
        this.listener = listener;
        this.secureImageLoader = imageLoader;
    }

    @Override
    public IssuesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view
            = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_types_list_item, parent, false);
        return new IssuesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IssuesViewHolder holder, int position) {
        holder.uiIssueType = issueTypes.get(position);
        holder.itemName.setText(holder.uiIssueType.getName());
        holder.actionView.setVisibility(TextUtils.isEmpty(holder.uiIssueType.getStatus()) ? View.VISIBLE : View.GONE);
        String discoveryStatus = holder.uiIssueType.getDiscoveryStatus();
        boolean isBusy = discoveryStatus.equals(UIWorkflowQueueJob.STATUS_PROCESSING)
            || discoveryStatus.equals(UIWorkflowQueueJob.STATUS_PENDING);

        holder.actionView.setVisibility(isBusy ? View.GONE : View.VISIBLE);
        holder.actionIcon.setImageDrawable(
            (
                discoveryStatus.equals(UIWorkflowQueueJob.STATUS_PROCESSED)
                    ? holder.isDiscovered.getContext().getResources().getDrawable(R.drawable.ic_rediscover)
                    : holder.isDiscovered.getContext().getResources().getDrawable(R.drawable.ic_discover)
            )
        );

        if (discoveryStatus.equals(UIWorkflowQueueJob.STATUS_PROCESSED)) {
            holder.isDiscovered.setVisibility(View.VISIBLE);
            holder.isDiscovered.setImageDrawable(
                holder.isDiscovered.getContext().getResources().getDrawable(R.drawable.ic_discovered)
            );
        } else if (isBusy) {
            holder.isDiscovered.setVisibility(View.VISIBLE);
            holder.isDiscovered.setImageDrawable(
                holder.isDiscovered.getContext().getResources().getDrawable(R.drawable.ic_pending)
            );
        } else if (discoveryStatus.equals(UIWorkflowQueueJob.STATUS_FAILED)) {
            holder.isDiscovered.setVisibility(View.VISIBLE);
            holder.isDiscovered.setImageDrawable(
                holder.isDiscovered.getContext().getResources().getDrawable(R.drawable.ic_error)
            );
        } else {
            holder.isDiscovered.setVisibility(View.GONE);
        }
        try {
            secureImageLoader.loadSVG(
                holder.uiIssueType.getIconUrl(),
                holder.issueTypeIcon.getContext(),
                holder.issueTypeIcon
            );
        } catch (Exception ignore) {
        }
        holder.bindEvents(listener);
    }

    @Override
    public int getItemCount() {
        return issueTypes.size();
    }

    void setIssueTypes(List<UIIssueType> issueTypes) {
        this.issueTypes = issueTypes;
        notifyDataSetChanged();
    }

    class IssuesViewHolder extends RecyclerView.ViewHolder {
        View actionView;
        AppCompatImageView actionIcon;
        View itemView;
        TextView itemName;
        AppCompatImageView isDiscovered;
        AppCompatImageView issueTypeIcon;
        UIIssueType uiIssueType;

        IssuesViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.actionIcon = (AppCompatImageView) itemView.findViewById(R.id.icon_explore);
            this.actionView = itemView.findViewById(R.id.action_explore);
            this.issueTypeIcon = (AppCompatImageView) itemView.findViewById(R.id.issue_type_icon);
            this.isDiscovered = (AppCompatImageView) itemView.findViewById(R.id.issue_explored);
            this.itemName = (TextView) itemView.findViewById(R.id.issue_type);
        }

        void bindEvents(final Listener listener) {
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDiscoverTicketClicked(uiIssueType);
                }
            });
        }
    }

    public interface Listener {
        void onDiscoverTicketClicked(UIIssueType issueType);
    }
}
