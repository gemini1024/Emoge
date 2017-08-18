package com.emoge.app.emoge.ui.correction;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.ui.palette.PaletteFragment;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubfilter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 17. 7. 26.
 * 보정. ( Recycle 안함! )
 * Bitmap -> Bitmap or Bitmap list -> Bitmap list
 * Frame -> Frame or Frame list -> Frame list
 */

public class Correcter
        implements OnBMClickListener, TabLayout.OnTabSelectedListener, BitmapModifiable {
    private static final String LOG_TAG = Correcter.class.getSimpleName();

    // 보정 타입 (Fragment 존재)
    public static final int MOD_FRAME_DELAY     = 0;
    public static final int CORRECT_BRIGHTNESS  = 1;
    public static final int CORRECT_CONTRAST    = 2;
    public static final int CORRECT_GAMMA       = 3;

    // 보정 타입 (명령형. Fragment 존재 X)
    public static final int CORRECT_REVERSE     = 100;
    public static final int CORRECT_APPLY       = 101;

    // 기본값 (For Fragment's SeekBar)
    public static final int DEFAULT_FRAME_DELAY = 500;
    public static final int DEFAULT_BRIGHTNESS  = 0;
    public static final int DEFAULT_CONTRAST    = 100;
    public static final int DEFAULT_GAMMA       = 128;


    // 필터
    public static final int FILTER_STAR     = 0;
    public static final int FILTER_BLUE     = 1;
    public static final int FILTER_STUCK    = 2;
    public static final int FILTER_LIME     = 3;
    public static final int FILTER_NIGHT    = 4;


    // NDK Libraries
    static {
//        System.loadLibrary("native-lib");
        System.loadLibrary("NativeImageProcessor");
    }

    private AppCompatActivity activity;     // 호출한 Activity (SupportFragment 생성)
    private int currentDelay;               // 현재 FPS (재생 속도)

    public Correcter(@NonNull AppCompatActivity activity) {
        this.activity   = activity;
        this.currentDelay = DEFAULT_FRAME_DELAY;
    }

    // 해당 Palette(보정을 위한 Fragment) 생성
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int index = tab.getPosition();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.popup_enter, R.anim.popup_exit, R.anim.popup_enter, R.anim.popup_exit);
        fragmentTransaction.replace(R.id.main_palette_container,
                PaletteFragment.newInstance(index, getDefaultValueByType(index)));
        fragmentTransaction.commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    // EventBus 로 해당 필터 보냄
    @Override
    public void onBoomButtonClick(int index) {
        switch (index) {
            case FILTER_STAR :
                EventBus.getDefault().post(SampleFilters.getStarLitFilter());
                break;
            case FILTER_BLUE :
                EventBus.getDefault().post(SampleFilters.getBlueMessFilter());
                break;
            case FILTER_STUCK :
                EventBus.getDefault().post(SampleFilters.getAweStruckVibeFilter());
                break;
            case FILTER_LIME :
                EventBus.getDefault().post(SampleFilters.getLimeStutterFilter());
                break;
            default :
                EventBus.getDefault().post(SampleFilters.getNightWhisperFilter());
                break;
        }
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
                return currentDelay;
        }
    }

    // FPS 변경
    @Override
    public int getCurrentDelay() {
        return currentDelay;
    }

    @Override
    public void setCurrentDelay(int currentDelay) {
        this.currentDelay = currentDelay;
    }


    // 밝기 변경
    @Override @NonNull
    public List<Frame> setBrightness(@NonNull List<Frame> frames, int value) {
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
    @Override @NonNull
    public List<Frame> setContrast(@NonNull List<Frame> frames, int value) {
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
    @Override @NonNull
    public List<Frame> setGamma(@NonNull List<Frame> frames, int value) {
        ArrayList<Frame> stageFrames = new ArrayList<>();
        Point[] rgbKnots;
        rgbKnots = new Point[3];
        rgbKnots[0] = new Point(0, 0);
        rgbKnots[1] = new Point(255-value, value);
        rgbKnots[2] = new Point(255, 255);
        Filter gammaFilter = new Filter();
        gammaFilter.addSubFilter(new ToneCurveSubfilter(rgbKnots, null, null, null));
        for(Frame frame : frames) {
            stageFrames.add(new Frame(frame.getId(),
                    gammaFilter.processFilter(frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true))));
        }
        return stageFrames;
    }

    // 필터 적용
    @Override @NonNull
    public List<Frame> setFilter(@NonNull List<Frame> frames, Filter filter) {
        ArrayList<Frame> stageFrames = new ArrayList<>();
        for(Frame frame : frames) {
            stageFrames.add(new Frame(frame.getId(),
                    filter.processFilter(frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true))));
        }
        return stageFrames;
    }
}
