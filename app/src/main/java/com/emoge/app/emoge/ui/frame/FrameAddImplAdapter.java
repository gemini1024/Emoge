package com.emoge.app.emoge.ui.frame;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.ui.VideoActivity;

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
                addItem(new Frame(nextId(), frameAdder.loadBitmapSampleSize(singleImageUri)));
            } else if( imageData.getClipData() != null ) {
                // multiple image
                ClipData clipData = imageData.getClipData();
                int firstAddPosition = getItemCount();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    addItemWithoutNotify(new Frame(nextId(),
                            frameAdder.loadBitmapSampleSize(clipData.getItemAt(i).getUri())));
                }
                notifyItemRangeInserted(firstAddPosition, getItemCount()-1);
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
                int firstAddPosition = getItemCount();
                for(Bitmap bitmap : bitmaps) {
                    addItemWithoutNotify(new Frame(nextId(), bitmap));
                }
                notifyItemRangeInserted(firstAddPosition, getItemCount()-1);
            }
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, e.getClass().getName(), e);
        }
    }

    @Override
    public void addFrameFromVideo(@NonNull Intent videoData) {
        if(videoData.getData() != null) {
            List<Bitmap> bitmaps = frameAdder.captureVideo(videoData.getData(),
                    videoData.getIntExtra(VideoActivity.INTENT_NAME_START_SEC, 0),
                    videoData.getIntExtra(VideoActivity.INTENT_NAME_CAPTURE_COUNT, 0),
                    videoData.getIntExtra(VideoActivity.INTENT_NAME_CAPTURE_DELAY, 100));
            int firstAddPosition = getItemCount();
            for (Bitmap bitmap : bitmaps) {
                addItemWithoutNotify(new Frame(nextId(), bitmap));
            }
            notifyItemRangeInserted(firstAddPosition, getItemCount()-1);
        }
    }
}
