package com.fullmob.jiraboard.ui;

import android.support.v7.app.AppCompatActivity;

import com.fullmob.jiraboard.app.FullmobAppInterface;

public class BaseActivity extends AppCompatActivity {

    public FullmobAppInterface getApp() {
        return (FullmobAppInterface) getApplication();
    }
}
