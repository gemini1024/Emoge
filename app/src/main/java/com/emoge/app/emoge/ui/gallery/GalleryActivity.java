package com.emoge.app.emoge.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidviewhover.BlurLayout;
import com.emoge.app.emoge.MainApplication;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.StoreGif;
import com.emoge.app.emoge.ui.palette.MainActivity;
import com.emoge.app.emoge.ui.server.ServerActivity;
import com.emoge.app.emoge.ui.view.HoverViews;
import com.emoge.app.emoge.utils.GifDownloadTask;
import com.emoge.app.emoge.utils.NetworkStatus;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class GalleryActivity extends AppCompatActivity {
    private final String LOG_TAG = GalleryActivity.class.getSimpleName();

    @BindView(R.id.gallery_best)    ImageView mBestPhoto;
    @BindView(R.id.gallery_window)  ConstraintLayout mGalleryWindow;

    private StoreGif mBestFavoriteGif;
    private Fragment mGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.img_loading).into(mBestPhoto);     // holder
        loadGifImages();

        addGalleryFragment();
        enterGallery();
    }

    // Gallery Fragment 불러오기
    private void addGalleryFragment() {
        findViewById(R.id.toolbar_back).setVisibility(View.GONE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        mGallery = GalleryFragment.newInstance(MainApplication.defaultDir,
                ImageFormatChecker.GIF_TYPE);
        fragmentTransaction.add(R.id.gallery_container, mGallery);
        fragmentTransaction.commit();
    }

    // GalleryWindow Animation
    private void enterGallery() {
        final Animation enterAnim = AnimationUtils.loadAnimation(this, R.anim.enter);
        mGalleryWindow.setAnimation(enterAnim);
        mGalleryWindow.setVisibility(View.VISIBLE);
    }

    private void exitGallery() {
        final Animation exitAnim = AnimationUtils.loadAnimation(this, R.anim.exit);
        mGalleryWindow.setAnimation(exitAnim);
        mGalleryWindow.setVisibility(View.GONE);
    }

    // 변경 사항 적용
    public void notifyGallery() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().detach(mGallery).attach(mGallery).commit();
    }


    // 움짤 생성하러 가기
    @OnClick(R.id.gallery_bt_making)
    void startMakingGif() {
        overridePendingTransition(0, android.R.anim.fade_in);
        startActivity(new Intent(this, MainActivity.class));
    }


    // Server 에서 가장 인기있는 움짤 가져오기
    void loadGifImages() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("유머");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(LOG_TAG, dataSnapshot.toString());
                if(dataSnapshot.getValue(StoreGif.class) != null) {
                    mBestFavoriteGif = dataSnapshot.getChildren().iterator().next().getValue(StoreGif.class);
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        StoreGif storeGif = childSnapshot.getValue(StoreGif.class);
                        if( mBestFavoriteGif.getFavorite() < storeGif.getFavorite()) {
                            mBestFavoriteGif = storeGif;
                        }
                    }
                    setBestFavoriteGif();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(LOG_TAG, "서버 연결 실패");
            }
        });
    }

    // Server 에서 인기 있는 움짤 가져와서 보여주기
    private void setBestFavoriteGif() {
        if(mBestFavoriteGif == null) {
            return;
        }
        Glide.with(GalleryActivity.this)
                .load(Uri.parse(mBestFavoriteGif.downloadUrl))
                .apply(new RequestOptions().format(DecodeFormat.PREFER_RGB_565)
                        .placeholder(R.drawable.img_loading))
                .into(mBestPhoto);
        setHoverByGif(mBestFavoriteGif);
    }

    // 인기 있는 움짤 칸을 덮어씌울 Hover View 설정
    private void setHoverByGif(StoreGif storeGif) {
        HoverViews hover = new HoverViews(this, (BlurLayout)findViewById(R.id.gallery_hover));
        hover.buildHoverView();
        hover.setText(storeGif.getTitle());
        hover.setDownloadButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada)
                        .duration(HoverViews.BLUR_DURATION)
                        .playOn(v);
                new GifDownloadTask(GalleryActivity.this).execute(mBestFavoriteGif);
            }
        });
        hover.setMoreButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Swing)
                        .duration(HoverViews.BLUR_DURATION)
                        .playOn(v);

                NetworkStatus.executeWithCheckingNetwork(GalleryActivity.this, new NetworkStatus.RequireIntentTask() {
                    @Override
                    public void Task() {
                        overridePendingTransition(0, android.R.anim.fade_in);
                        startActivity(new Intent(GalleryActivity.this, ServerActivity.class));
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        SweetDialogs.showExitAppDialog(this).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                exitGallery();
                sweetAlertDialog.dismissWithAnimation();
                finish();
            }
        });
    }
}
