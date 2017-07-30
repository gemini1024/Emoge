package com.emoge.app.emoge.ui;

import android.content.Intent;
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

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.ui.boombutton.FrameAddButton;
import com.emoge.app.emoge.ui.boombutton.ImageCorrectionButton;
import com.emoge.app.emoge.ui.frame.FrameAdapter;
import com.emoge.app.emoge.ui.frame.FrameAdder;
import com.emoge.app.emoge.utils.GifSaver;
import com.github.chrisbanes.photoview.PhotoView;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
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

    private FrameAdder mFrameAdder;
    private FrameAdapter mFrameAdapter;

    private Timer mPreviewTimer;
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
        mFrameAdder = new FrameAdder(this);
        mFrameAdapter = new FrameAdapter(mFrameRecyclerView, new ArrayList<Frame>());
        mFrameRecyclerView.setHasFixedSize(true);
        mFrameRecyclerView.setAdapter(mFrameAdapter);
        mFrameRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mFrameRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Enable Buttons
        mShareButton.setVisibility(View.VISIBLE);
        mSaveButton.setVisibility(View.VISIBLE);
        new FrameAddButton().buildAddButton(this, mFrameAddMenuButton);
        new ImageCorrectionButton().buildSelectButton(this, mCorrectSelectButton);
    }


    @Override
    protected void onResume() {
        super.onResume();
        TimerTask previewTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mFrameAdapter.getItemCount() > 0) {
                            mPreview.setImageBitmap(mFrameAdapter.getItem(mPreviewIndex).getBitmap());
                            mPreviewIndex = (mPreviewIndex + 1) % mFrameAdapter.getItemCount();
                        }
                    }
                });
            }
        };
        mPreviewTimer = new Timer();
        mPreviewTimer.schedule(previewTask, mFps, mFps);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreviewTimer.cancel();
        mPreviewTimer = null;
    }


    // 저장 기능
    @OnClick(R.id.toolbar_save)
    void makeToGifByImages(View view) {
        // make gif
        new GifSaver(MainActivity.this).execute(mFrameAdapter.getFrames());
    }


    // 공유 기능
    @OnClick(R.id.toolbar_share)
    void onShareButton() {
    }


    // TODO : 보정 연습 용. 보정 완성 후 제거
    @OnClick(R.id.main_bt_camera)
    void callCameraActivity() {
        Intent intent = new Intent(getBaseContext(), CameraActivity.class);
        startActivity(intent);
    }

    private void startVideoActivity(@NonNull Intent videoData) {
        if(videoData.getData() != null) {
            Intent videoActivityIntent = new Intent(this, VideoActivity.class);
            videoActivityIntent.setData(videoData.getData());
            overridePendingTransition(0, android.R.anim.fade_in);
            startActivityForResult(videoActivityIntent, FrameAdder.INTENT_CAPTURE_VIDEO);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if( data != null) {
                switch (requestCode) {
                    case FrameAdder.INTENT_GET_IMAGE:
                        mFrameAdapter.addFrameFromImages(mFrameAdder, data);
                        break;
                    case FrameAdder.INTENT_GET_VIDEO :
                        startVideoActivity(data);
                        break;
                    case FrameAdder.INTENT_CAPTURE_VIDEO :
                        mFrameAdapter.addFrameFromVideo(mFrameAdder, data);
                        break;
                }
            } else {
                // show error or do nothing
                Log.e(LOG_TAG, getString(R.string.err_intent_return_null));
            }
        }
    }

}