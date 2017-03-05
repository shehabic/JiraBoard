package com.fullmob.jiraboard;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.fullmob.jiraapi.HttpClientBuilder;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

public class DebugUtils {
    public DebugUtils() {
    }

    public static void addDebugInterceptors(HttpClientBuilder builder) {
    }

    public static void initDebugTools(Context appContext) {
        Stetho.initialize(
            Stetho.newInitializerBuilder(appContext)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(appContext))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(appContext).build())
                .build());

    }
}