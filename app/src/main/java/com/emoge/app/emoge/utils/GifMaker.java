package com.emoge.app.emoge.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.emoge.app.emoge.MainApplication;
import com.emoge.app.emoge.encoder.AnimatedGifEncoder;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.model.GifMakingInfo;
import com.waynejo.androidndkgif.GifEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by jh on 17. 7. 14.
 * 이미지(Frame)들을 이용해 GIF 파일 생성
 */

class GifMaker {
    private static final String LOG_TAG = GifMaker.class.getSimpleName();

    private static final String FILE_NAME_FORMAT = "%s (%d).gif";

    private static final int QUALITY_LOW = 0;
    private static final int QUALITY_MEDIUM = 1;
    private static final int QUALITY_HIGH = 2;
    private static final int QUALITY_SUPER_HIGH = 3;


    // 초고화질 - AnimatedGifEncoder
    // 저~고화질 - GifEncoder(Android NDK Gif)  (속도가 빠르지만 화질에 한계가 있음)
    @Nullable
    File saveAsGif(@NonNull List<Frame> images, @NonNull GifMakingInfo info) {
        if(QUALITY_SUPER_HIGH == info.getQuality()) {
            return saveAsGif(info.getTitle(), images, info.getFrameDelay());
        } else {
            return saveAsGif(info.getTitle(), images, info.getFrameDelay(),
                    getEncodingTypeByQuality(info.getQuality()));
        }
    }

    // position (int) -> quality type (enum)
    @NonNull
    private GifEncoder.EncodingType getEncodingTypeByQuality(int quality) {
        switch (quality) {
            case QUALITY_HIGH :
                return GifEncoder.EncodingType.ENCODING_TYPE_STABLE_HIGH_MEMORY;
            case QUALITY_LOW :
                return GifEncoder.EncodingType.ENCODING_TYPE_SIMPLE_FAST;
            default:
                return GifEncoder.EncodingType.ENCODING_TYPE_NORMAL_LOW_MEMORY;
        }
    }


    // using GifEncoder(Android NDK Gif)
    @Nullable
    private File saveAsGif(@NonNull String title, @NonNull List<Frame> images,
                           int frameDelay, @NonNull GifEncoder.EncodingType encodingType) {
        File file = makeFile(title);
        GifEncoder encoder = new GifEncoder();

        try {
            encoder.init(images.get(0).getBitmap().getWidth(), images.get(0).getBitmap().getHeight(),
                    file.getPath(), encodingType);

            for(Frame frame : images) {
                encoder.encodeFrame(frame.getBitmap(), frameDelay);
                Logger.i(LOG_TAG, "added frame");
            }
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            encoder.close();
            Logger.d(LOG_TAG, "encoder finish");
        }
    }


    // using AnimatedGifEncoder
    @Nullable
    private File saveAsGif(@NonNull String title, @NonNull List<Frame> images, int frameDelay) {
        ByteArrayOutputStream gifBos = makeGifByImages(images, frameDelay);
        File file = makeFile(title);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(gifBos.toByteArray());
            return file;
        } catch (IOException e) {
            Logger.e(LOG_TAG, e);
            return null;
        }
    }

    @NonNull
    private ByteArrayOutputStream makeGifByImages(@NonNull List<Frame> images, int frameDelay) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setDelay(frameDelay);
        encoder.setRepeat(0);   // 0 : roof, 1 : non-roof
        encoder.start(bos);

        for (Frame frame : images) {
            encoder.addFrame(frame.getBitmap());
            Logger.i(LOG_TAG, "added frame");
        }

        encoder.finish();
        Logger.d(LOG_TAG, "encoder finish");

        return bos;
    }



    // Directory 생성 및 덮어쓰기 방지
    File makeFile(String wantedFileName) {
        String fileName = wantedFileName+".gif";

        // Directory 생성
        File filePath = new File(MainApplication.defaultDir);
        if(!filePath.isDirectory()){
            filePath.mkdirs();
        }
        // 파일 이름 중복 방지
        int cnt = 2;
        while(new File(MainApplication.defaultDir, fileName).exists()) {
            fileName = String.format(FILE_NAME_FORMAT, wantedFileName, cnt);
            cnt++;
        }
        return new File(MainApplication.defaultDir, fileName);
    }
}
