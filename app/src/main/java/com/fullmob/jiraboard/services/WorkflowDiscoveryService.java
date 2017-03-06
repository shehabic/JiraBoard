package com.fullmob.jiraboard.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fullmob.jiraboard.app.FullmobAppInterface;
import com.fullmob.jiraboard.managers.workflow.WorkflowDiscoveryManager;

import javax.inject.Inject;

public class WorkflowDiscoveryService extends Service {

    private static final String START_DISCOVERY_ACTION = "START_DISCOVERY_ACTION";
    private static final String STOP_DISCOVERY_ACTION = "STOP_DISCOVERY_ACTION";

    @Inject WorkflowDiscoveryManager manager;

    public WorkflowDiscoveryService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, WorkflowDiscoveryService.class);
        intent.setAction(START_DISCOVERY_ACTION);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, WorkflowDiscoveryService.class);
        intent.setAction(STOP_DISCOVERY_ACTION);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        injectDependencies();
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case START_DISCOVERY_ACTION:
                    startDiscovery();
                    break;

                case STOP_DISCOVERY_ACTION:
                    stopDiscovery();
                    stopSelf();
                    break;
            }
        }

        return START_STICKY;
    }

    private void stopDiscovery() {

    }

    private void startDiscovery() {

    }

    private void injectDependencies() {
        getApp().createWorkflowDiscoveryComponent().inject(this);
    }

    public FullmobAppInterface getApp() {
        return (FullmobAppInterface) getApplication();
    }
}
