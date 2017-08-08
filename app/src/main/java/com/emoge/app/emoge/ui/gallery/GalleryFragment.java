package com.emoge.app.emoge.ui.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emoge.app.emoge.R;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryFragment extends Fragment {
    private final String LOG_TAG = GalleryFragment.class.getSimpleName();

    private static final String ARG_DIR_PATH = "dir_path";
    private String mDirPath;

    @BindView(R.id.gallery)
    RecyclerView mGallery;

    private GalleryAdapter mGalleryAdapter;

    public GalleryFragment() {
    }

    public static GalleryFragment newInstance(String param1) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DIR_PATH, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDirPath = getArguments().getString(ARG_DIR_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);


        // RecyclerView 설정
        mGalleryAdapter = new GalleryAdapter(getContext(), ImageFormatChecker.IMAGE_FORMAT, new ArrayList<File>());
        mGallery.setHasFixedSize(true);
        mGallery.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        mGallery.setAdapter(mGalleryAdapter);

        new ReadAlbumTask(this, mGalleryAdapter).execute(mDirPath);

        return view;
    }

}
