package com.emoge.app.emoge.ui.boombutton;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;

import com.emoge.app.emoge.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

/**
 * Created by jh on 17. 7. 14.
 */

public class FrameAdder {
    public static final int INTENT_GET_IMAGE    = 0;
    public static final int INTENT_GET_GIF      = 1;
    public static final int INTENT_GET_VIDEO    = 2;


    public void buildAddButton(final Activity activity, final BoomMenuButton bmb) {
        String[] titles = {"이미지 추가", "다른 움짤 추가", "비디오 추가"};
        String[] subTitles = {"이미지를 추가합니다", "다른 움짤을 불러옵니다", "비디오를 추가합니다"};
        int[] images = {R.drawable.ic_photo_library_24dp, R.drawable.ic_gif_24dp, R.drawable.ic_play_circle_filled_24dp};

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            final HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(images[i])
                    .normalText(titles[i])
                    .subNormalText(subTitles[i])
                    .normalColor(Color.GRAY)
                    .listener(new LoadFrames(activity));
            bmb.addBuilder(builder);
        }
    }

    private class LoadFrames implements OnBMClickListener, PermissionListener {
        Activity activity;
        int selectedIndex;

        LoadFrames(Activity activity) {
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
                    activity.startActivityForResult(Intent.createChooser(intent,"Select Video"), INTENT_GET_IMAGE);
                    break;
                case INTENT_GET_GIF :
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/gif");
                    activity.startActivityForResult(Intent.createChooser(intent,"Select Video"), INTENT_GET_IMAGE);
                    break;
                case INTENT_GET_VIDEO :
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    activity.startActivityForResult(Intent.createChooser(intent,"Select Video"), INTENT_GET_VIDEO);
                    break;
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

        }
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
            //It's an image
        }
        else if(mimeType.startsWith("video")) {
            //It's a video
        }

        return contentPath;
    }

}
