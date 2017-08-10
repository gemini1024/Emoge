package com.emoge.app.emoge.ui.correction;

import com.zomato.photofilters.imageprocessors.Filter;

/**
 * Created by jh on 17. 8. 1.
 */

interface Correctable {
    void setFps(int value);
    void setBrightness(int value);
    void setContrast(int value);
    void setGamma(int value);
    void setFilter(Filter filter);
    void apply();
}
