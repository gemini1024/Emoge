package com.emoge.app.emoge.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.emoge.app.emoge.encoder.AnimatedGifEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by jh on 17. 7. 14.
 * 이미지들을 이용해 GIF 파일 생성
 */

public class GifMaker {
    private static final String LOG_TAG = GifMaker.class.getSimpleName();

    public boolean saveAsGif(@NonNull ByteArrayOutputStream gifBos) {
        File filePath = new File(Environment.getExternalStorageDirectory().getPath(),
                new Date().getTime()+".gif");
        FileOutputStream outputStream;
        boolean result = false;
        try {
            outputStream = new FileOutputStream(filePath);
            outputStream.write(gifBos.toByteArray());
            result = true;
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG, e.getClass().getName(), e);
        } catch (IOException e) {
            Log.d(LOG_TAG, e.getClass().getName(), e);
        }
        return result;
    }


    // TODO : 불러온 이미지로 GIF 생성
    public ByteArrayOutputStream makeGifByImages(Resources res, int[] resourceIds, int frameDelay) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setDelay(frameDelay);
        encoder.setRepeat(0);   // 0 : roof, 1 : non-roof
        encoder.start(bos);

        for (int imgId : resourceIds) {
            Bitmap bitmap = loadBitmapSampleSize(res, imgId, 100, 100);
            Log.i(LOG_TAG, "loaded");
            encoder.addFrame(bitmap);
            Log.i(LOG_TAG, "added");
            bitmap.recycle();
        }

        encoder.finish();
        Log.d(LOG_TAG, "encoder finish");

        return bos;
    }


    private Bitmap loadBitmapSampleSize(Resources res, int resourceId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resourceId, options);

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeResource(res, resourceId, options);
    }


}
