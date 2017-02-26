package com.fullmob.jiraboard.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.fullmob.jiraboard.DebugUtils;

public class FullmobApp extends FullmobDiApp {
    @Override
    public void onCreate() {
        super.onCreate();
        initDI();
        DebugUtils.initDebugTools(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
