package com.emoge.app.emoge.ui.server;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.emoge.app.emoge.R;

/**
 * Created by jh on 17. 8. 4.
 * 서버 category. get category's name by index
 */

class Category {
    private final String storeName;         // 내부 저장소 이름 (Realm)
    private final String[] categoryNames;   // 서버 저장소 이름 (Firebase)

    Category(Resources res) {
        this.storeName = res.getString(R.string.category_store);
        this.categoryNames = res.getStringArray(R.array.server_category);
    }

    @NonNull
    String getCategoryName(int index) {
        if(index == 0) {
            return storeName;
        } else {
            return categoryNames[index-1];
        }
    }

    int getCategoryNum() {
        return categoryNames.length;
    }
}
