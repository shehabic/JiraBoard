package com.fullmob.jiraboard.ui.projects;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.ui.models.UIProject;

import java.util.ArrayList;
import java.util.List;

class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {

    private final SecuredImagesManagerInterface securedImagesManager;
    private List<UIProject> projects;
    private Listener listener;

    public ProjectsAdapter(Listener listener, SecuredImagesManagerInterface securedImagesManager) {
        this(new ArrayList<UIProject>(), listener, securedImagesManager);
    }

    public ProjectsAdapter(List<UIProject> projects, Listener listener, SecuredImagesManagerInterface securedImagesManager) {
        this.projects = projects;
        this.listener = listener;
        this.securedImagesManager = securedImagesManager;
    }

    public void setProjects(List<UIProject> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view =
            (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.project_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.projectTitle.setText(projects.get(position).getName());
        holder.uiProject = projects.get(position);
        securedImagesManager.loadSVG(holder.uiProject.getAvatarUrls().get48x48(), holder.view.getContext(), holder.projectImage);
        holder.bindEvents();
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout view;
        final TextView projectTitle;
        UIProject uiProject;
        AppCompatImageView projectImage;

        ViewHolder(LinearLayout v) {
            super(v);
            view = v;
            projectTitle = (TextView) v.findViewById(R.id.project_name);
            projectImage = (AppCompatImageView) v.findViewById(R.id.project_icon);
        }

        void bindEvents() {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(uiProject);
                    }
                }
            });
        }
    }

    public static interface Listener {
        void onClick(UIProject uiProject);
    }
}
