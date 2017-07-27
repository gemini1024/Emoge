package com.emoge.app.emoge.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by jh on 17. 7. 25.
 */

public class GifSaver extends AsyncTask<List<Frame>, Integer, Boolean> {
    private static final String LOG_TAG = GifSaver.class.getSimpleName();

    Activity activity;
    ProgressDialog progressDialog;

    public GifSaver(Activity activity) {
        this.activity = activity;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(activity,
                activity.getString(R.string.saving_gif_title),
                activity.getString(R.string.saving_gif_loading), false, false);
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
            Toast.makeText(activity, R.string.saving_gif, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, R.string.err_dont_saving_gif, Toast.LENGTH_SHORT).show();
        }
    }
}