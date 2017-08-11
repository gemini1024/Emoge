package com.emoge.app.emoge;

import android.app.Application;
import android.os.Environment;

import com.daimajia.androidviewhover.BlurLayout;
import com.squareup.leakcanary.LeakCanary;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        Realm.init(this);
        BlurLayout.setGlobalDefaultDuration(450);
    }
}
