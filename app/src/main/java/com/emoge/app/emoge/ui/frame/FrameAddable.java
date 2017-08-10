package com.emoge.app.emoge.ui.frame;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by jh on 17. 8. 1.
 */

interface FrameAddable {
    boolean addFrameFromImages(@NonNull Intent imageData);
    boolean addFrameFromGif(@NonNull Intent imageData, int maxSize);
    boolean addFrameFromVideo(@NonNull Intent videoData, int maxSize);
}
