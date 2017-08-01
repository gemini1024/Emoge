package com.emoge.app.emoge.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.emoge.app.emoge.encoder.AnimatedGifEncoder;
import com.emoge.app.emoge.model.Frame;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by jh on 17. 7. 14.
 * 이미지(Frame)들을 이용해 GIF 파일 생성
 */

class GifMaker {
    private static final String LOG_TAG = GifMaker.class.getSimpleName();

    boolean saveAsGif(@NonNull ByteArrayOutputStream gifBos) {
        File filePath = new File(Environment.getExternalStorageDirectory().getPath(),
                new Date().getTime()+".gif");
        FileOutputStream outputStream;
        boolean result = false;
        try {
            outputStream = new FileOutputStream(filePath);
            outputStream.write(gifBos.toByteArray());
            result = true;
        } catch (IOException e) {
            Log.d(LOG_TAG, e.getClass().getName(), e);
        }
        return result;
    }


    ByteArrayOutputStream makeGifByImages(List<Frame> images, int frameDelay) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setDelay(frameDelay);
        encoder.setRepeat(0);   // 0 : roof, 1 : non-roof
        encoder.start(bos);

        for (Frame frame : images) {
            encoder.addFrame(frame.getBitmap());
            Log.i(LOG_TAG, "added");
        }

        encoder.finish();
        Log.d(LOG_TAG, "encoder finish");

        return bos;
    }

}
