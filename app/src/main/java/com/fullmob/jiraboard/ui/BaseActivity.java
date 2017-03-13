package com.fullmob.jiraboard.ui;

import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.app.FullmobAppInterface;

public class BaseActivity extends AppCompatActivity {

    public FullmobAppInterface getApp() {
        return (FullmobAppInterface) getApplication();
    }

    public void showLoading() {
        if (findViewById(R.id.loader_layout) != null) {
            findViewById(R.id.loader_layout).setVisibility(View.VISIBLE);
        }
    }

    public void hideLoading() {
        if (findViewById(R.id.loader_layout) != null) {
            findViewById(R.id.loader_layout).setVisibility(View.GONE);
        }
    }

    public boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
