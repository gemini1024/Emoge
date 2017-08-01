package com.emoge.app.emoge.ui.palette;

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
import com.emoge.app.emoge.ui.CameraActivity;
import com.emoge.app.emoge.ui.VideoActivity;
import com.emoge.app.emoge.ui.correction.CorrectImplAdapter;
import com.emoge.app.emoge.ui.correction.Correcter;
import com.emoge.app.emoge.ui.frame.FrameAddTask;
import com.emoge.app.emoge.ui.frame.FrameAdder;
import com.emoge.app.emoge.ui.view.MenuButtons;
import com.emoge.app.emoge.utils.Dialogs;
import com.emoge.app.emoge.utils.GifSaveTask;
import com.github.chrisbanes.photoview.PhotoView;
import com.nightonke.boommenu.BoomMenuButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Gif 생성을 위한 작업 창
 */

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar)             Toolbar mToolbar;
    @BindView(R.id.toolbar_share)       ImageButton mShareButton;
    @BindView(R.id.toolbar_save)        ImageButton mSaveButton;
    @BindView(R.id.main_preview)        PhotoView mPreview;
    @BindView(R.id.main_frame_list)     RecyclerView mFrameRecyclerView;
    @BindView(R.id.main_bt_add_frame)   BoomMenuButton mFrameAddMenuButton;
    @BindView(R.id.main_bt_correction)  BoomMenuButton mCorrectSelectButton;


    // Frame 저장 및 수정용 Adapter
    private CorrectImplAdapter mFrameAdapter;



    // Preview
    private int mPreviewIndex;
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
            mHandler.postDelayed(mTask, mFrameAdapter.getFps());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mTask, mFrameAdapter.getFps());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mTask);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mHandler = new Handler();
        Correcter correcter = new Correcter(this);
        FrameAdder frameAdder = new FrameAdder(this);

        addPalette(correcter);
        setFrameList(frameAdder, correcter);
        enableMenuButtons(frameAdder, correcter);
    }

    private void addPalette(Correcter correcter) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.main_palette_container,
                PaletteFragment.newInstance(Correcter.MAIN_PALETTE, correcter.getCurrentFps()));
        fragmentTransaction.commit();
    }

    private void setFrameList(FrameAdder frameAdder, Correcter correcter) {
        mFrameAdapter = new CorrectImplAdapter(mFrameRecyclerView,new ArrayList<Frame>(), frameAdder, correcter);
        mFrameRecyclerView.setHasFixedSize(true);
        mFrameRecyclerView.setAdapter(mFrameAdapter);
        mFrameRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mFrameRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void enableMenuButtons(FrameAdder frameAdder, Correcter correcter) {
        mShareButton.setVisibility(View.VISIBLE);
        mSaveButton.setVisibility(View.VISIBLE);
        MenuButtons.buildAddButton(getResources(), mFrameAddMenuButton, frameAdder);
        MenuButtons.buildSelectButton(getResources(), mCorrectSelectButton, correcter);
    }


    // 보정 기능
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPaletteEvent(PaletteMessage message) {
        mHandler.removeCallbacks(mTask);
        switch (message.getType()) {
            case Correcter.MAIN_PALETTE :
                mFrameAdapter.setFps(message.getValue());
                break;
            case Correcter.CORRECT_BRIGHTNESS :
                mFrameAdapter.setBrightness(message.getValue());
                break;
            case Correcter.CORRECT_CONTRAST :
                mFrameAdapter.setContrast(message.getValue());
                break;
            case Correcter.CORRECT_GAMMA :
                mFrameAdapter.setGamma(message.getValue());
                break;
            case Correcter.CORRECT_REVERSE :
                mFrameAdapter.reverse();
                break;
            case Correcter.CORRECT_APPLY :
                mFrameAdapter.apply();
                break;
            case Correcter.CORRECT_RESET :
                mFrameAdapter.reset();
                break;
        }
        mPreview.setImageBitmap(mFrameAdapter.getItem(mPreviewIndex).getBitmap());
        mHandler.postDelayed(mTask, mFrameAdapter.getFps());
        mFrameAdapter.clearPreviousFrames();    // View 에서 띄우는 이미지를 변경했으므로 -> 제거
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            if( data != null) {
                if(requestCode == FrameAdder.INTENT_GET_VIDEO) {
                    startVideoActivity(data);
                    return;
                }
                new FrameAddTask(this, mFrameAdapter, requestCode).execute(data);
            } else {
                // show error or do nothing
                Log.e(LOG_TAG, getString(R.string.err_intent_return_null));
            }
        }
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
        new GifSaveTask(this, mFrameAdapter.getFrames()).execute(mFrameAdapter.getFps());
    }


    // 실수로 나가기 방지
    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Dialogs.showExitDialog(this);
        } else {
            super.onBackPressed();
        }
    }
}