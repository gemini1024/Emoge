package com.emoge.app.emoge.ui.server;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.StoreGif;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by jh on 17. 8. 7.
 */

public class CategoryFavoriteFragment extends Fragment {
    private final String LOG_TAG = CategoryFavoriteFragment.class.getSimpleName();

    @BindView(R.id.server_gif_list)
    RecyclerView mGifList;

    private StoreGifAdapter mGifAdapter;
    private Realm realm;


    public CategoryFavoriteFragment() {
    }

    public static CategoryFavoriteFragment newInstance() {
        return new CategoryFavoriteFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server, container, false);
        ButterKnife.bind(this, view);
//        realm = Realm.getDefaultInstance();

        // RecyclerView 설정
        mGifAdapter = new StoreGifAdapter(getContext(), new ArrayList<StoreGif>());
        mGifList.setHasFixedSize(true);
        mGifList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mGifList.setAdapter(mGifAdapter);


        // TODO : Realm 으로 담은 움짤 가져오기

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mGifAdapter != null) {
            mGifAdapter.clear();
        }
    }
}
