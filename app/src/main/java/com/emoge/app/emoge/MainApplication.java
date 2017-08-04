package com.emoge.app.emoge;

import android.app.Application;
import android.os.Environment;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by jh on 17. 7. 25.
 * leakCanary 사용.
 */

public class MainApplication extends Application {
    public static final String defaultDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/madeGif/";

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
