package com.emoge.app.emoge.model;

/**
 * Created by jh on 17. 8. 9.
 * 보정 history.
 */

public class History {
    private int modifiedBrightness;
    private int modifiedContrast;
    private int modifiedGamma;

    public History(int modifiedBrightness, int modifiedContrast, int modifiedGamma) {
        this.modifiedBrightness = modifiedBrightness;
        this.modifiedContrast = modifiedContrast;
        this.modifiedGamma = modifiedGamma;
    }

    public int getModifiedBrightness() {
        return modifiedBrightness;
    }

    public int getModifiedContrast() {
        return modifiedContrast;
    }

    public int getModifiedGamma() {
        return modifiedGamma;
    }

}
