package com.emoge.app.emoge.ui.palette;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.model.FrameStatusMessage;
import com.emoge.app.emoge.model.GifMakingInfo;
import com.emoge.app.emoge.model.History;
import com.emoge.app.emoge.model.PaletteMessage;
import com.emoge.app.emoge.ui.VideoActivity;
import com.emoge.app.emoge.ui.correction.CorrectImplAdapter;
import com.emoge.app.emoge.ui.correction.Correcter;
import com.emoge.app.emoge.ui.frame.FrameAddTask;
import com.emoge.app.emoge.ui.frame.FrameAdder;
import com.emoge.app.emoge.ui.gallery.GalleryViewPagerAdapter;
import com.emoge.app.emoge.ui.gallery.ImageFormatChecker;
import com.emoge.app.emoge.ui.history.HistoryImplAdapter;
import com.emoge.app.emoge.ui.view.MenuButtons;
import com.emoge.app.emoge.utils.GifSaveTask;
import com.emoge.app.emoge.utils.dialog.EditorDialog;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;
import com.github.chrisbanes.photoview.PhotoView;
import com.nightonke.boommenu.BoomMenuButton;
import com.zomato.photofilters.imageprocessors.Filter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
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
    @BindView(R.id.toolbar_next)        ImageButton mNextButton;
    @BindView(R.id.main_bt_add_frame)   BoomMenuButton mAddMenu;
    @BindView(R.id.main_bt_correction)  BoomMenuButton mCorrectMenu;
    @BindView(R.id.main_frame_list)     RecyclerView mFrameRecyclerView;
    @BindView(R.id.main_history)        RecyclerView mHistoryView;
    @BindView(R.id.main_preview)        PhotoView mPreview;
    @BindView(R.id.main_fps_text)       TextView mFpsTextView;

    @BindView(R.id.main_palette_window)
    ConstraintLayout mPaletteWindow;
    @BindView(R.id.main_gallery_window)
    ConstraintLayout mGalleryWindow;
    @BindView(R.id.main_gallery_container)
    ViewPager mGallery;

    // Frame 저장 및 수정용 Adapter
    private CorrectImplAdapter mFrameAdapter;
    private HistoryImplAdapter mHistoryAdapter;

    private TabLayout.Tab mMainTab;

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



    // Gallery/Palette Animation
    private void enterViews(View view) {
        final Animation enterAnim = AnimationUtils.loadAnimation(this, R.anim.enter);
        view.setAnimation(enterAnim);
        view.setVisibility(View.VISIBLE);
    }

    private void exitViews(View view) {
        final Animation exitAnim = AnimationUtils.loadAnimation(this, R.anim.exit);
        view.setAnimation(exitAnim);
        view.setVisibility(View.GONE);
    }



    // Initialize
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mHandler = new Handler();
        Correcter correcter = new Correcter(this);
        FrameAdder frameAdder = new FrameAdder(this);

        addGallery();
        addPalette(correcter);
        setFrameList(frameAdder, correcter);
        setHistory();
        enableButtons(frameAdder, correcter);
    }

    private void addGallery() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_gallery_container);
        viewPager.setAdapter(new GalleryViewPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_gallery_tab);
        tabLayout.setupWithViewPager(viewPager);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enterViews(mGalleryWindow);
            }
        }, 500);
    }

    private void addPalette(Correcter correcter) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
        fragmentTransaction.add(R.id.main_palette_container,
                PaletteFragment.newInstance(Correcter.MOD_FRAME_DELAY, correcter.getCurrentFps()));
        fragmentTransaction.commit();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_palette_tab);
        mMainTab = tabLayout.newTab().setIcon(R.drawable.ic_play);
        tabLayout.addTab(mMainTab);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_brightness));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_contrast));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_gamma));
        tabLayout.addOnTabSelectedListener(correcter);

    }

    private void setFrameList(FrameAdder frameAdder, Correcter correcter) {
        mFrameAdapter = new CorrectImplAdapter(mFrameRecyclerView,new ArrayList<Frame>(), frameAdder, correcter);
        mFrameAdapter.setDialogTargetWhenOnItemClick(this);
        mFrameRecyclerView.setHasFixedSize(true);
        mFrameRecyclerView.setAdapter(mFrameAdapter);
        mFrameRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mFrameRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setHistory() {
        mHistoryAdapter = new HistoryImplAdapter(mFrameAdapter, new ArrayList<History>());
        mHistoryView.setHasFixedSize(true);
        mHistoryView.setAdapter(mHistoryAdapter);
        LinearLayoutManager stackLayoutManager = new LinearLayoutManager(this);
        stackLayoutManager.setStackFromEnd(true);
        mHistoryView.setLayoutManager(stackLayoutManager);
    }

    private void enableButtons(FrameAdder frameAdder, final Correcter correcter) {
        ImageButton backButton = (ImageButton) findViewById(R.id.toolbar_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.onBackPressed();
            }
        });
        mNextButton.setVisibility(View.VISIBLE);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFrameAdapter.isEmpty()) {
                    SweetDialogs.showErrorDialog(MainActivity.this,
                            R.string.err_no_image_title, R.string.err_no_image_content);
                } else if(mGalleryWindow.getVisibility() == View.VISIBLE) {
                    mMainTab.select();
                    correcter.onTabSelected(mMainTab);
                    exitViews(mGalleryWindow);
                    enterViews(mPaletteWindow);
                    mNextButton.setVisibility(View.GONE);
                    mHistoryAdapter.clearHistory();
                    mHistoryAdapter.setOriginalFrames();
                }
            }
        });
        MenuButtons.buildAddButton(this,mAddMenu, frameAdder);
        MenuButtons.buildSelectButton(this,mCorrectMenu, correcter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mFrameAdapter != null) {
            mFrameAdapter.clear();
            mHistoryAdapter.clearHistory();
        }
    }




    // 프레임 딜레이 보기 쉽게 프리뷰 위에 표시
    private void showFps(int value) {
        mFpsTextView.setVisibility(View.VISIBLE);
        mFpsTextView.setText(String.format("%.2f s", value/1000.f));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFpsTextView.setVisibility(View.GONE);
            }
        }, 500);
    }



    // EventBus -
    // 보정 기능 ( Message from PaletteFragment, FrameAdder(only frame delay message) )
    @Subscribe(threadMode = ThreadMode.MAIN)
    void onPaletteEvent(PaletteMessage message) {
        mHandler.removeCallbacks(mTask);
        if(message.getType() == Correcter.MOD_FRAME_DELAY) {
            showFps(message.getValue());
        } else if(message.getType() == Correcter.CORRECT_APPLY) {
            mHistoryAdapter.addHistory();
        }
        mFrameAdapter.correct(message.getType(), message.getValue());
        mPreview.setImageBitmap(mFrameAdapter.getItem(mPreviewIndex).getBitmap());
        mHandler.postDelayed(mTask, mFrameAdapter.getFps());
        mFrameAdapter.clearPreviousFrames();    // View 에서 띄우는 이미지를 변경했으므로 -> 제거
    }

    // 필터 적용 ( Message from Correcter )
    @Subscribe(threadMode = ThreadMode.MAIN)
    void onPaletteEvent(Filter filter) {
        mHandler.removeCallbacks(mTask);
        mFrameAdapter.setFilter(filter);
        mHistoryAdapter.addHistory();
        mFrameAdapter.apply();
        mPreview.setImageBitmap(mFrameAdapter.getItem(mPreviewIndex).getBitmap());
        mHandler.postDelayed(mTask, mFrameAdapter.getFps());
        mFrameAdapter.clearPreviousFrames();    // View 에서 띄우는 이미지를 변경했으므로 -> 제거
    }


    // FrameAdapter 상태 Event ( Message from FrameAdapter )
    @Subscribe(threadMode = ThreadMode.MAIN)
    void receiveFrameStatusMessage(FrameStatusMessage message) {
        if(message.equals(FrameStatusMessage.FULL)) {
        } else if(message.equals(FrameStatusMessage.NOT_FULL)){
        } else {
            while(mGalleryWindow.getVisibility() != View.VISIBLE) {
                MainActivity.this.onBackPressed();
            }
        }
    }

    // Add File ( Message from GalleryAdapter )
    @Subscribe(threadMode = ThreadMode.MAIN)
    void addFileFromGallery(File file) {
        int requestCode;
        if(ImageFormatChecker.inFormat(file, ImageFormatChecker.IMAGE_FORMAT)) {
            requestCode = FrameAdder.INTENT_GET_IMAGE;
        } else if(ImageFormatChecker.inFormat(file, ImageFormatChecker.GIF_FORMAT)) {
            requestCode = FrameAdder.INTENT_GET_GIF;
        } else {
            SweetDialogs.showErrorDialog(this,
                    R.string.err_add_file_title, R.string.err_add_file_content);
            return;
        }

        new FrameAddTask(this, mFrameAdapter, requestCode)
                .execute(new Intent().setData(Uri.fromFile(file)));
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



    // select video
    private void startVideoActivity(@NonNull Intent videoData) {
        if(videoData.getData() != null) {
            Intent videoActivityIntent = new Intent(this, VideoActivity.class);
            videoActivityIntent.setData(videoData.getData());
            overridePendingTransition(0, android.R.anim.fade_in);
            startActivityForResult(videoActivityIntent, FrameAdder.INTENT_CAPTURE_VIDEO);
        }
    }

    // startVideoActivity() or FrameAddTask
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



    // 저장 기능 (category, quality, title 설정)
    @OnClick(R.id.main_save)
    void showEditorDialog() {
        if(mFrameAdapter.isEmpty()) {
            SweetDialogs.showErrorDialog(this, R.string.err_no_image_title, R.string.err_no_image_content);
        } else {
            EventBus.getDefault().post(new PaletteMessage(Correcter.CORRECT_APPLY, 0));
            final EditorDialog editorDialog = new EditorDialog(this);
            editorDialog.setSaveButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeToGif(editorDialog);
                }
            }).show();
        }
    }

    // making gif
    private void makeToGif(EditorDialog editorDialog) {
        String gifTitle = editorDialog.getContent();
        if(TextUtils.isEmpty(gifTitle)) {
            editorDialog.setError(getString(R.string.naming_gif_hint));
        } else {
            GifMakingInfo makingInfo = new GifMakingInfo(editorDialog.getCategory(),
                    editorDialog.getContent(), editorDialog.getQuality(), mFrameAdapter.getFps());
            new GifSaveTask(MainActivity.this, mFrameAdapter.getFrames())
                    .execute(makingInfo);
            editorDialog.dismiss();
        }

    }


    // 실수로 나가기 방지
    @Override
    public void onBackPressed() {
        if(mGalleryWindow.getVisibility() == View.VISIBLE) {
            // 이미지 추가 중인 경우
            if(!mFrameAdapter.isEmpty()) {
                SweetDialogs.showExitDialog(this);
            } else {
                super.onBackPressed();
            }
        } else {
            // 보정 중인 경우
            EventBus.getDefault().post(new PaletteMessage(Correcter.CORRECT_APPLY, 0));
            exitViews(mPaletteWindow);
            enterViews(mGalleryWindow);
            mNextButton.setVisibility(View.VISIBLE);
        }
    }
}