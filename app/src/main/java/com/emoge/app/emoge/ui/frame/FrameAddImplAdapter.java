package com.emoge.app.emoge.ui.frame;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.emoge.app.emoge.model.Frame;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by jh on 17. 8. 1.
 * 파일에서 이미지를 추가할 수 있는 어댑터
 */

public class FrameAddImplAdapter extends FrameAdapter implements FrameAddable {
    private static final String LOG_TAG = FrameAddImplAdapter.class.getSimpleName();

    private FrameAdder frameAdder;      // 파일 접근 처리

    public FrameAddImplAdapter(@NonNull RecyclerView recyclerView,
                               @NonNull List<Frame> frames,
                               @NonNull FrameAdder frameAdder) {
        super(recyclerView, frames);
        this.frameAdder = frameAdder;
    }


    @Override
    public void addFrameFromImages(@NonNull Intent imageData) {
        try {
            Uri singleImageUri = imageData.getData();
            if( singleImageUri != null ) {
                // single image
                addItem(new Frame(getItemCount(), frameAdder.loadBitmapSampleSize(singleImageUri)));
            } else if( imageData.getClipData() != null ) {
                // multiple image
                ClipData clipData = imageData.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    addItem(new Frame(getItemCount(), frameAdder.loadBitmapSampleSize(clipData.getItemAt(i).getUri())));
                }
            }
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, e.getClass().getName(), e);
        }
    }

    @Override
    public void addFrameFromGif(@NonNull Intent imageData) {
        try {
            Uri imageUri = imageData.getData();
            if( imageUri != null ) {
                List<Bitmap> bitmaps = frameAdder.loadBitmapsFromGif(imageUri);
                for(Bitmap bitmap : bitmaps) {
                    addItem(new Frame(getItemCount(), bitmap));
                }
            }
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, e.getClass().getName(), e);
        }
    }

    @Override
    public void addFrameFromVideo(@NonNull Intent videoData) {
        if(videoData.getData() != null) {
            List<Bitmap> bitmaps = frameAdder.captureVideo(videoData.getData(),
                    videoData.getIntExtra("startSec", 0),
                    videoData.getIntExtra("count", 0),
                    videoData.getIntExtra("fps", 1));
            for (Bitmap bitmap : bitmaps) {
                addItem(new Frame(getItemCount(), bitmap));
            }
        }
    }
}
