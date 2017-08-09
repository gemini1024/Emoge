package com.emoge.app.emoge.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jh on 17. 8. 9.
 * 담아가기 용.
 */

public class MyStoreGif extends RealmObject {
    // intent title
    public static final String KEY_FIELD        = "id";

    @PrimaryKey
    private String id;            // id ( For db )
    private String title;       // 움짤 이름 (파일 이름 및 서버 업로드시 짤 제목)
    private String downloadUrl; // 움짤 download 할 수 있는 URL

    public MyStoreGif() {
    }

    public MyStoreGif(String title, String downloadUrl) {
        this.title = title;
        this.downloadUrl = downloadUrl;
    }

    public void setAll(MyStoreGif myStoreGif) {
        this.title = myStoreGif.getTitle();
        this.downloadUrl = myStoreGif.getDownloadUrl();
    }

    public void setAll(StoreGif storeGif) {
        this.title = storeGif.getTitle();
        this.downloadUrl = storeGif.getDownloadUrl();
    }

    public String getTitle() {
        return title;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
