package com.emoge.app.emoge.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.emoge.app.emoge.MainApplication;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.StoreGif;
import com.emoge.app.emoge.ui.palette.MainActivity;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalleryActivity extends AppCompatActivity {
    private final String LOG_TAG = GalleryActivity.class.getSimpleName();

    @BindView(R.id.gallery_best)
    PhotoView bestPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.img_no_image).into(bestPhoto);
        loadGifImages();

        findViewById(R.id.toolbar_back).setVisibility(View.GONE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.gallery_container,
                GalleryFragment.newInstance(MainApplication.defaultDir, ImageFormatChecker.GIF_TYPE));
        fragmentTransaction.commit();
    }


    @OnClick(R.id.gallery_bt_making)
    void startMakingGif() {
        overridePendingTransition(0, android.R.anim.fade_in);
        startActivity(new Intent(this, MainActivity.class));
    }


    void loadGifImages() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("유머");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(LOG_TAG, dataSnapshot.toString());
                if(dataSnapshot.getValue(StoreGif.class) != null) {
                    StoreGif bestFavoriteGif = dataSnapshot.getChildren().iterator().next().getValue(StoreGif.class);
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        StoreGif storeGif = childSnapshot.getValue(StoreGif.class);
                        if( bestFavoriteGif.getFavorite() < storeGif.getFavorite()) {
                            bestFavoriteGif = storeGif;
                        }
                    }
                    Glide.with(GalleryActivity.this)
                            .load(bestFavoriteGif.getDownloadUrl())
                            .into(bestPhoto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(LOG_TAG, "서버 연결 실패");
            }
        });
    }

}
