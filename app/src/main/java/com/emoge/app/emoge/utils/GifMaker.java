package com.emoge.app.emoge.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.emoge.app.emoge.MainApplication;
import com.emoge.app.emoge.encoder.AnimatedGifEncoder;
import com.emoge.app.emoge.model.Frame;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by jh on 17. 7. 14.
 * 이미지(Frame)들을 이용해 GIF 파일 생성
 */

class GifMaker {
    private static final String LOG_TAG = GifMaker.class.getSimpleName();

    @Nullable
    File saveAsGif(@NonNull String title, @NonNull ByteArrayOutputStream gifBos) {
        File file = makeFile(title);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(gifBos.toByteArray());
            return file;
        } catch (IOException e) {
            Log.d(LOG_TAG, e.getClass().getName(), e);
            return null;
        }
    }

    private File makeFile(String wantedFileName) {
        String fileName = wantedFileName+".gif";

        // Directory 생성
        File filePath = new File(MainApplication.defaultDir);
        if(!filePath.isDirectory()){
            filePath.mkdirs();
        }
        // 파일 이름 중복 방지
        int cnt = 2;
        while(new File(MainApplication.defaultDir, fileName).exists()) {
            fileName = String.format("%s (%d).gif", wantedFileName, cnt);
            cnt++;
        }
        return new File(MainApplication.defaultDir, fileName);
    }

    @NonNull
    ByteArrayOutputStream makeGifByImages(@NonNull List<Frame> images, int frameDelay) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setDelay(frameDelay);
        encoder.setRepeat(0);   // 0 : roof, 1 : non-roof
        encoder.start(bos);

        for (Frame frame : images) {
            encoder.addFrame(frame.getBitmap());
            Log.i(LOG_TAG, "added frame");
        }

        encoder.finish();
        Log.d(LOG_TAG, "encoder finish");

        return bos;
    }

}
