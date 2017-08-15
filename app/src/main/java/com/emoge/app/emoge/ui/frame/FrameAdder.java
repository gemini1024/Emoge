package com.emoge.app.emoge.ui.frame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.encoder.GifDecoder;
import com.emoge.app.emoge.model.PaletteMessage;
import com.emoge.app.emoge.ui.correction.Correcter;
import com.emoge.app.emoge.utils.Logger;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by jh on 17. 7. 26.
 * Frame 추가.
 * 이 클래스로만 단말 내부 파일 접근
 */


public class FrameAdder implements OnBMClickListener, BitmapLoadable {
    private static final String LOG_TAG = FrameAdder.class.getSimpleName();

    // Menu Position
    public static final int INTENT_GET_IMAGE        = 0;
    public static final int INTENT_GET_GIF          = 1;
    public static final int INTENT_GET_VIDEO        = 2;
    public static final int INTENT_CAPTURE_VIDEO    = 100;


    // 이미지 크기 제한
    private static final int MAX_WIDTH   = 400;
    private static final int MAX_HEIGHT  = 400;

    private Activity activity;

    public FrameAdder(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onBoomButtonClick(int index) {
        Intent intent;

        switch (index) {
            case INTENT_GET_IMAGE :
                intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/jpeg", "image/png"});
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                activity.overridePendingTransition(0, android.R.anim.fade_in);
                activity.startActivityForResult(Intent.createChooser(intent,activity.getString(R.string.select_image)), INTENT_GET_IMAGE);
                break;
            case INTENT_GET_GIF :
                intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/gif"});
                activity.overridePendingTransition(0, android.R.anim.fade_in);
                activity.startActivityForResult(Intent.createChooser(intent,activity.getString(R.string.select_image)), INTENT_GET_GIF);
                break;
            case INTENT_GET_VIDEO :
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                activity.overridePendingTransition(0, android.R.anim.fade_in);
                activity.startActivityForResult(Intent.createChooser(intent,activity.getString(R.string.select_video)), INTENT_GET_VIDEO);
                break;
        }

    }

    private String getRealPathFromUri(Context context, Uri selectedUri) {
        String[] columns = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.MIME_TYPE };

        Cursor cursor = context.getContentResolver().query(selectedUri, columns, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int pathColumnIndex     = cursor.getColumnIndex( columns[0] );
            int mimeTypeColumnIndex = cursor.getColumnIndex( columns[1] );

            String contentPath = cursor.getString(pathColumnIndex);
            String mimeType    = cursor.getString(mimeTypeColumnIndex);
            cursor.close();

            if(mimeType.startsWith("image")) {
                // image
            }
            else if(mimeType.startsWith("video")) {
                // video
            }

            return contentPath;
        }
        return selectedUri.getPath();
    }

    @Override @NonNull
    public Bitmap loadBitmapSampleSize(@NonNull Uri imageUri) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(imageUri), null, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = getSampleSize(options.outWidth, options.outHeight);

        Bitmap bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(imageUri), null, options);
        int orientation = exifOrientationToDegrees(new ExifInterface(imageUri.getPath())
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL));
        return rotateBitmap(bitmap, orientation);
    }

    private Bitmap rotateBitmap(@NonNull Bitmap bitmap, int degree) {
        if(degree != 0) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degree, (float)bitmap.getWidth()/2, (float)bitmap.getHeight());
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return rotatedBitmap;
        }
        return bitmap;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }


    @Override @NonNull
    public List<Bitmap> loadBitmapsFromGif(@NonNull Uri imageUri, int maxSize) throws FileNotFoundException {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        GifDecoder decoder = new GifDecoder();
        decoder.read(activity.getContentResolver().openInputStream(imageUri));
        int addCount = Math.min(decoder.getFrameCount(), maxSize);
        for(int i=0; i<addCount; i++) {
            bitmaps.add(decoder.getFrame(i));
        }
        if(decoder.getDelay(0) > 9 && decoder.getDelay(0) < 2001) {
            EventBus.getDefault().post(new PaletteMessage(Correcter.MOD_FRAME_DELAY,
                    decoder.getDelay(0)));
        }
        return bitmaps;
    }


    private int getSampleSize(int width, int height) {
        int inSampleSize = 1;
        if (width > MAX_WIDTH || height > MAX_HEIGHT) {
            inSampleSize = width > height ? width / MAX_WIDTH : height / MAX_HEIGHT;
        }
        return inSampleSize;
    }



    @Override @NonNull
    public List<Bitmap> captureVideo(@NonNull Uri videoUri, int maxSize, int startSec, int count, int fps) {
        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
        List<Bitmap> bitmapArrayList = new ArrayList<>();

        int addCount = Math.min(count, maxSize);
        retriever.setDataSource(getRealPathFromUri(activity, videoUri));
        for(int i = 0; i < addCount; i++){
            Bitmap videoFrame = retriever.getFrameAtTime((startSec + i*fps)*1000L, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
            if(videoFrame != null) {
                bitmapArrayList.add(resizeVideoFrame(videoFrame));
            } else {
                Logger.e(LOG_TAG, "not found video frame : " + i);
            }
        }
        retriever.release();
        EventBus.getDefault().post(new PaletteMessage(Correcter.MOD_FRAME_DELAY, fps));

        return bitmapArrayList;
    }

    @NonNull
    private Bitmap resizeVideoFrame(@NonNull Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();

        if(width > MAX_WIDTH || height > MAX_HEIGHT) {
            if(width > height) {
                float rate = MAX_WIDTH / (float) width;
                height = (int) (height * rate);
                width = MAX_WIDTH;
            } else {
                float rate = MAX_HEIGHT / (float) height;
                width = (int) (width * rate);
                height = MAX_HEIGHT;
            }
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(source, width, height, true);
            source.recycle();
            return scaledBitmap;
        } else {
            return source;
        }
    }

}

