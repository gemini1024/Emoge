package com.emoge.app.emoge;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;

import com.bumptech.glide.Glide;

import io.realm.Realm;

/**
 * Created by jh on 17. 7. 25.
 * leakCanary 사용.
 */

public class MainApplication extends Application {
    public static final String defaultDir =
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/madeGif/";

    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("MAIN_PREF", MODE_PRIVATE);
        Realm.init(this);
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.with(this).onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.with(this).onTrimMemory(level);
    }
}
