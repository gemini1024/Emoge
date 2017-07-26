package com.emoge.app.emoge.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;

/**
 * Created by jh on 17. 7. 26.
 */

public class BitmapConverter {
    @NonNull
    public static byte[] bitmapToByte(@NonNull Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
