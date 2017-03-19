package com.fullmob.jiraboard.ui;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public void showLoading() {
        getBaseActivity().showLoading();
    }

    public void hideLoading() {
        getBaseActivity().hideLoading();
    }
}
