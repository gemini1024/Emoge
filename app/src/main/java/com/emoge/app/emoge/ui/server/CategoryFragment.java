package com.emoge.app.emoge.ui.server;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.MyStoreGif;
import com.emoge.app.emoge.model.StoreGif;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jh on 17. 8. 3.
 * 서버의 카테고리별 View (Firebase 이용)
 */
public class CategoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String LOG_TAG = CategoryFragment.class.getSimpleName();
    private static final String ARG_CATEGORY = "category";

    @BindView(R.id.server_gif_list_container)
    SwipeRefreshLayout mRefresher;
    @BindView(R.id.server_gif_list)
    RecyclerView mGifList;
    @BindView(R.id.server_no_image)
    ImageView mNoImage;
    @BindView(R.id.server_progress)
    ProgressBar mProgress;

    private StoreGifAdapter mGifAdapter;
    private Realm mRealm;
    private DatabaseReference mFirebaseDb;


    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(String category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    private boolean isFavoriteCategory() {
        return getArguments() == null || getArguments().getString(ARG_CATEGORY) == null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server, container, false);
        ButterKnife.bind(this, view);
        mRealm = Realm.getDefaultInstance();
        if(!isFavoriteCategory()) {
            mFirebaseDb = FirebaseDatabase.getInstance().getReference(
                    getArguments().getString(ARG_CATEGORY, getString(R.string.category_store)));
        }

        // RecyclerView 설정
        mRefresher.setOnRefreshListener(this);
        mGifAdapter = new StoreGifAdapter(this, new ArrayList<StoreGif>(), mRealm, mFirebaseDb);
        mGifList.setHasFixedSize(true);
        mGifList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mGifList.setAdapter(mGifAdapter);

        if(isFavoriteCategory()) {
            loadFavoriteGifImages();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if( mRealm != null ) {
            mRealm.close();
        }
    }

    // 새로고침
    @Override
    public void onRefresh() {
        mRefresher.setRefreshing(false);
        if(isFavoriteCategory()) {
            loadFavoriteGifImages();
        } else {
            loadServerGifImages();
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mGifAdapter != null) {
            if(isVisibleToUser) {
                onRefresh();
            } else {
                mGifAdapter.clear();
            }
        }
    }


    // Realm 에서 불러오기
    private void loadFavoriteGifImages() {
        RealmResults<MyStoreGif> myStoreGifs = mRealm.where(MyStoreGif.class).findAll();
        mGifAdapter.clear();
        for(MyStoreGif myStoreGif : myStoreGifs) {
            mGifAdapter.addItem(new StoreGif(myStoreGif.getTitle(), myStoreGif.getDownloadUrl(), -1));
        }
        if(mGifAdapter.isEmpty()) {
            Glide.with(getContext()).load(R.drawable.img_no_image).into(mNoImage);
            mNoImage.setVisibility(View.VISIBLE);
        } else {
            mNoImage.setVisibility(View.GONE);
        }
    }

    // 서버연결 ( Firebase )
    private void loadServerGifImages() {
        mProgress.setVisibility(View.VISIBLE);
        mFirebaseDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgress.setVisibility(View.GONE);
                Log.i(LOG_TAG, dataSnapshot.toString());
                mGifAdapter.clear();
                if(dataSnapshot.getValue(StoreGif.class) != null) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        StoreGif storeGif = childSnapshot.getValue(StoreGif.class);
                        mGifAdapter.addItem(storeGif);
                    }
                }
                if(mGifAdapter.isEmpty()) {
                    Glide.with(getContext()).load(R.drawable.img_no_image).into(mNoImage);
                    mNoImage.setVisibility(View.VISIBLE);
                } else {
                    mNoImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgress.setVisibility(View.GONE);
                SweetDialogs.showErrorDialog(getActivity(),
                        R.string.err_loading_image_title, R.string.err_loading_image_content);
            }
        });
    }

}
