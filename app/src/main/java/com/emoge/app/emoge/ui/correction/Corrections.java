package com.emoge.app.emoge.ui.correction;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.ui.palette.PaletteFragment;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;

/**
 * Created by jh on 17. 7. 26.
 */

public class Corrections implements OnBMClickListener {
    private static final String LOG_TAG = Corrections.class.getSimpleName();

    public static final int MAIN_PALETTE        = 10;
    public static final int CORRECT_BRIGHTNESS  = 0;
    public static final int CORRECT_CONTRAST    = 1;
    public static final int CORRECT_GAMMA       = 2;

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    private AppCompatActivity activity;


    public Corrections(@NonNull AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onBoomButtonClick(int index) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_palette_container, PaletteFragment.newInstance(index))
                .addToBackStack(null)
                .commit();
    }
}
