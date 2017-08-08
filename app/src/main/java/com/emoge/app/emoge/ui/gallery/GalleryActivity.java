package com.emoge.app.emoge.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.emoge.app.emoge.MainApplication;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.ui.palette.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.gallery_container,
                GalleryFragment.newInstance(MainApplication.defaultDir, ImageFormatChecker.GIF_TYPE));
        fragmentTransaction.commit();
    }


    @OnClick(R.id.gallery_bt_making)
    void startMakingGif() {
        overridePendingTransition(0, android.R.anim.fade_in);
        startActivity(new Intent(this, MainActivity.class));
    }
}
