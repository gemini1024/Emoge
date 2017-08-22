package com.emoge.app.emoge.ui.frame;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.utils.Logger;
import com.emoge.app.emoge.utils.SeekBarNumberTransformers;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;
import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Video 에서 Frame 추출하기위한
 * 정보(캡처 시작 위치, 캡처 수, fps) 획득
 */

public class VideoActivity extends AppCompatActivity implements BetterVideoCallback {
    private static final String LOG_TAG = VideoView.class.getSimpleName();

    public static final String INTENT_NAME_START_SEC        = "startSec";
    public static final String INTENT_NAME_CAPTURE_COUNT    = "count";
    public static final String INTENT_NAME_CAPTURE_DELAY    = "delay";


    @BindView(R.id.video_video)
    BetterVideoPlayer mVideoView;
    @BindView(R.id.video_count)
    DiscreteSeekBar mCountBar;
    @BindView(R.id.video_fps)
    DiscreteSeekBar mFpsBar;

    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        ImageButton backButton = (ImageButton) findViewById(R.id.toolbar_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if( getIntent() != null && getIntent().getData() != null ) {
            videoUri = getIntent().getData();
            Logger.i(LOG_TAG, videoUri.toString());

            mVideoView.setCallback(this);
            mVideoView.setSource(videoUri);
            mVideoView.enableSwipeGestures();

            mFpsBar.setNumericTransformer(SeekBarNumberTransformers.Multiply(100));
            mFpsBar.setMin(1);
            mFpsBar.setMax(20);
            mFpsBar.setProgress(5);
        } else {
            Logger.e(LOG_TAG, getString(R.string.err_not_found_video_title));
            SweetDialogs.showErrorDialog(this,
                    R.string.err_not_found_video_title, R.string.err_not_found_video_text);
            finish();
        }
    }

    @OnClick(R.id.video_button)
    void onCaptureVideo() {
        Intent returnIntent = new Intent();
        returnIntent.setData(videoUri);
        returnIntent.putExtra(INTENT_NAME_START_SEC, mVideoView.getCurrentPosition());
        returnIntent.putExtra(INTENT_NAME_CAPTURE_COUNT, mCountBar.getProgress());
        returnIntent.putExtra(INTENT_NAME_CAPTURE_DELAY, mFpsBar.getProgress()*100);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    public void onStarted(BetterVideoPlayer player) {

    }

    @Override
    public void onPaused(BetterVideoPlayer player) {

    }

    @Override
    public void onPreparing(BetterVideoPlayer player) {

    }

    @Override
    public void onPrepared(BetterVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(BetterVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(BetterVideoPlayer player) {

    }

    @Override
    public void onToggleControls(BetterVideoPlayer player, boolean isShowing) {

    }
}
