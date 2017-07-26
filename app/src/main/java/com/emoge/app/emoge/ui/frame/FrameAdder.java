package com.emoge.app.emoge.ui.frame;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.emoge.app.emoge.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;

import java.util.ArrayList;

/**
 * Created by jh on 17. 7. 26.
 */


public class FrameAdder implements OnBMClickListener, PermissionListener {
    private static final String LOG_TAG = FrameAdder.class.getSimpleName();

    public static final int INTENT_GET_IMAGE    = 0;
    public static final int INTENT_GET_GIF      = 1;
    public static final int INTENT_GET_VIDEO    = 2;

    private Activity activity;
    private int selectedIndex;

    public FrameAdder(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onBoomButtonClick(int index) {
        selectedIndex = index;

        new TedPermission(activity).setPermissionListener(this)
                .setDeniedMessage(R.string.err_permission_denied)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    public void onPermissionGranted() {
        Intent intent;
        switch (selectedIndex) {
            case INTENT_GET_IMAGE :
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/jpeg, image/png");
                activity.startActivityForResult(Intent.createChooser(intent,activity.getString(R.string.select_image)), INTENT_GET_IMAGE);
                break;
            case INTENT_GET_GIF :
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/gif");
                activity.startActivityForResult(Intent.createChooser(intent,"Select Video"), INTENT_GET_GIF);
                break;
            case INTENT_GET_VIDEO :
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(Intent.createChooser(intent,activity.getString(R.string.select_video)), INTENT_GET_VIDEO);
                break;
        }
    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        Log.d(LOG_TAG, activity.getString(R.string.err_permission_denied));
    }


    private String getRealPathFromUri(Context context, Uri selectedUri) {
        String[] columns = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.MIME_TYPE };

        Cursor cursor = context.getContentResolver().query(selectedUri, columns, null, null, null);
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

}

