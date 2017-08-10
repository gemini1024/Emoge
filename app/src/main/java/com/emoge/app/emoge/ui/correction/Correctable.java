package com.emoge.app.emoge.ui.correction;

import android.support.annotation.NonNull;

import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.model.History;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.List;

/**
 * Created by jh on 17. 8. 1.
 */

public interface Correctable {
    // 보정
    void correct(int type, int value);
    void setFps(int value);
    void setBrightness(int value);
    void setContrast(int value);
    void setGamma(int value);
    void setFilter(Filter filter);
    void apply();
    // 보정 adapter 조작
    @NonNull List<Frame> getFrames();
    @NonNull History getModifiedValues();
    void clearPreviousFrames();
    boolean addItem(@NonNull Frame item);
    void clear();

}
