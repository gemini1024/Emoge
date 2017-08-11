package com.emoge.app.emoge.ui.frame;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by jh on 17. 8. 11.
 * 단말 내부 이미지 접근 가능
 */

public interface LocalImageAccessible {
    @NonNull Bitmap loadBitmapSampleSize(@NonNull Uri imageUri) throws IOException;
    @NonNull List<Bitmap> loadBitmapsFromGif(@NonNull Uri imageUri, int maxSize) throws FileNotFoundException;
    @NonNull List<Bitmap> captureVideo(@NonNull Uri videoUri, int maxSize, int startSec, int count, int fps);
}
