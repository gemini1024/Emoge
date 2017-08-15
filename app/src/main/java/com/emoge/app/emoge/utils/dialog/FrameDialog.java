package com.emoge.app.emoge.utils.dialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

import com.emoge.app.emoge.R;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by jh on 17. 8. 15.
 */

public class FrameDialog extends CustomDialog {
    private static final String LOG_TAG = FrameDialog.class.getSimpleName();

    public FrameDialog(Activity activity, Bitmap bitmap) {
        super(activity, R.layout.dialog_with_frame);
        PhotoView image = (PhotoView)findViewById(R.id.dialog_frame);
        image.setImageBitmap(bitmap);
    }

    public FrameDialog setRemoveButtonListener(View.OnClickListener listener) {
        findViewById(R.id.dialog_bt_remove).setOnClickListener(listener);
        return this;
    }
}
