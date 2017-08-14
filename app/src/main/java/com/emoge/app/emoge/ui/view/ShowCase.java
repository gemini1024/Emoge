package com.emoge.app.emoge.ui.view;

import android.app.Activity;
import android.os.Handler;

import com.emoge.app.emoge.MainApplication;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.ui.gallery.GalleryActivity;
import com.emoge.app.emoge.ui.palette.MainActivity;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;

/**
 * Created by jh on 17. 8. 14.
 * 앱 최초 실행시 설명서
 */

public class ShowCase {

    private static final String PREF_SHOWN_COUNT_NAME   = "showcase";
    private static final int SHOWCASE_DELAY             = 600;

    private static final int SHOWCASE_NOT_SHOWN         = 5000;
    private static final int SHOWCASE_GALLERY           = 5001;
    private static final int SHOWCASE_FRAME_ADD         = 5002;
    private static final int SHOWCASE_CORRECT           = 5003;

    private static int getShownShowCaseCount() {
        return MainApplication.getSharedPreferences().getInt(PREF_SHOWN_COUNT_NAME, SHOWCASE_NOT_SHOWN);
    }

    private static void setShownShowCaseCount(int value) {
        MainApplication.getSharedPreferences().edit().putInt(PREF_SHOWN_COUNT_NAME, value).apply();
    }

    private static FancyShowCaseView buildShowCaseView(Activity activity, int viewRes, int titleRes, FocusShape focusShape) {
        return new FancyShowCaseView.Builder(activity)
                .focusOn(activity.findViewById(viewRes))
                .focusShape(focusShape)
                .title(activity.getString(titleRes))
                .build();
    }

    public static void startShowCase(final GalleryActivity activity) {
        if (getShownShowCaseCount() < SHOWCASE_GALLERY) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    buildShowCaseView(activity, R.id.gallery_bt_making, R.string.showcase_making_gif, FocusShape.CIRCLE).show();
                }
            }, SHOWCASE_DELAY);
            setShownShowCaseCount(SHOWCASE_GALLERY);
        }
    }


    public static void startShowCase(final MainActivity activity) {
        if (getShownShowCaseCount() < SHOWCASE_FRAME_ADD) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new FancyShowCaseQueue()
                            .add(buildShowCaseView(activity, R.id.main_frame_list, R.string.showcase_add_frame, FocusShape.ROUNDED_RECTANGLE))
                            .add(buildShowCaseView(activity, R.id.toolbar_next, R.string.showcase_show_correct, FocusShape.CIRCLE))
                            .show();
                }
            }, SHOWCASE_DELAY);
            setShownShowCaseCount(SHOWCASE_FRAME_ADD);
        }
    }

    public static void startCorrectShowCase(final MainActivity activity) {
        if (getShownShowCaseCount() < SHOWCASE_CORRECT) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new FancyShowCaseQueue()
                            .add(buildShowCaseView(activity, R.id.main_palette_tab, R.string.showcase_correcting, FocusShape.ROUNDED_RECTANGLE))
                            .add(buildShowCaseView(activity, R.id.main_bt_correction, R.string.showcase_correcting_filter, FocusShape.CIRCLE))
                            .add(buildShowCaseView(activity, R.id.main_bt_history, R.string.showcase_history, FocusShape.CIRCLE))
                            .add(buildShowCaseView(activity, R.id.toolbar_save, R.string.showcase_save_gif, FocusShape.CIRCLE))
                            .show();
                }
            }, SHOWCASE_DELAY);
            setShownShowCaseCount(SHOWCASE_CORRECT);
        }
    }
}
