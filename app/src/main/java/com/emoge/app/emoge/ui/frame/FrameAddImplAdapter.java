package com.emoge.app.emoge.ui.frame;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.utils.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by jh on 17. 8. 1.
 * 파일에서 이미지를 추가할 수 있는 어댑터
 */

public class FrameAddImplAdapter extends FrameAdapter implements FrameAddable {
    private static final String LOG_TAG = FrameAddImplAdapter.class.getSimpleName();

    private BitmapLoadable frameAdder;      // 파일 접근 처리

    public FrameAddImplAdapter(@NonNull RecyclerView recyclerView,
                               @NonNull List<Frame> frames,
                               @NonNull BitmapLoadable frameAdder) {
        super(recyclerView, frames);
        this.frameAdder = frameAdder;
    }


    @Override
    public boolean addFrameFromImages(@NonNull Intent imageData) {
        boolean resultBool = false;
        try {
            Uri singleImageUri = imageData.getData();
            if( singleImageUri != null ) {
                // single image
                resultBool = addItem(new Frame(nextId(), frameAdder.loadBitmapSampleSize(singleImageUri)));
            } else if( imageData.getClipData() != null ) {
                // multiple image
                ClipData clipData = imageData.getClipData();
                int firstAddPosition = getItemCount();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    resultBool =addItemWithoutNotify(new Frame(nextId(),
                            frameAdder.loadBitmapSampleSize(clipData.getItemAt(i).getUri())));
                }
                notifyItemRangeInserted(firstAddPosition, clipData.getItemCount());
            }
        } catch (IOException e) {
            Logger.e(LOG_TAG, e);
        }
        return resultBool;
    }

    @Override
    public boolean addFrameFromGif(@NonNull Intent imageData, int maxSize) {
        boolean resultBool = false;
        try {
            Uri imageUri = imageData.getData();
            if( imageUri != null ) {
                List<Bitmap> bitmaps = frameAdder.loadBitmapsFromGif(imageUri, maxSize);
                int firstAddPosition = getItemCount();
                for(Bitmap bitmap : bitmaps) {
                    resultBool = addItemWithoutNotify(new Frame(nextId(), bitmap));
                }
                notifyItemRangeInserted(firstAddPosition, bitmaps.size());
            }
        } catch (FileNotFoundException e) {
            Logger.e(LOG_TAG, e);
        }
        return resultBool;
    }

    @Override
    public boolean addFrameFromVideo(@NonNull Intent videoData, int maxSize) {
        boolean resultBool = false;
        if(videoData.getData() != null) {
            List<Bitmap> bitmaps = frameAdder.captureVideo(videoData.getData(), maxSize,
                    videoData.getIntExtra(VideoFragment.INTENT_NAME_START_SEC, 0),
                    videoData.getIntExtra(VideoFragment.INTENT_NAME_CAPTURE_COUNT, 0),
                    videoData.getIntExtra(VideoFragment.INTENT_NAME_CAPTURE_DELAY, 100));
            int firstAddPosition = getItemCount();
            for (Bitmap bitmap : bitmaps) {
                resultBool = addItemWithoutNotify(new Frame(nextId(), bitmap));
            }
            notifyItemRangeInserted(firstAddPosition, bitmaps.size());
        }
        return resultBool;
    }
}
