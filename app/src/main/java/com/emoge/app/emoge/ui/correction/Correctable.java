package com.emoge.app.emoge.ui.correction;

/**
 * Created by jh on 17. 8. 1.
 */

public interface Correctable {
    void setBrightness(int value);
    void setContrast(int value);
    void setGamma(int value);
    void apply();
    void reset();
}
