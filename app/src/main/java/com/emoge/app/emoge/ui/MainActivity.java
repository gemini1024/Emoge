package com.emoge.app.emoge.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.ui.boombutton.FrameAddButton;
import com.emoge.app.emoge.ui.boombutton.ImageCorrectionButton;
import com.emoge.app.emoge.ui.frame.FrameAdapter;
import com.emoge.app.emoge.ui.frame.FrameAdder;
import com.emoge.app.emoge.utils.GifSaver;
import com.github.chrisbanes.photoview.PhotoView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private final int DEFAULT_FPS = 500;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_share) ImageButton mShareButton;
    @BindView(R.id.toolbar_save) ImageButton mSaveButton;
    @BindView(R.id.main_preview) PhotoView mPreview;
    @BindView(R.id.main_frame_list) RecyclerView mFrameRecyclerView;
    @BindView(R.id.main_bt_add_frame) BoomMenuButton mFrameAddMenuButton;
    @BindView(R.id.main_bt_correction) BoomMenuButton mCorrectSelectButton;

    private FrameAdapter mFrameAdapter;
    private List<Frame> mFrames;
    private Timer mTimer;
    private int mPreviewIndex;
    private int mFps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        // Preview
        mFps = DEFAULT_FPS;

        // Set mFrameRecyclerView
        mFrameAdapter = new FrameAdapter(this, mFrameRecyclerView, makeDummyDatas());
        mFrameRecyclerView.setHasFixedSize(true);
        mFrameRecyclerView.setAdapter(mFrameAdapter);
        mFrameRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mFrameRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFrames = mFrameAdapter.getFrames();

        // Enable Buttons
        mShareButton.setVisibility(View.VISIBLE);
        mSaveButton.setVisibility(View.VISIBLE);
        new FrameAddButton().buildAddButton(this, mFrameAddMenuButton);
        new ImageCorrectionButton().buildSelectButton(this, mCorrectSelectButton);
    }

    @NonNull
    private ArrayList<Frame> makeDummyDatas() {
        ArrayList<Frame> dummyDatas = new ArrayList<>();
        dummyDatas.add(new Frame(1,R.drawable.img_boost));
        dummyDatas.add(new Frame(2,R.drawable.img_square_sun));
        dummyDatas.add(new Frame(3,R.drawable.img_square_cloud));
        dummyDatas.add(new Frame(4,R.drawable.img_noodle));
        return dummyDatas;
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimerTask mTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(mFrames.get(mPreviewIndex).getImage()).into(mPreview);
                        mPreviewIndex = (mPreviewIndex+1) % mFrames.size();
                    }
                });
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, 0, mFps);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimer.cancel();
        mTimer = null;
    }


    // 저장 기능
    @OnClick(R.id.toolbar_save)
    void makeToGifByImages(View view) {
        PermissionListener storagePermissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // make gif
                new GifSaver(MainActivity.this).execute(mFrames);
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Log.e(LOG_TAG, getString(R.string.err_permission_denied));
            }
        };
        new TedPermission(this).setPermissionListener(storagePermissionListener)
                .setDeniedMessage(R.string.err_permission_denied)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }


    // 공유 기능
    @OnClick(R.id.toolbar_share)
    void onShareButton() {
    }


    // TODO : 보정 연습 용. 보정 완성 후 제거
    @OnClick(R.id.main_bt_camera)
    void callCameraActivity() {
        PermissionListener cameraPermissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(getBaseContext(), CameraActivity.class);
                startActivity(intent);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Log.e(LOG_TAG, getString(R.string.err_permission_denied));
            }
        };
        new TedPermission(this).setPermissionListener(cameraPermissionListener)
                .setDeniedMessage(R.string.err_permission_denied)
                .setPermissions(android.Manifest.permission.CAMERA)
                .check();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if( data != null) {
                Uri selectedUri = data.getData();
                // image
//                Glide.with(this).load(selectedUri).into(mImageView);
                switch (requestCode) {
                    case FrameAdder.INTENT_GET_VIDEO :
                        Intent videoActivityIntent = new Intent(this, VideoActivity.class);
                        videoActivityIntent.setData(selectedUri);
                        startActivity(videoActivityIntent);
                }
            } else {
                // show error or do nothing
                Log.e(LOG_TAG, getString(R.string.err_intent_return_null));
            }
        }
    }

}