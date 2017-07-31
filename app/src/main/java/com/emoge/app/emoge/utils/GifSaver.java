package com.emoge.app.emoge.utils;

import android.app.Activity;
import android.os.AsyncTask;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 7. 25.
 */

public class GifSaver extends AsyncTask<List<Frame>, Integer, Boolean> {
    private static final String LOG_TAG = GifSaver.class.getSimpleName();

    Activity activity;
    SweetAlertDialog progressDialog;

    public GifSaver(Activity activity) {
        this.activity = activity;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = Dialogs.showLoadingProgressDialog(activity, R.string.saving_gif);
    }

    @Override
    protected Boolean doInBackground(List<Frame>... params) {
        if( params[0].isEmpty() ) {
            return false;
        }
        GifMaker gifMaker = new GifMaker();
        ByteArrayOutputStream bos = gifMaker.makeGifByImages(params[0], 500);
        gifMaker.saveAsGif(bos);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean isSaved) {
        super.onPostExecute(isSaved);
        progressDialog.dismiss();
        if(isSaved) {
            Dialogs.showSuccessDialog(activity, R.string.complete, R.string.saved_gif);
        } else {
            // TODO : 만든 움짤 확인
            Dialogs.showSuccessDialog(activity,
                    R.string.err_dont_saving_gif_title, R.string.err_dont_saving_gif_content);
        }
    }
}