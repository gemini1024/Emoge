package com.emoge.app.emoge.model;

import com.zomato.photofilters.imageprocessors.Filter;

/**
 * Created by jh on 17. 8. 9.
 * 보정 history.
 */

public class History {
    private int modifiedBrightness;
    private int modifiedContrast;
    private int modifiedGamma;
    private Filter appliedFilter;

    public History(int modifiedBrightness, int modifiedContrast, int modifiedGamma) {
        this.modifiedBrightness = modifiedBrightness;
        this.modifiedContrast = modifiedContrast;
        this.modifiedGamma = modifiedGamma;
    }

    public History(Filter appliedFilter) {
        this.appliedFilter = appliedFilter;
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

    public Filter getAppliedFilter() {
        return appliedFilter;
    }

    public void setModifiedBrightness(int modifiedBrightness) {
        this.modifiedBrightness = modifiedBrightness;
    }

    public void setModifiedContrast(int modifiedContrast) {
        this.modifiedContrast = modifiedContrast;
    }

    public void setModifiedGamma(int modifiedGamma) {
        this.modifiedGamma = modifiedGamma;
    }

    public void setAppliedFilter(Filter appliedFilter) {
        this.appliedFilter = appliedFilter;
    }
}
