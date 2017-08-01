package com.emoge.app.emoge.ui.correction;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.ui.palette.PaletteFragment;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubfilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 17. 7. 26.
 */

public class Correcter implements OnBMClickListener {
    private static final String LOG_TAG = Correcter.class.getSimpleName();

    public static final int MAIN_PALETTE        = 10;
    public static final int CORRECT_BRIGHTNESS  = 0;
    public static final int CORRECT_CONTRAST    = 1;
    public static final int CORRECT_GAMMA       = 2;


    public static final int DEFAULT_FPS         = 650;
    public static final int DEFAULT_BRIGHTNESS  = 0;
    public static final int DEFAULT_CONTRAST    = 100;
    public static final int DEFAULT_GAMMA       = 128;

    private int currentFps;
    private int currentBrightness;
    private int currentContrast;
    private int currentGamma;


    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
        System.loadLibrary("NativeImageProcessor");
    }

    private AppCompatActivity activity;


    public Correcter(@NonNull AppCompatActivity activity) {
        this.activity           = activity;
        this.currentFps         = DEFAULT_FPS;
        this.currentBrightness  = DEFAULT_BRIGHTNESS;
        this.currentContrast    = DEFAULT_CONTRAST;
        this.currentGamma       = DEFAULT_GAMMA;
    }

    @Override
    public void onBoomButtonClick(int index) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_palette_container,
                PaletteFragment.newInstance(index, getCurrentValueByType(index)));
        if(fragmentManager.getBackStackEntryCount() == 0) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public int getCurrentFps() {
        return currentFps;
    }

    public void setCurrentFps(int currentFps) {
        this.currentFps = currentFps;
    }

    public int getCurrentValueByType(int type) {
        switch (type) {
            case CORRECT_BRIGHTNESS :
                return currentBrightness;
            case CORRECT_CONTRAST :
                return currentContrast;
            case CORRECT_GAMMA :
                return currentGamma;
            default:
                return currentFps;
        }
    }

    public List<Frame> setBrightness(List<Frame> frames, int value) {
        ArrayList<Frame> stageFrames = new ArrayList<>();
        Filter brightFilter = new Filter();
        brightFilter.addSubFilter(new BrightnessSubfilter(value));
        for(Frame frame : frames) {
            stageFrames.add(new Frame(frame.getId(),
                    brightFilter.processFilter(frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true))));
            Log.d(LOG_TAG, "apply");
        }
        currentBrightness = value;
        Log.d(LOG_TAG, String.valueOf(value));
        return stageFrames;
    }


    public List<Frame> setContrast(List<Frame> frames, int value) {
        ArrayList<Frame> stageFrames = new ArrayList<>();
        Filter contrastFilter = new Filter();
        contrastFilter.addSubFilter(new ContrastSubfilter((float)(value)/100.0f));
        for(Frame frame : frames) {
            stageFrames.add(new Frame(frame.getId(),
                    contrastFilter.processFilter(frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true))));
        }
        currentContrast = value;
        return stageFrames;
    }


    public List<Frame> setGamma(List<Frame> frames, int value) {
        ArrayList<Frame> stageFrames = new ArrayList<>();
        Filter gammaFilter = new Filter();
        Point[] rgbKnots;
        rgbKnots = new Point[3];
        rgbKnots[0] = new Point(0, 0);
        rgbKnots[1] = new Point(value, value);
        rgbKnots[2] = new Point(255, 255);
        gammaFilter.addSubFilter(new ToneCurveSubfilter(rgbKnots, null, null, null));
        for(Frame frame : frames) {
            stageFrames.add(new Frame(frame.getId(),
                    gammaFilter.processFilter(frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true))));
        }
        currentBrightness = value;
        return stageFrames;
    }


}
