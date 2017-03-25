package com.fullmob.jiraboard.ui.home.tickets;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class SearchFragment extends BaseFragment implements TicketsScreenView, SearchResultsAdapter.Listener {

    public static final String TAG = SearchFragment.class.getSimpleName();
    private OnFragmentInteractionListener listener;
    private SearchResultsAdapter resultsAdapter;

    @BindView(R.id.search_view) MaterialSearchView searchView;
    @BindView(R.id.search_results) RecyclerView searchResultsRecyclerView;
    @BindView(R.id.error) TextView errorTextField;

    @Inject TicketsScreenPresenter presenter;
    @Inject SecuredImagesManagerInterface imagesLoader;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();

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
                presenter.onSearchQueryChanged(newText);
                return false;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {

        }
        getBaseActivity().getApp().createTicketsScreenComponent(this).inject(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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
        if (issues.size() > 0) {
            hideError();
            showSearchResults();
            resultsAdapter.setIssues(issues);
        } else {
            showError();
            hideSearchResults();
            errorTextField.setText(R.string.no_results_found);
        }
    }

    @Override
    public void showSearchError(Throwable throwable) {
        errorTextField.setText(getString(R.string.err_msg_unknown_error));
        showError();
        hideSearchResults();
    }

    @Override
    public void showSearchResults() {
        searchResultsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSearchResults() {
        searchResultsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        errorTextField.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        errorTextField.setVisibility(View.GONE);
    }

    @Override
    public void openIssueItem(Issue issue) {
        listener.onSearchItemClicked(issue);
    }

    @Override
    public void setLastSearchText(String lastSavedSearch) {
        searchView.setQuery(lastSavedSearch, false);
    }

    public void onSearchRequested(String searchQuery) {
        presenter.onSearchRequested(searchQuery);
    }

    @Override
    public void onDiscoverTicketClicked(Issue issue) {
        presenter.onsSearchResultItemClicked(issue);
    }

    public interface OnFragmentInteractionListener {
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
