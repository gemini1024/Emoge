package com.emoge.app.emoge.model;

/**
 * Created by jh on 17. 8. 13.
 */

public class License {
    private String title;
    private String content;

    public License(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
