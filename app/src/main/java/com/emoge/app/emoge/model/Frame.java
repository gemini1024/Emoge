package com.emoge.app.emoge.model;

/**
 * Created by jh on 17. 7. 25.
 */

public class Frame {
    long id;            // 프레임별 고유 id
    Object image;       // 실질 이미지

    public Frame(long id, Object image) {
        this.id = id;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public Object getImage() {
        return image;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Frame) {
            return this.id == ((Frame)obj).getId();
        }
        return super.equals(obj);
    }
}
