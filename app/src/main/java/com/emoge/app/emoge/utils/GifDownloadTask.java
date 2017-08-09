package com.emoge.app.emoge.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.StoreGif;
import com.emoge.app.emoge.ui.gallery.GalleryActivity;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 8. 9.
 * 서버 이미지(StoreGif)를 다운로드하는 작업 ( Background )
 */

public class GifDownloadTask extends AsyncTask<StoreGif, Void, File> {
    private static final String LOG_TAG = GifDownloadTask.class.getSimpleName();

    private SweetAlertDialog progressDialog;

    private Activity activity;      // called Activity

    public GifDownloadTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = SweetDialogs.showLoadingProgressDialog(activity, R.string.download_gif_loading);
    }

    @Override
    protected File doInBackground(StoreGif... params) {
        StoreGif storeGif = params[0];
        File file = new GifMaker().makeFile(storeGif.getTitle());
        URLConnection connection;

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            connection = new URL(storeGif.getDownloadUrl()).openConnection();
            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int bufferLength;
            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                outputStream.write(buffer, 0, bufferLength);
            }
            inputStream.close();
            return file;
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getClass().getSimpleName(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(File savedFile) {
        super.onPostExecute(savedFile);
        progressDialog.dismissWithAnimation();
        if(savedFile != null) {
            Glide.with(activity).load(savedFile);
            SweetDialogs.showSuccessDialog(activity, R.string.download_gif_title, R.string.download_gif_content);
            if(activity instanceof GalleryActivity) {
                ((GalleryActivity)activity).notifyGallery();
            }
        } else {
            SweetDialogs.showErrorDialog(activity,
                    R.string.err_loading_image_title, R.string.err_loading_image_content);
        }
    }
}
