package com.fullmob.jiraboard.ui.home.statuses;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.ui.animation.FlipAnimator;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;

import java.util.ArrayList;
import java.util.List;

class IssueStatusesAdapter extends RecyclerView.Adapter<IssueStatusesAdapter.SearchResultsViewHolder> {
    private List<UIIssueStatus> statuses;
    private IssueStatusesAdapter.Listener listener;
    private SparseBooleanArray selectedItems;
    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    IssueStatusesAdapter(
        List<UIIssueStatus> statuses,
        IssueStatusesAdapter.Listener listener
    ) {
        this.statuses = statuses;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    @Override
    public IssueStatusesAdapter.SearchResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IssueStatusesAdapter.SearchResultsViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_issue_status, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(IssueStatusesAdapter.SearchResultsViewHolder holder, int position) {
        holder.issueStatus = statuses.get(position);
        holder.position = position;
        holder.itemName.setText(holder.issueStatus.getName());
        holder.itemView.setActivated(selectedItems.get(position, false));
        String colorName = holder.issueStatus.getStatusCategory().getColorName();
        adjustStatusColor(colorName, holder);
        holder.bindEvents(listener);
        applyIconAnimation(holder, position);
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
        return statuses.size();
    }

    void setStatuses(List<UIIssueStatus> issues) {
        this.statuses = issues;
        notifyDataSetChanged();
    }

    class SearchResultsViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView itemName;
        View itemStatusColor;
        View printStatus;
        UIIssueStatus issueStatus;
        View iconContainer, iconChecked, iconNormal;
        int position;

        SearchResultsViewHolder(View view) {
            super(view);
            itemName = (TextView) itemView.findViewById(R.id.issue_name);
            printStatus = itemView.findViewById(R.id.print_status_icon);
            itemStatusColor = itemView.findViewById(R.id.issue_status_color);
            iconChecked = itemView.findViewById(R.id.icon_checked);
            iconNormal = itemView.findViewById(R.id.icon_front);
            iconContainer = itemView.findViewById(R.id.issue_status_icon);
        }

        void bindEvents(final IssueStatusesAdapter.Listener listener) {
            printStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPrintStatusClicked(issueStatus);
                }
            });
            iconContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onIconClicked(issueStatus, position);
                }
            });
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(issueStatus, position);
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

            return true;
        }
    }

    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<UIIssueStatus> getSelectedItems() {
        List<UIIssueStatus> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(statuses.get(selectedItems.keyAt(i)));
        }

        return items;
    }

    private void applyIconAnimation(SearchResultsViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconNormal.setAlpha(0f);
            resetIconYAxis(holder.iconChecked);
            holder.iconChecked.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(holder.itemView.getContext(), holder.iconChecked, holder.iconNormal, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconChecked.setAlpha(0f);
            resetIconYAxis(holder.iconNormal);
            holder.iconNormal.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(holder.itemView.getContext(), holder.iconChecked, holder.iconNormal, false);
                resetCurrentIndex();
            }
        }
    }

    public interface Listener {
        void onPrintStatusClicked(UIIssueStatus issueStatus);

        void onIconClicked(UIIssueStatus issueStatus, int position);

        void onRowLongClicked(UIIssueStatus issueStatus, int position);
    }
}