package com.emoge.app.emoge.ui.frame;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by jh on 17. 8. 1.
 */

interface FrameAddable {
    void addFrameFromImages(@NonNull Intent imageData);
    void addFrameFromGif(@NonNull Intent imageData, int maxSize);
    void addFrameFromVideo(@NonNull Intent videoData, int maxSize);
}
