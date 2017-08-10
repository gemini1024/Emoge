package com.emoge.app.emoge.ui.frame;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 8. 1.
 * 파일에서 Frame 추가 작업 ( Background )
 */

public class FrameAddTask extends AsyncTask<Intent, Void, Boolean> {
    private final String LOG_TAG = FrameAddTask.class.getSimpleName();

    private Activity activity;
    private RecyclerView framesView;
    private FrameAddImplAdapter adapter;
    private int requestCode;
    private SweetAlertDialog dialog;

    public FrameAddTask(Activity activity, RecyclerView framesView,
                        FrameAddImplAdapter adapter, int requestCode) {
        this.activity = activity;
        this.framesView = framesView;
        this.adapter = adapter;
        this.requestCode = requestCode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = SweetDialogs.showLoadingProgressDialog(activity, R.string.adding_frames);
    }

    @Override
    protected Boolean doInBackground(@NonNull Intent... params) {
        switch (requestCode) {
            case FrameAdder.INTENT_GET_IMAGE:
                return adapter.addFrameFromImages(params[0]);
            case FrameAdder.INTENT_GET_GIF:
                return adapter.addFrameFromGif(params[0], adapter.getMaxItemSize()-adapter.getItemCount()+1);
            case FrameAdder.INTENT_CAPTURE_VIDEO:
                return adapter.addFrameFromVideo(params[0], adapter.getMaxItemSize()-adapter.getItemCount()+1);
            default:
                return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Log.i(LOG_TAG, String.valueOf(aBoolean));
        dialog.dismissWithAnimation();
        if(aBoolean) {
            framesView.scrollToPosition(adapter.getItemCount()-1);
        } else if(requestCode == FrameAdder.INTENT_GET_IMAGE) {
            SweetDialogs.showErrorDialog(activity, R.string.err_add_file_title, R.string.err_add_file_content);
        } else {
            SweetDialogs.showErrorDialog(activity, R.string.err_add_frames_title, R.string.err_add_frames_content);
        }
    }
}
