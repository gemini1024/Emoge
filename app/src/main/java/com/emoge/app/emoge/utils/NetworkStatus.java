package com.emoge.app.emoge.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 8. 6.
 * 네트워크 상태 확인 후 작업. (데이터 송수신 많을 때)
 */

public class NetworkStatus {
    private static final int NETWORK_NONE    = 5000;
    private static final int NETWORK_WIFI    = 5001;
    private static final int NETWORK_MOBILE  = 5002;


    private static int getNetworkStatus(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_WIFI;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return NETWORK_MOBILE;
            }
        }
        return NETWORK_NONE;
    }


    public static void executeWithCheckingNetwork(@NonNull Activity activity,
                                                  @NonNull final RequireIntentTask task) {
        switch (getNetworkStatus(activity)) {
            case NETWORK_WIFI :
                task.Task();
                break;
            case NETWORK_MOBILE :
                final SweetAlertDialog dialog = SweetDialogs.showWarningMobileNetworkDialog(activity);
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        task.Task();
                        dialog.dismissWithAnimation();
                    }
                }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.cancel();
                    }
                });
                break;
            case NETWORK_NONE :
                SweetDialogs.showErrorDialog(activity, R.string.check_network, R.string.non_network);
                break;
        }
    }

    public interface RequireIntentTask {
        void Task();
    }

}
