package com.emoge.app.emoge.model;

/**
 * Created by jh on 17. 8. 5.
 */

public class GifMakingInfo {
    private String category;
    private String title;
    private int quality;
    private int frameDelay;

    public GifMakingInfo(String category, String title, int quality, int frameDelay) {
        this.category = category;
        this.title = title;
        this.quality = quality;
        this.frameDelay = frameDelay;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getFrameDelay() {
        return frameDelay;
    }

    public void setFrameDelay(int frameDelay) {
        this.frameDelay = frameDelay;
    }
}
