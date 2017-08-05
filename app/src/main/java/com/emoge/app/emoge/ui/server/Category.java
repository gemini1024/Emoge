package com.emoge.app.emoge.ui.server;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.emoge.app.emoge.R;

/**
 * Created by jh on 17. 8. 4.
 * 서버 category. get category's name by index
 */

class Category {
    private final String[] categoryNames;
    private final String storeName;

    Category(Resources res) {
        this.categoryNames = res.getStringArray(R.array.server_category);
        this.storeName = res.getString(R.string.category_store);
    }

    @NonNull
    String getCategoryName(int category) {
        if(category >= 0 && category < categoryNames.length) {
            return categoryNames[category];
        } else {
            return storeName;
        }
    }

    int getCategoryNum() {
        return categoryNames.length +1;  // +store
    }
}
