package com.emoge.app.emoge.ui.correction;

import android.support.annotation.NonNull;

import com.emoge.app.emoge.model.Frame;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.List;

/**
 * Created by jh on 17. 8. 11.
 * Bitmap Image 변경 가능
 */

interface BitmapModifiable {
    int getCurrentDelay();
    void setCurrentDelay(int currentDelay);
    @NonNull List<Frame> setBrightness(@NonNull List<Frame> frames, int value);
    @NonNull List<Frame> setContrast(@NonNull List<Frame> frames, int value);
    @NonNull List<Frame> setGamma(@NonNull List<Frame> frames, int value);
    @NonNull List<Frame> setFilter(@NonNull List<Frame> frames, Filter filter);
}
