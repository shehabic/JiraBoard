package com.fullmob.jiraboard.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class WorkflowDiscoveryService extends Service {
    private static final String START_DISCOVERY_ACTION = "START_DISCOVERY_ACTION";
    private static final String STOP_DISCOVERY_ACTION = "STOP_DISCOVERY_ACTION";



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startWorkflowDiscoveryService(Context context) {
        Intent intent = new Intent(context, WorkflowDiscoveryService.class);
        intent.setAction(START_DISCOVERY_ACTION);
        context.startService(intent);
    }

    // call only in case when you user login/loggedOut or changing country/location
    public static void stopWorkflowDiscoveryService(Context context) {
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
                    stopDiscover();
                    stopSelf();
                    break;
            }
        }

        return START_STICKY;
    }

    private void stopDiscover() {

    }

    private void startDiscovery() {

    }

    private void injectDependencies() {

    }
}
