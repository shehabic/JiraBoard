package com.fullmob.jiraboard.ui.issuetypes;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.ui.models.UIIssueType;

import java.util.List;

class IssueTypesAdapter extends RecyclerView.Adapter<IssueTypesAdapter.IssuesViewHolder> {
    private List<UIIssueType> issueTypes;
    private SecuredImagesManagerInterface secureImageLoader;
    private Listener listener;

    public IssueTypesAdapter(List<UIIssueType> issueTypes, Listener listener, SecuredImagesManagerInterface imageLoader) {
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
        try {
            secureImageLoader.loadSVG(holder.uiIssueType.getIconUrl(), holder.imageView.getContext(), holder.imageView);
        } catch (Exception e) {

        }
        holder.bindEvents(listener);
    }

    @Override
    public int getItemCount() {
        return issueTypes.size();
    }

    public void setIssueTypes(List<UIIssueType> issueTypes) {
        this.issueTypes = issueTypes;
        notifyDataSetChanged();
    }

    public class IssuesViewHolder extends RecyclerView.ViewHolder {
        public AppCompatImageView actionView;
        public View itemView;
        public TextView itemName;
        public AppCompatImageView imageView;
        public UIIssueType uiIssueType;

        public IssuesViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.actionView = (AppCompatImageView) itemView.findViewById(R.id.action_explore);
            this.imageView = (AppCompatImageView) itemView.findViewById(R.id.issue_type_icon);
            this.itemName = (TextView) itemView.findViewById(R.id.issue_type);
        }

        public void bindEvents(final Listener listener) {
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(uiIssueType);
                }
            });
        }
    }

    public static interface Listener {
        void onClick(UIIssueType issueType);
    }
}
