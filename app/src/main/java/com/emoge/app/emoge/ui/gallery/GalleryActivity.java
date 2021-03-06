package com.emoge.app.emoge.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.emoge.app.emoge.MainApplication;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.StoreGif;
import com.emoge.app.emoge.ui.gallery.best.ServerImageScrollAdapter;
import com.emoge.app.emoge.ui.license.LicenseActivity;
import com.emoge.app.emoge.ui.palette.MainActivity;
import com.emoge.app.emoge.ui.server.ServerActivity;
import com.emoge.app.emoge.ui.view.ShowCase;
import com.emoge.app.emoge.utils.Logger;
import com.emoge.app.emoge.utils.NetworkStatus;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class GalleryActivity extends AppCompatActivity {
    private final String LOG_TAG = GalleryActivity.class.getSimpleName();

    private static final int BEST_PHOTO_NUM = 5;

    @BindView(R.id.gallery_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.gallery_best)
    DiscreteScrollView mBestPhoto;
    @BindView(R.id.gallery_best_loading)
    AVLoadingIndicatorView mBestLoading;
    @BindView(R.id.gallery_window)
    ConstraintLayout mGalleryWindow;

    private List<StoreGif> mBestFavoriteGifs;
    private Fragment mGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        mBestFavoriteGifs = new ArrayList<>();
        NetworkStatus.executeWithCheckingNetwork(GalleryActivity.this, new NetworkStatus.RequireIntentTask() {
            @Override
            public void Task() {
                loadGifImages();
            }
        });

        addNavigation();
        addGalleryFragment();
        enterGallery();
        ShowCase.startShowCase(this);
    }


    // Navigation Drawer 추가
    private void addNavigation() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mNavigationDrawer, (Toolbar) findViewById(R.id.toolbar),
                R.string.navigation_open, R.string.navigation_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                ConstraintLayout drawerLayout = (ConstraintLayout) findViewById(R.id.navigation_container);
                ConstraintLayout contentLayout = (ConstraintLayout) findViewById(R.id.gallery_contents);
                contentLayout.setTranslationX(slideOffset * drawerLayout.getWidth());
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }
        };
        mNavigationDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Gallery Fragment 불러오기
    private void addGalleryFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        mGallery = GalleryFragment.newInstance(MainApplication.defaultDir,
                ImageFormatChecker.GIF_TYPE);
        fragmentTransaction.add(R.id.gallery_container, mGallery);
        fragmentTransaction.commit();
    }

    // GalleryWindow Animation
    private void enterGallery() {
        final Animation enterAnim = AnimationUtils.loadAnimation(this, R.anim.popup_enter);
        mGalleryWindow.setAnimation(enterAnim);
        mGalleryWindow.setVisibility(View.VISIBLE);
    }

    private void exitGallery() {
        final Animation exitAnim = AnimationUtils.loadAnimation(this, R.anim.popup_exit);
        mGalleryWindow.setAnimation(exitAnim);
        mGalleryWindow.setVisibility(View.GONE);
    }

    // 변경 사항 적용
    public void notifyGallery() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().detach(mGallery).attach(mGallery).commit();
    }


    // Server 에서 가장 인기있는 움짤 가져오기
    void loadGifImages() {
        String[] categories = getResources().getStringArray(R.array.server_category);
        for(String category : categories) {
            loadGifsByCategory(FirebaseDatabase.getInstance().getReference(category));
        }
    }

    private void loadGifsByCategory(DatabaseReference categoryDb) {
        categoryDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Logger.d(LOG_TAG, dataSnapshot.toString());
                if (dataSnapshot.getValue(StoreGif.class) != null) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        mBestFavoriteGifs.add(childSnapshot.getValue(StoreGif.class));
                    }
                }
                Collections.sort(mBestFavoriteGifs, new Comparator<StoreGif>() {
                    @Override
                    public int compare(StoreGif o1, StoreGif o2) {
                        return Integer.compare(o2.getFavorite(), o1.getFavorite());
                    }
                });
                mBestFavoriteGifs.subList(Math.min(5, mBestFavoriteGifs.size()), mBestFavoriteGifs.size()).clear();
                setBestFavoriteGif();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Logger.e(LOG_TAG, databaseError);
            }
        });
    }

    // Server 에서 인기 있는 움짤 가져와서 보여주기
    private void setBestFavoriteGif() {
        if (mBestFavoriteGifs.isEmpty()) {
            return;
        }
        mBestLoading.hide();
        mBestPhoto.setAdapter(new ServerImageScrollAdapter(this, mBestFavoriteGifs));
        mBestPhoto.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
    }

    // Navigation -
    // 서버로
    @OnClick({R.id.navigation_server_icon, R.id.navigation_server})
    void connectToServer() {
        NetworkStatus.executeWithCheckingNetwork(GalleryActivity.this, new NetworkStatus.RequireIntentTask() {
            @Override
            public void Task() {
                startActivity(new Intent(GalleryActivity.this, ServerActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
    }

    // 움짤 생성하러 가기
    @OnClick({R.id.navigation_making_gif_icon, R.id.navigation_making_gif, R.id.gallery_bt_making})
    public void startMakingGif() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    // License 보기
    @OnClick({R.id.navigation_license_icon, R.id.navigation_license})
    void showLicense() {
        startActivity(new Intent(this, LicenseActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    // 설명서 다시보기
    @OnClick({R.id.navigation_help_icon, R.id.navigation_help})
    void showHelp() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        }
        ShowCase.initShownShowCase();
        ShowCase.startShowCase(this);
    }

    @OnClick({R.id.navigation_exit_icon, R.id.navigation_exit})
    void exitApp() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        }
        showExitAppDialog();
    }

    private void showExitAppDialog() {
        SweetDialogs.showExitAppDialog(this).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                exitGallery();
                sweetAlertDialog.dismissWithAnimation();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            showExitAppDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(getApplicationContext()).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(getApplicationContext()).clearDiskCache();
            }
        }).start();
    }
}
