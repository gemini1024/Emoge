package com.emoge.app.emoge.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.emoge.app.emoge.R;
import com.tapadoo.alerter.Alerter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity {
    private static final String LOG_TAG = VideoView.class.getSimpleName();

    @BindView(R.id.video_video)
    VideoView mVideoView;

    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        if( getIntent() != null && getIntent().getData() != null ) {
            videoUri = getIntent().getData();
            Log.i(LOG_TAG, videoUri.toString());
            mVideoView.setVideoURI(videoUri);
            mVideoView.setMediaController(new MediaController(this));
            mVideoView.start();

            // TODO : 뷰에 연결 후 제거
            onCaptureVideo();
        } else {
            Log.e(LOG_TAG, getString(R.string.err_not_found_video_title));
            Alerter.create(this)
                    .setTitle(R.string.err_not_found_video_title)
                    .setText(R.string.err_not_found_video_text)
                    .show();
            finish();
        }
    }

    // TODO : 뷰에 연결
    void onCaptureVideo() {
        Intent returnIntent = new Intent();
        returnIntent.setData(videoUri);
        returnIntent.putExtra("startSec", 5000);
        returnIntent.putExtra("count", 5);
        returnIntent.putExtra("fps", 1000);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

}
