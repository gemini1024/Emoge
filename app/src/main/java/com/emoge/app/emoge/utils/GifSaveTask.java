package com.emoge.app.emoge.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 7. 25.
 * 이미지(Frame)들을 이용해 GIF 로 저장 작업 ( Background )
 */

public class GifSaveTask extends AsyncTask<Integer, Integer, File> {
    private static final String LOG_TAG = GifSaveTask.class.getSimpleName();

    private Activity activity;
    private SweetAlertDialog progressDialog;
    private List<Frame> frames;

    public GifSaveTask(Activity activity, List<Frame> frames) {
        this.activity = activity;
        this.frames = frames;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = Dialogs.showLoadingProgressDialog(activity, R.string.saving_gif);
    }

    @Override
    protected File doInBackground(Integer... params) {
        if( frames.isEmpty() ) {
            return null;
        }
        GifMaker gifMaker = new GifMaker();
        ByteArrayOutputStream bos = gifMaker.makeGifByImages(frames, params[0]);
        return gifMaker.saveAsGif(bos);
    }

    @Override
    protected void onPostExecute(final File savedFile) {
        super.onPostExecute(savedFile);
        progressDialog.dismissWithAnimation();
        if(savedFile != null) {
            SweetAlertDialog dialog = Dialogs.showSuccessDialog(activity, R.string.complete, R.string.saved_gif);
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    showResultDialog(savedFile);
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        } else {
            Dialogs.showErrorDialog(activity,
                    R.string.err_dont_saving_gif_title, R.string.err_dont_saving_gif_content);
        }
    }

    private void showResultDialog(File file) {
        final CustomDialogController controller = Dialogs.showImageDialog(activity, Uri.fromFile(file));
        controller.setShareKakaoButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 공유
                Toast.makeText(activity, "share", Toast.LENGTH_SHORT).show();
                controller.dismiss();
            }
        });
    }
}