package com.emoge.app.emoge.model;

/**
 * Created by jh on 17. 8. 5.
 * 움짤 저장시 옵션
 */

public class GifMakingInfo {
    private String category;    // 서버 업로드시 저장될 category
    private String title;       // 움짤 이름 (파일 이름 및 서버 업로드시 짤 제목)
    private int quality;        // 저장 품질 (EditorDialog 에서 선택)
    private int frameDelay;     // 한 프레임이 보여질 시간 (ms)

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
