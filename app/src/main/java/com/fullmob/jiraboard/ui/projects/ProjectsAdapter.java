package com.fullmob.jiraboard.ui.projects;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.ArrayList;
import java.util.List;

class ProjectsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UIProject> projects;

    public ProjectsAdapter() {
        this(new ArrayList<UIProject>());
    }

    public ProjectsAdapter(List<UIProject> projects) {
        this.projects = projects;
    }

    public void setProjects(List<UIProject> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new RecyclerView.ViewHolder() {
//            @Override
//            public String toString() {
//                return super.toString();
//            }
//        }; LayoutInflater.from(parent.getContext()).inflate(R.layout.project_list_item);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
