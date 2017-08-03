package com.emoge.app.emoge.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by jh on 17. 8. 3.
 */

@IgnoreExtraProperties
public class StoreGif {
    public String title;
    public String downloadUrl;
    public int favorite;

    public StoreGif() {
    }

    public StoreGif(String title, String downloadUrl, int favorite) {
        this.title = title;
        this.downloadUrl = downloadUrl;
        this.favorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
