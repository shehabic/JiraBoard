package com.fullmob.jiraboard.ui.home.tickets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.ui.BaseFragment;


public class TicketsFragment extends BaseFragment {

    public static final String TAG = TicketsFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

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
        return inflater.inflate(R.layout.fragment_tickets, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onTicketsFragmentInteraction();
    }
}
