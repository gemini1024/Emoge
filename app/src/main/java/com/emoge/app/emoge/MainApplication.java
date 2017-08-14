package com.emoge.app.emoge;

import android.app.Application;
import android.os.Environment;

import io.realm.Realm;

/**
 * Created by jh on 17. 7. 25.
 * leakCanary 사용.
 */

public class MainApplication extends Application {
    public static final String defaultDir =
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/madeGif/";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
