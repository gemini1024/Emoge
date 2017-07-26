package com.emoge.app.emoge.ui.correction;

import android.app.Activity;

import com.nightonke.boommenu.BoomButtons.OnBMClickListener;

/**
 * Created by jh on 17. 7. 26.
 */

public class Corrections implements OnBMClickListener {
    private static final String LOG_TAG = Corrections.class.getSimpleName();

    public static final int CORRECT_BRIGHTNESS  = 0;
    public static final int CORRECT_CONSTRACT   = 1;
    public static final int CORRECT_GAMMA       = 2;

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    private Activity activity;

    public Corrections(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onBoomButtonClick(int index) {

    }
}
