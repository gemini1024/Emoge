package com.emoge.app.emoge.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.model.GifMakingInfo;
import com.emoge.app.emoge.utils.dialog.ImageDialog;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;

import java.io.File;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 7. 25.
 * 이미지(Frame)들을 이용해 GIF 로 저장 작업 ( Background )
 */

public class GifSaveTask extends AsyncTask<GifMakingInfo, Integer, File> {
    private static final String LOG_TAG = GifSaveTask.class.getSimpleName();

    private SweetAlertDialog progressDialog;

    private Activity activity;
    private List<Frame> frames;
    private GifMakingInfo info;

    public GifSaveTask(Activity activity, List<Frame> frames) {
        this.activity = activity;
        this.frames = frames;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = SweetDialogs.showLoadingProgressDialog(activity, R.string.saving_gif);
    }

    @Override
    protected File doInBackground(GifMakingInfo... params) {
        info = params[0];
        if( frames.isEmpty() || info == null ) {
            return null;
        }
        return new GifMaker().saveAsGif(frames, info);
    }

    @Override
    protected void onPostExecute(final File savedFile) {
        super.onPostExecute(savedFile);
        progressDialog.dismissWithAnimation();
        if(savedFile != null) {
            SweetAlertDialog dialog = SweetDialogs.showSuccessDialog(activity, R.string.complete, R.string.saved_gif);
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    showResultImageDialog(savedFile);
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        } else {
            SweetDialogs.showErrorDialog(activity,
                    R.string.err_dont_saving_gif_title, R.string.err_dont_saving_gif_content);
        }
    }

    private void showResultImageDialog(@NonNull File file) {
        final Uri fileUri = Uri.fromFile(file);
        final ImageDialog imageDialog = new ImageDialog(activity, fileUri);
        imageDialog.setShareOtherAppButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GifSharer.shareOtherApps(activity, fileUri);
                imageDialog.dismiss();
            }
        }).setShareServerButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GifSharer.shareServer(info.getCategory(),info.getTitle(), fileUri);
                imageDialog.dismiss();
            }
        }).show();
    }
}