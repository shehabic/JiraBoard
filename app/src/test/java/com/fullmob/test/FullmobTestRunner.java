package com.fullmob.test;

import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.test.runner.AndroidJUnitRunner;

public class FullmobTestRunner extends AndroidJUnitRunner{
    @Override
    public void onCreate(Bundle arguments)
    {
        MultiDex.install(getTargetContext());
        super.onCreate(arguments);
    }
}
