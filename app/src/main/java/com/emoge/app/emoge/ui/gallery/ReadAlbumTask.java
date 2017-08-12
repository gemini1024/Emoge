package com.emoge.app.emoge.ui.gallery;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.emoge.app.emoge.R;
import com.snatik.storage.Storage;
import com.snatik.storage.helpers.OrderType;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by jh on 17. 8. 8.
 * execute(dirPath).
 * dirPath 존재 시 - 해당 Dir 의 파일가져오기
 * dirPath null - 전체 이미지 파일 가져오기
 * 시간(최신)순 정렬
 */

class ReadAlbumTask extends AsyncTask<String, Void, Boolean> {
    private final String LOG_TAG = ReadAlbumTask.class.getSimpleName();

    private Fragment fragment;
    private LocalImageLoadable adapter;
    private SwipeRefreshLayout refresher;
    private ImageView noImageView;

    ReadAlbumTask(Fragment fragment, LocalImageLoadable adapter,
                  SwipeRefreshLayout refresher, ImageView noImageView) {
        this.fragment = fragment;
        this.adapter = adapter;
        this.refresher = refresher;
        this.noImageView = noImageView;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if(params[0] != null) {
            return findDirectory(params[0]);
        } else {
            return findAllDirectory();
        }
    }

    @Override
    protected void onPostExecute(Boolean hasLoaded) {
        super.onPostExecute(hasLoaded);
        refresher.setRefreshing(false);
        if(!hasLoaded || adapter.isEmpty()) {
            Glide.with(fragment.getContext()).load(R.drawable.img_no_image).into(noImageView);
            noImageView.setVisibility(View.VISIBLE);
        } else {
            noImageView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }

    private boolean findDirectory(String filepath) {
        Storage storage = new Storage(fragment.getContext());
        if(!storage.isDirectoryExists(filepath)) {
            storage.createDirectory(filepath);
        }
        List<File> myFiles = storage.getFiles(filepath);
        if (myFiles != null) {
            Collections.sort(myFiles, OrderType.DATE.getComparator());
            for(File file : myFiles) {
                adapter.addItemWithoutNotify(file);
            }
        }
        return true;
    }

    private boolean findAllDirectory() {
        String[] projection = { MediaStore.Images.Media.DATA,
                MediaStore.Images.ImageColumns.DATE_TAKEN };
        Cursor imageCursor = fragment.getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        if(imageCursor != null) {
            int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);
            if(imageCursor.moveToFirst()) {
                do {
                    String filePath = imageCursor.getString(dataColumnIndex);
                    File imageFile = new File(filePath);
                    adapter.addItemWithoutNotify(imageFile);
                } while (imageCursor.moveToNext());
            }
            imageCursor.close();
            return true;
        }
        return false;
    }
}
