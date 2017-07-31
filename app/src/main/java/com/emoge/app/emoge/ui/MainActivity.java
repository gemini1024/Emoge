package com.emoge.app.emoge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.model.PaletteMessage;
import com.emoge.app.emoge.ui.boombutton.FrameAddButton;
import com.emoge.app.emoge.ui.boombutton.ImageCorrectionButton;
import com.emoge.app.emoge.ui.correction.Corrections;
import com.emoge.app.emoge.ui.frame.FrameAdapter;
import com.emoge.app.emoge.ui.frame.FrameAdder;
import com.emoge.app.emoge.ui.palette.PaletteFragment;
import com.emoge.app.emoge.utils.Dialogs;
import com.emoge.app.emoge.utils.GifSaver;
import com.github.chrisbanes.photoview.PhotoView;
import com.nightonke.boommenu.BoomMenuButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

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

    private Corrections mCorrections;
    private FrameAdder mFrameAdder;
    private FrameAdapter mFrameAdapter;

    // Preview
    private int mPreviewIndex;
    private int mFps;
    private Handler mHandler;
    private final Runnable mTask = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mFrameAdapter.getItemCount() > 0) {
                        mPreview.setImageBitmap(mFrameAdapter.getItem(mPreviewIndex).getBitmap());
                        mPreviewIndex = (mPreviewIndex + 1) % mFrameAdapter.getItemCount();
                    } else {
                        Glide.with(getBaseContext()).load(R.drawable.img_no_image).into(mPreview);
                    }
                }
            });
            mHandler.postDelayed(mTask, mFps);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        // Preview
        mHandler = new Handler();
        mFps = DEFAULT_FPS;

        // Set mFrameRecyclerView
        mFrameAdder = new FrameAdder(this);
        mFrameAdapter = new FrameAdapter(mFrameRecyclerView, new ArrayList<Frame>());
        mFrameRecyclerView.setHasFixedSize(true);
        mFrameRecyclerView.setAdapter(mFrameAdapter);
        mFrameRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mFrameRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Add Palette
        mCorrections = new Corrections(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.main_palette_container, PaletteFragment.newInstance(Corrections.MAIN_PALETTE));
        fragmentTransaction.commit();

        // Enable Buttons
        mShareButton.setVisibility(View.VISIBLE);
        mSaveButton.setVisibility(View.VISIBLE);
        new FrameAddButton().buildAddButton(getResources(), mFrameAddMenuButton, mFrameAdder);
        new ImageCorrectionButton().buildSelectButton(getResources(), mCorrectSelectButton, mCorrections);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mTask, mFps);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mTask);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    // 공유 기능
    @OnClick(R.id.toolbar_share)
    void onShareButton() {
        // TODO : 보정 연습 용. 보정 완성 후 제거
        Intent intent = new Intent(getBaseContext(), CameraActivity.class);
        startActivity(intent);
    }


    // 저장 기능
    @OnClick(R.id.toolbar_save)
    void makeToGifByImages() {
        new GifSaver(this, mFrameAdapter.getFrames()).execute(mFps);
    }


    // 보정 기능
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPaletteEvent(PaletteMessage message) {
        switch (message.getType()) {
            case Corrections.MAIN_PALETTE :
                mFps = message.getValue();
                break;
            case Corrections.CORRECT_BRIGHTNESS :
                break;
            case Corrections.CORRECT_CONTRAST :
                break;
            case Corrections.CORRECT_GAMMA :
                break;
        }
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
                SweetAlertDialog loadingDialog;

                switch (requestCode) {
                    case FrameAdder.INTENT_GET_IMAGE:
                        mFrameAdapter.addFrameFromImages(mFrameAdder, data);
                        break;
                    case FrameAdder.INTENT_GET_GIF:
                        loadingDialog = Dialogs.showLoadingProgressDialog(this, R.string.adding_frames);
                        mFrameAdapter.addFrameFromGif(mFrameAdder, data);
                        loadingDialog.dismissWithAnimation();
                        break;
                    case FrameAdder.INTENT_GET_VIDEO :
                        startVideoActivity(data);
                        break;
                    case FrameAdder.INTENT_CAPTURE_VIDEO :
                        loadingDialog = Dialogs.showLoadingProgressDialog(this, R.string.adding_frames);
                        mFrameAdapter.addFrameFromVideo(mFrameAdder, data);
                        loadingDialog.dismissWithAnimation();
                        break;
                }
            } else {
                // show error or do nothing
                Log.e(LOG_TAG, getString(R.string.err_intent_return_null));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Dialogs.showExitDialog(this);
        } else {
            super.onBackPressed();
        }
    }
}