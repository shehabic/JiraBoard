package com.fullmob.jiraboard.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FullmobSchedulers {

    public static Scheduler getMainThread() {
        return AndroidSchedulers.mainThread();
    }
}
