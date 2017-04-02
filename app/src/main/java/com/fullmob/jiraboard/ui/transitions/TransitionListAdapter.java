package com.fullmob.jiraboard.ui.transitions;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraapi.models.issue.Status;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.transitions.TransitionStep;
import com.fullmob.jiraboard.transitions.TransitionSteps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shehabic on 28/03/2017.
 */
public class TransitionListAdapter extends RecyclerView.Adapter<TransitionListAdapter.TransitionsViewHolder> {

    private static final int TRANSITION_ITEM_TYPE_ARROW = 1;
    private static final int TRANSITION_ITEM_TYPE_STATUS = 2;

    private final List<TransitionStep> steps = new ArrayList<>();

    public TransitionListAdapter(TransitionSteps steps, Issue issue) {
        Status status = issue.getIssueFields().getStatus();
        this.steps.add(
            new TransitionStep(
                "0",
                status.getName(),
                status.getId(),
                status.getName(),
                status.getStatusCategory().getColorName(),
                status.getStatusCategory().getColorName()
            )
        );
        this.steps.addAll(steps.getSteps());
    }

    @Override
    public TransitionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransitionsViewHolder(
            viewType == TRANSITION_ITEM_TYPE_STATUS
                ? LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transition_state, parent, false)
                : LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transition_arrow, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(TransitionsViewHolder holder, int position) {
        if (position % 2 == 0) {
            int realPosition = (position / 2);
            holder.transitionStep = steps.get(realPosition);
            holder.bind();
        }
    }

    @Override
    public int getItemCount() {
        return steps.size() + steps.size() - 1;
    }

    static class TransitionsViewHolder extends RecyclerView.ViewHolder {
        TransitionStep transitionStep;
        TextView itemText;

        TransitionsViewHolder(View itemView) {
            super(itemView);
            itemText = (TextView) itemView.findViewById(R.id.title);
        }

        void bind() {
            itemText.setText(transitionStep.toName);
            Drawable background;
            @ColorInt int color = itemText.getContext().getResources().getColor(R.color.white);
            if (itemText != null && transitionStep != null) {
                switch (transitionStep.toColor.toLowerCase()) {
                    case "green":
                        background = itemText.getContext().getResources().getDrawable(R.drawable.status_color_green);
                        break;

                    case "yellow":
                        background = itemText.getContext().getResources().getDrawable(R.drawable.status_color_yellow);
                        color = itemText.getContext().getResources().getColor(R.color.black);
                        break;

                    default:
                        background = itemText.getContext().getResources().getDrawable(R.drawable.status_color_blue);
                        break;
                }
                itemText.setBackgroundDrawable(background);
                itemText.setTextColor(color);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TRANSITION_ITEM_TYPE_STATUS : TRANSITION_ITEM_TYPE_ARROW;
    }
}
