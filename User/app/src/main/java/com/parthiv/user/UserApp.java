package com.parthiv.user;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

public class UserApp extends Application {

    private static WeakReference<Context> context;

    public static Context getAppContext() {
        return context.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = new WeakReference<>(getApplicationContext());
    }
}
