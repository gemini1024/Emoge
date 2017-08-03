package com.emoge.app.emoge.ui.server;

/**
 * Created by jh on 17. 8. 3.
 */

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
import com.emoge.app.emoge.utils.Dialogs;
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
 * A placeholder fragment containing a simple view.
 */
public class CategoryFragment extends Fragment {
    private final String LOG_TAG = CategoryFragment.class.getSimpleName();
    private static final String ARG_CATEGORY = "category";

    @BindView(R.id.server_gif_list)
    RecyclerView mGifList;

    private StoreGifAdapter mGifAdapter;


    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(int sectionNumber) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server, container, false);
        ButterKnife.bind(this, view);

        // RecyclerView 설정
        mGifAdapter = new StoreGifAdapter(getContext(), new ArrayList<StoreGif>());
        mGifList.setHasFixedSize(true);
        mGifList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mGifList.setAdapter(mGifAdapter);

        loadGifImages();

        return view;
    }

    void loadGifImages() {
        final SweetAlertDialog dialog = Dialogs.showLoadingProgressDialog(getActivity(), R.string.loading_image);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(
                CategoryPagerAdapter.getCategoryName(getArguments().getInt(ARG_CATEGORY)).toString());
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
                Dialogs.showErrorDialog(getActivity(),
                        R.string.err_loading_image_title, R.string.err_loading_image_content);
            }
        });
    }


}
