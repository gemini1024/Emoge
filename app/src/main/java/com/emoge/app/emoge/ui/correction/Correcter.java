package com.emoge.app.emoge.ui.correction;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

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
 * 보정.
 * Bitmap -> Bitmap or Bitmap list -> Bitmap list
 * Frame -> Frame or Frame list -> Frame list
 */

public class Correcter implements OnBMClickListener {
    private static final String LOG_TAG = Correcter.class.getSimpleName();

    // 보정 타입 (Fragment 존재)
    public static final int CORRECT_BRIGHTNESS  = 0;
    public static final int CORRECT_CONTRAST    = 1;
    public static final int CORRECT_GAMMA       = 2;
    public static final int MAIN_PALETTE        = 10;

    // 보정 타입 (명령형. Fragment 존재 X)
    public static final int CORRECT_REVERSE     = 100;
    public static final int CORRECT_APPLY       = 101;
    public static final int CORRECT_RESET       = 102;

    // 기본값 (For Fragment's SeekBar)
    private static final int DEFAULT_FPS         = 650;
    private static final int DEFAULT_BRIGHTNESS  = 0;
    private static final int DEFAULT_CONTRAST    = 100;
    private static final int DEFAULT_GAMMA       = 128;


    // NDK Libraries
    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
        System.loadLibrary("NativeImageProcessor");
    }

    private AppCompatActivity activity;     // 호출한 Activity (SupportFragment 생성)
    private int currentFps;                 // 현재 FPS (재생 속도)

    public Correcter(@NonNull AppCompatActivity activity) {
        this.activity   = activity;
        this.currentFps = DEFAULT_FPS;
    }

    // 해당 Palette(보정을 위한 Fragment) 생성
    @Override
    public void onBoomButtonClick(int index) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_palette_container,
                PaletteFragment.newInstance(index, getDefaultValueByType(index)));
        if(fragmentManager.getBackStackEntryCount() == 0) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    // 각 보정 타입 별 기본값
    private int getDefaultValueByType(int type) {
        switch (type) {
            case CORRECT_BRIGHTNESS :
                return DEFAULT_BRIGHTNESS;
            case CORRECT_CONTRAST :
                return DEFAULT_CONTRAST;
            case CORRECT_GAMMA :
                return DEFAULT_GAMMA;
            default :
                return currentFps;
        }
    }

    // FPS 변경
    public int getCurrentFps() {
        return currentFps;
    }

    void setCurrentFps(int currentFps) {
        this.currentFps = currentFps;
    }


    // 밝기 변경
    List<Frame> setBrightness(List<Frame> frames, int value) {
        ArrayList<Frame> stageFrames = new ArrayList<>();
        Filter brightFilter = new Filter();
        brightFilter.addSubFilter(new BrightnessSubfilter(value));
        for(Frame frame : frames) {
            stageFrames.add(new Frame(frame.getId(),
                    brightFilter.processFilter(frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true))));
        }
        return stageFrames;
    }

    // 대비 변경
    List<Frame> setContrast(List<Frame> frames, int value) {
        ArrayList<Frame> stageFrames = new ArrayList<>();
        Filter contrastFilter = new Filter();
        contrastFilter.addSubFilter(new ContrastSubfilter((float)(value)/100.0f));
        for(Frame frame : frames) {
            stageFrames.add(new Frame(frame.getId(),
                    contrastFilter.processFilter(frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true))));
        }
        return stageFrames;
    }

    // 감마 변경
    List<Frame> setGamma(List<Frame> frames, int value) {
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
        return stageFrames;
    }

}
