package com.emoge.app.emoge.ui.splash;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.ui.MainActivity;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,
                            findViewById(R.id.splash_image), getString(R.string.splash_image));
                startActivity(new Intent(SplashActivity.this, MainActivity.class), options.toBundle());
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
