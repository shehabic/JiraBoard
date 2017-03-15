package com.fullmob.jiraboard.ui.home.tickets;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.managers.images.SecuredImagesManagerInterface;
import com.fullmob.jiraboard.ui.BaseFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TicketsFragment extends BaseFragment implements TicketsScreenView, SearchResultsAdapter.Listener {

    public static final String TAG = TicketsFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private SearchResultsAdapter resultsAdapter;

    @BindView(R.id.search_view) MaterialSearchView searchView;
    @BindView(R.id.search_results) RecyclerView searchResultsRecyclerView;

    @Inject TicketsScreenPresenter presenter;
    @Inject SecuredImagesManagerInterface imagesLoader;

    public TicketsFragment() {
    }

    public static TicketsFragment newInstance() {
        TicketsFragment fragment = new TicketsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);
        ButterKnife.bind(this, view);
        resultsAdapter = new SearchResultsAdapter(new ArrayList<Issue>(), this, imagesLoader);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        searchResultsRecyclerView.setHasFixedSize(true);
        searchResultsRecyclerView.setAdapter(resultsAdapter);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.onSearchRequested(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
        getBaseActivity().getApp().createTicketsScreenComponent(this).inject(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showLoading() {
        getBaseActivity().showLoading();
    }

    @Override
    public void hideLoading() {
        getBaseActivity().hideLoading();
    }

    @Override
    public void renderResultTicket(List<Issue> issues) {
        resultsAdapter.setIssues(issues);
    }

    @Override
    public void showSearchError(Throwable throwable) {

    }

    @Override
    public void openIssueItem(Issue issue) {

    }

    public void onSearchRequested(String searchQuery) {
        presenter.onSearchRequested(searchQuery);
    }

    @Override
    public void onDiscoverTicketClicked(Issue issue) {
        presenter.onsSearchResultItemClicked(issue);
    }

    public interface OnFragmentInteractionListener {
        void onTicketsFragmentInteraction();

        void onSearchItemClicked(Issue issue);
    }

    @Override
    public void onResume() {
        searchView.showSearch();
        super.onResume();
        try {
            ImageButton back
                = (ImageButton) searchView.findViewById(com.miguelcatalan.materialsearchview.R.id.action_up_btn);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            back.setImageDrawable(getBaseActivity().getResources().getDrawable(R.drawable.ic_search_icon));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
