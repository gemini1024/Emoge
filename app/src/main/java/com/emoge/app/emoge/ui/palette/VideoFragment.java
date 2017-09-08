package com.emoge.app.emoge.ui.palette;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.ui.frame.FrameAdapter;
import com.emoge.app.emoge.utils.SeekBarNumberTransformers;
import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment implements BetterVideoCallback {
    private static final String LOG_TAG = VideoFragment.class.getSimpleName();

    public static final String INTENT_NAME_START_SEC        = "startSec";
    public static final String INTENT_NAME_CAPTURE_COUNT    = "count";
    public static final String INTENT_NAME_CAPTURE_DELAY    = "delay";

    private static final String ARG_VIDEO_DATA = "video_data";

    @BindView(R.id.video_video)
    BetterVideoPlayer mVideoView;
    @BindView(R.id.video_count)
    DiscreteSeekBar mCountBar;
    @BindView(R.id.video_fps)
    DiscreteSeekBar mFpsBar;

    private Uri videoUri;

    public VideoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(String videoData) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIDEO_DATA, videoData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoUri = Uri.parse(getArguments().getString(ARG_VIDEO_DATA));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);

        mVideoView.setCallback(this);
        mVideoView.setSource(videoUri);
        mVideoView.enableSwipeGestures();

        mCountBar.setMin(1);
        mCountBar.setMax(FrameAdapter.MAX_ITEM_SIZE);
        mCountBar.setProgress(FrameAdapter.MAX_ITEM_SIZE/2);

        mFpsBar.setNumericTransformer(SeekBarNumberTransformers.Multiply(100));
        mFpsBar.setMin(1);
        mFpsBar.setMax(20);
        mFpsBar.setProgress(5);

        return view;
    }

    @OnClick(R.id.main_gallery_video_back)
    void onFinish() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(0, android.R.anim.fade_out).remove(this).commit();
    }

    @OnClick(R.id.video_button)
    void onCaptureVideo() {
        Intent returnIntent = new Intent();
        returnIntent.setData(videoUri);
        returnIntent.putExtra(INTENT_NAME_START_SEC, mVideoView.getCurrentPosition());
        returnIntent.putExtra(INTENT_NAME_CAPTURE_COUNT, mCountBar.getProgress());
        returnIntent.putExtra(INTENT_NAME_CAPTURE_DELAY, mFpsBar.getProgress()*100);
        EventBus.getDefault().post(returnIntent);
    }

    @Override
    public void onPause() {
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
