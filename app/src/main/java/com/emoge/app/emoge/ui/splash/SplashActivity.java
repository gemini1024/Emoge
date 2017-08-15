package com.emoge.app.emoge.ui.splash;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.ui.gallery.GalleryActivity;
import com.emoge.app.emoge.utils.Logger;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

/**
 * 첫 시작화면.
 * 퍼미션체크
 */

public class SplashActivity extends AppCompatActivity implements PermissionListener {
    private final String LOG_TAG = SplashActivity.class.getSimpleName();
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private TedPermission mPermission;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        mPermission = new TedPermission(this).setPermissionListener(this)
                .setDeniedMessage(R.string.err_permission_denied)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                mPermission.check();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onPermissionGranted() {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,
                findViewById(R.id.splash_image), getString(R.string.splash_image));
        startActivity(new Intent(SplashActivity.this, GalleryActivity.class), options.toBundle());
    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        Logger.e(LOG_TAG, getString(R.string.err_permission_denied));
        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
