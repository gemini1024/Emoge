package com.emoge.app.emoge.ui.server;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emoge.app.emoge.R;
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
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 8. 3.
 * 서버의 카테고리별 View (Firebase 이용)
 */
public class CategoryFragment extends Fragment {
    private final String LOG_TAG = CategoryFragment.class.getSimpleName();
    private static final String ARG_CATEGORY = "category";

    @BindView(R.id.server_gif_list)
    RecyclerView mGifList;

    private StoreGifAdapter mGifAdapter;


    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(String category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server, container, false);
        ButterKnife.bind(this, view);

        // RecyclerView 설정
        mGifAdapter = new StoreGifAdapter(this, new ArrayList<StoreGif>());
        mGifList.setHasFixedSize(true);
        mGifList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mGifList.setAdapter(mGifAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mGifAdapter != null) {
            mGifAdapter.closeRealm();
        }
    }

    // 새로고침
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mGifAdapter != null) {
            if(isVisibleToUser) {
                if(!mGifAdapter.isEmpty()) {
                    mGifAdapter.clear();
                }
                loadGifImages();
            } else {
                mGifAdapter.clear();
            }
        }
    }

    // 서버연결 ( Firebase )
    private void loadGifImages() {
        final SweetAlertDialog dialog = SweetDialogs.showLoadingProgressDialog(getActivity(), R.string.loading_image);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(
                getArguments().getString(ARG_CATEGORY, getString(R.string.category_store)));
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(LOG_TAG, dataSnapshot.toString());
                mGifAdapter.clear();
                if(dataSnapshot.getValue(StoreGif.class) != null) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        StoreGif storeGif = childSnapshot.getValue(StoreGif.class);
                        mGifAdapter.addItem(storeGif);
                    }
                }
                dialog.dismissWithAnimation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismissWithAnimation();
                SweetDialogs.showErrorDialog(getActivity(),
                        R.string.err_loading_image_title, R.string.err_loading_image_content);
            }
        });
    }
}
