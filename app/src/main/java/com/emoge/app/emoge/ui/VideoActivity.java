package com.emoge.app.emoge.ui;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        if( getIntent() != null && getIntent().getData() != null ) {
            Log.i(LOG_TAG, getIntent().getData().toString());
            mVideoView.setVideoURI(getIntent().getData());
            mVideoView.setMediaController(new MediaController(this));
            mVideoView.start();
        } else {
            Log.e(LOG_TAG, getString(R.string.err_not_found_video_title));
            Alerter.create(this)
                    .setTitle(R.string.err_not_found_video_title)
                    .setText(R.string.err_not_found_video_text)
                    .show();
            finish();
        }
    }

}
