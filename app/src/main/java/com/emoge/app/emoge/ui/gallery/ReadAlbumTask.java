package com.emoge.app.emoge.ui.gallery;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.snatik.storage.Storage;

import java.io.File;
import java.util.List;

/**
 * Created by jh on 17. 8. 8.
 */

public class ReadAlbumTask extends AsyncTask<String, Void, Boolean> {
    private final String LOG_TAG = ReadAlbumTask.class.getSimpleName();

    private Fragment fragment;
    private GalleryAdapter adapter;

    public ReadAlbumTask(Fragment fragment, GalleryAdapter adapter) {
        this.fragment = fragment;
        this.adapter = adapter;
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
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean) {
            adapter.notifyDataSetChanged();
        } else {
            Log.e(LOG_TAG, "not found images");
        }
    }

    private boolean findDirectory(String filepath) {
        Storage storage = new Storage(fragment.getContext());
        if(!storage.isDirectoryExists(filepath)) {
            storage.createDirectory(filepath);
        }
        List<File> myFiles = storage.getFiles(filepath);
        for(File file : myFiles) {
            adapter.addItemWithoutNotify(file);
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
