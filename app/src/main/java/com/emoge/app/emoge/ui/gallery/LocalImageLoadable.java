package com.emoge.app.emoge.ui.gallery;

import java.io.File;

/**
 * Created by jh on 17. 8. 11.
 * 단말 내부 이미지 가져와서 adapter 에 추가 가능
 */

interface LocalImageLoadable {
    boolean isEmpty();
    boolean addItemWithoutNotify(File file);
    void notifyDataSetChanged();
}
