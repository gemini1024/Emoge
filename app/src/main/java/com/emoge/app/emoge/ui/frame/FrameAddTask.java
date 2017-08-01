package com.emoge.app.emoge.ui.frame;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.utils.Dialogs;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 8. 1.
 * 파일에서 Frame 추가 작업 ( Background )
 */

public class FrameAddTask extends AsyncTask<Intent, Void, Boolean> {
    private final String LOG_TAG = FrameAddTask.class.getSimpleName();

    private Activity activity;
    private FrameAddImplAdapter adapter;
    private int requestCode;
    private SweetAlertDialog dialog;

    public FrameAddTask(Activity activity, FrameAddImplAdapter adapter, int requestCode) {
        this.activity = activity;
        this.adapter = adapter;
        this.requestCode = requestCode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = Dialogs.showLoadingProgressDialog(activity, R.string.adding_frames);
    }

    @Override
    protected Boolean doInBackground(@NonNull Intent... params) {
        switch (requestCode) {
            case FrameAdder.INTENT_GET_IMAGE:
                adapter.addFrameFromImages(params[0]);
                break;
            case FrameAdder.INTENT_GET_GIF:
                adapter.addFrameFromGif(params[0]);
                break;
            case FrameAdder.INTENT_CAPTURE_VIDEO:
                adapter.addFrameFromVideo(params[0]);
                break;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        dialog.dismissWithAnimation();
    }
}
