package com.emoge.app.emoge.ui.frame;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.utils.SeekBarNumberTransformers;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Video 에서 Frame 추출하기위한
 * 정보(캡처 시작 위치, 캡처 수, fps) 획득
 */

public class VideoActivity extends AppCompatActivity {
    private static final String LOG_TAG = VideoView.class.getSimpleName();

    public static final String INTENT_NAME_START_SEC        = "startSec";
    public static final String INTENT_NAME_CAPTURE_COUNT    = "count";
    public static final String INTENT_NAME_CAPTURE_DELAY    = "delay";


    @BindView(R.id.video_video)
    VideoView mVideoView;
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

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(LOG_TAG, getString(R.string.err_not_found_video_title));
                SweetDialogs.showErrorDialog(VideoActivity.this,
                        R.string.err_not_found_video_title, R.string.err_not_found_video_text);
                finish();
                return false;
            }
        });

        if( getIntent() != null && getIntent().getData() != null ) {
            videoUri = getIntent().getData();
            Log.i(LOG_TAG, videoUri.toString());
            mVideoView.setVideoURI(videoUri);
            mVideoView.setMediaController(new MediaController(mVideoView.getContext()));
            mVideoView.start();

            mFpsBar.setNumericTransformer(SeekBarNumberTransformers.Multiply(100));
            mFpsBar.setMin(1);
            mFpsBar.setMax(20);
            mFpsBar.setProgress(5);
        } else {
            Log.e(LOG_TAG, getString(R.string.err_not_found_video_title));
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

}
