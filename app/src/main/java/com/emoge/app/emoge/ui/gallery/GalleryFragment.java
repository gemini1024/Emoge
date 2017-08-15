package com.emoge.app.emoge.ui.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.emoge.app.emoge.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String LOG_TAG = GalleryFragment.class.getSimpleName();

    private static final String ARG_DIR_PATH    = "dir_path";
    private static final String ARG_TYPE        = "image_type";
    private static final int GALLERY_WIDTH_NUM  = 4;

    private String mDirPath;        // Nullable. (움짤 생성 화면에서 null)
    private int mImageType;         // 포함시킬 파일 Format (ImageFormatChecker)

    @BindView(R.id.gallery_container)
    SwipeRefreshLayout mRefresher;
    @BindView(R.id.gallery)
    RecyclerView mGallery;
    @BindView(R.id.gallery_no_image)
    ImageView mNoImage;

    private GalleryAdapter mGalleryAdapter;

    public GalleryFragment() {
    }

    public static GalleryFragment newInstance(String filePath, int imageFormat) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DIR_PATH, filePath);
        args.putInt(ARG_TYPE, imageFormat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDirPath = getArguments().getString(ARG_DIR_PATH, null);
            mImageType = getArguments().getInt(ARG_TYPE, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);

        // RecyclerView 설정
        mRefresher.setOnRefreshListener(this);
        List<String> fileFormat = mImageType == ImageFormatChecker.IMAGE_TYPE ?
                ImageFormatChecker.IMAGE_FORMAT : ImageFormatChecker.GIF_FORMAT;
        mGalleryAdapter = new GalleryAdapter(this, fileFormat, new ArrayList<File>(), TextUtils.isEmpty(mDirPath));
        mGallery.setHasFixedSize(true);
        mGallery.setLayoutManager(new StaggeredGridLayoutManager(GALLERY_WIDTH_NUM, StaggeredGridLayoutManager.VERTICAL));
        mGallery.setAdapter(mGalleryAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mGalleryAdapter.clear();
        new ReadAlbumTask(this, mGalleryAdapter, mRefresher, mNoImage).execute(mDirPath);
    }
}
