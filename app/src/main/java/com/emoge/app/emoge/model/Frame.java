package com.emoge.app.emoge.model;

import android.graphics.Bitmap;

/**
 * Created by jh on 17. 7. 25.
 * 프레임(Gif 구성 이미지) 데이터
 */

public class Frame {
    long id;            // 프레임별 고유 id
    Bitmap bitmap;      // 실질 이미지

    public Frame(long id, Bitmap bitmap) {
        this.id = id;
        this.bitmap = bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public long getId() {
        return id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    // For Draggable Recycler View
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Frame) {
            return this.id == ((Frame)obj).getId();
        }
        return super.equals(obj);
    }
}
