package com.fullmob.jiraboard.ui.transitions;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.ui.models.UITransitionItem;

import java.util.List;

class TransitionsAdapter extends RecyclerView.Adapter<TransitionsAdapter.TransitionsViewHolder> {
    private List<UITransitionItem> transitionItems;
    private TransitionsAdapter.Listener listener;

    TransitionsAdapter(List<UITransitionItem> issues, TransitionsAdapter.Listener listener) {
        this.transitionItems = issues;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(TransitionsViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return transitionItems.get(position).type;
    }

    @Override
    public TransitionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == UITransitionItem.TRANSITION_ITEM) {
            return new TransitionsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transition_item, parent, false)
            );
        } else {
            return new TransitionsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transition_header, parent, false)
            );
        }
    }

    @Override
    public void onBindViewHolder(TransitionsViewHolder holder, int position) {
        holder.transitionItem = transitionItems.get(position);
        holder.bindItems();
    }

    private void adjustStatusColor(String colorName, TransitionsViewHolder holder) {
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
        return transitionItems.size();
    }

    void setStatuses(List<UITransitionItem> transitionItems) {
        this.transitionItems = transitionItems;
        notifyDataSetChanged();
    }

    class TransitionsViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView itemName;
        View itemStatusColor;
        UITransitionItem transitionItem;

        TransitionsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemName = (TextView) itemView.findViewById(R.id.title);
            this.itemStatusColor = itemView.findViewById(R.id.issue_status_color);
        }

        void bindEvents(final TransitionsAdapter.Listener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTransitionSelected(transitionItem);
                }
            });
        }

        public void bindItems() {
            if (transitionItem.type == UITransitionItem.TRANSITION_HEADER_DIRECT) {
                itemName.setText(itemName.getContext().getString(R.string.direct_transitions));
            } else if (transitionItem.type == UITransitionItem.TRANSITION_HEADER_FURTHER) {
                itemName.setText(itemName.getContext().getString(R.string.further_transitions));
            } else if (transitionItem.type == UITransitionItem.TRANSITION_ITEM) {
                itemName.setText(transitionItem.transition.toName);
                adjustStatusColor(transitionItem.transition.toColor, this);
                bindEvents(listener);
            }
        }
    }

    public interface Listener {
        void onTransitionSelected(UITransitionItem issueStatus);
    }
}