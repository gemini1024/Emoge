package com.emoge.app.emoge.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;

import com.emoge.app.emoge.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 7. 31.
 * Dialog 여러 군데서 쓰기 위함.
 */

public class Dialogs {
    @NonNull
    public static SweetAlertDialog showLoadingProgressDialog(@NonNull Activity activity,
                                                             int stringRes) {
        SweetAlertDialog progressDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(
                ResourcesCompat.getColor(activity.getResources(), R.color.colorProgress, null));
        progressDialog.setTitleText(activity.getString(stringRes))
                .setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    @NonNull
    public static SweetAlertDialog showSuccessDialog(@NonNull Activity activity,
                                                     int stringTitleRes,
                                                     int stringContentRes) {
        SweetAlertDialog successDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(activity.getString(stringTitleRes))
                .setContentText(activity.getString(stringContentRes));
        successDialog.show();
        return successDialog;
    }


    @NonNull
    public static SweetAlertDialog showErrorDialog(@NonNull Activity activity,
                                                     int stringTitleRes,
                                                     int stringContentRes) {
        SweetAlertDialog errorDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(activity.getString(stringTitleRes))
                .setContentText(activity.getString(stringContentRes));
        errorDialog.show();
        return errorDialog;
    }

    @NonNull
    public static SweetAlertDialog showExitDialog(@NonNull final Activity activity) {
        SweetAlertDialog exitDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(activity.getString(R.string.exit))
                .setContentText(activity.getString(R.string.exit_content))
                .setCancelText(activity.getString(R.string.cancel))
                .setConfirmText(activity.getString(R.string.exit))
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        activity.finish();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                });
        exitDialog.show();
        return exitDialog;
    }


    @NonNull
    public static CustomDialogController showImageDialog(@NonNull Activity activity,
                                                         @NonNull Bitmap bitmap) {
        AlertDialog.Builder ab = new AlertDialog.Builder(activity, R.style.CustomAlertDialog);
        CustomDialogController controller = new CustomDialogController(activity);
        controller.setImage(bitmap);
        controller.build(ab);
        controller.show();
        return controller;
    }

    @NonNull
    public static CustomDialogController showImageDialog(@NonNull Activity activity,
                                                            @NonNull Uri uri) {
        AlertDialog.Builder ab = new AlertDialog.Builder(activity, R.style.CustomAlertDialog);
        CustomDialogController controller = new CustomDialogController(activity);
        controller.setImage(activity, uri);
        controller.build(ab);
        controller.show();
        return controller;
    }

}
