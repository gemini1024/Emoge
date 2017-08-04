package com.emoge.app.emoge.utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.emoge.app.emoge.R;

/**
 * Created by jh on 17. 8. 2.
 * 커스텀 다이얼로그 생성 및 제어 (show/cancel/dismiss)
 * 상속한 클래스로만 생성 가능. (innerLayout)
 */

abstract class CustomDialog implements DialogInterface {
    private static final String LOG_TAG = CustomDialog.class.getSimpleName();

    private AlertDialog.Builder builder;
    private Dialog dialog;
    private View innerLayout;

    protected CustomDialog(Activity activity, int layoutRes) {
        builder = new AlertDialog.Builder(activity, R.style.CustomAlertDialog);
        innerLayout = activity.getLayoutInflater().inflate(layoutRes, null);
    }

    View findViewById(int id) {
        return innerLayout.findViewById(id);
    }

    public void show() {
        builder.setView(innerLayout);
        dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void cancel() {
        if(dialog != null) {
            dialog.cancel();
        }
    }

    @Override
    public void dismiss() {
        if(dialog != null) {
            dialog.dismiss();
        }
    }
}
