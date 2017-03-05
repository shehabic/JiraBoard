package com.fullmob.jiraboard.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.app.FullmobAppInterface;

public class BaseActivity extends AppCompatActivity {

    public FullmobAppInterface getApp() {
        return (FullmobAppInterface) getApplication();
    }

    public void showLoading() {
        findViewById(R.id.loader_layout).setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        findViewById(R.id.loader_layout).setVisibility(View.GONE);
    }
}
