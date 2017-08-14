package com.emoge.app.emoge.utils.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;

import com.emoge.app.emoge.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 7. 31.
 * SweetAlertDialog 여러 군데서 쓰기 위함.
 */

public class SweetDialogs {
    private static final String LOG_TAG = SweetDialogs.class.getSimpleName();

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
    public static SweetAlertDialog showWarningDialog(@NonNull Activity activity,
                                                   int stringTitleRes,
                                                   int stringContentRes) {
        SweetAlertDialog errorDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(activity.getString(stringTitleRes))
                .setContentText(activity.getString(stringContentRes))
                .setCancelText(activity.getString(R.string.cancel))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                });
        errorDialog.show();
        return errorDialog;
    }

    @NonNull
    public static SweetAlertDialog showExitMakingDialog(@NonNull final Activity activity) {
        SweetAlertDialog exitDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(activity.getString(R.string.stop_making))
                .setContentText(activity.getString(R.string.stop_making_content))
                .setCancelText(activity.getString(R.string.cancel))
                .setConfirmText(activity.getString(R.string.stop_making))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                });
        exitDialog.setCancelable(false);
        exitDialog.show();
        return exitDialog;
    }

    @NonNull
    public static SweetAlertDialog showExitAppDialog(@NonNull final Activity activity) {
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
        exitDialog.setCancelable(false);
        exitDialog.show();
        return exitDialog;
    }


    @NonNull
    public static SweetAlertDialog showWarningMobileNetworkDialog(@NonNull final Activity activity) {
        SweetAlertDialog exitDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(activity.getString(R.string.check_network))
                .setContentText(activity.getString(R.string.warning_mobile_network))
                .setCancelText(activity.getString(R.string.cancel))
                .setConfirmText(activity.getString(R.string.continue_work))
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                });
        exitDialog.setCancelable(false);
        exitDialog.show();
        return exitDialog;
    }
}
