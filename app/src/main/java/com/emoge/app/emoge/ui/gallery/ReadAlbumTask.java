package com.emoge.app.emoge.ui.gallery;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.File;

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
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor imageCursor = fragment.getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);

        if(imageCursor != null) {
            int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);
            imageCursor.moveToFirst();
            do {
                String filePath = imageCursor.getString(dataColumnIndex);
                File imageFile = new File(filePath);
                adapter.addItemWithoutNotify(imageFile);
            } while(imageCursor.moveToNext());
            imageCursor.close();
            return true;
        }
        return false;
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
}
