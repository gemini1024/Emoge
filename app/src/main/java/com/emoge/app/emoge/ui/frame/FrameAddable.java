package com.emoge.app.emoge.ui.frame;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by jh on 17. 8. 1.
 */

public interface FrameAddable {
    void addFrameFromImages(@NonNull Intent imageData);
    void addFrameFromGif(@NonNull Intent imageData);
    void addFrameFromVideo(@NonNull Intent videoData);
}
