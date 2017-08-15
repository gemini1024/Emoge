package com.emoge.app.emoge.utils.dialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.utils.GlideAvRequester;
import com.github.chrisbanes.photoview.PhotoView;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by jh on 17. 8. 4.
 * 프레임 확인 및 저장된 움짤 확인 용도의 Dialog
 */

public class ImageDialog extends CustomDialog {
    private static final String LOG_TAG = ImageDialog.class.getSimpleName();

    private View shareLayout;

    private ImageDialog(Activity activity) {
        super(activity, R.layout.dialog_with_image);
        shareLayout = findViewById(R.id.dialog_share_container);
    }

    public ImageDialog(Activity activity, Bitmap bitmap) {
        this(activity);
        PhotoView image = (PhotoView)findViewById(R.id.dialog_image);
        image.setImageBitmap(bitmap);
    }

    public ImageDialog(Activity activity, Uri uri) {
        this(activity);
        AVLoadingIndicatorView loadingIndicatorView = (AVLoadingIndicatorView)findViewById(R.id.dialog_image_loading);
        PhotoView image = (PhotoView)findViewById(R.id.dialog_image);
        loadingIndicatorView.setVisibility(View.VISIBLE);
        loadingIndicatorView.show();
        Glide.with(activity).load(uri).
                listener(new GlideAvRequester<Drawable>(loadingIndicatorView)).into(image);
    }

    public ImageDialog setRemoveButtonListener(View.OnClickListener listener) {
        Button removeButton = (Button)findViewById(R.id.dialog_bt_remove);
        removeButton.setOnClickListener(listener);
        removeButton.setVisibility(View.VISIBLE);
        return this;
    }

    public ImageDialog setShareServerButton(View.OnClickListener listener) {
        shareLayout.setVisibility(View.VISIBLE);
        ImageButton shareServerButton = (ImageButton)findViewById(R.id.dialog_share_server);
        shareServerButton.setOnClickListener(listener);
        shareServerButton.setVisibility(View.VISIBLE);
        return this;
    }

    public ImageDialog setShareKakaoButton(View.OnClickListener listener) {
        shareLayout.setVisibility(View.VISIBLE);
        ImageButton shareOtherAppButton = (ImageButton)findViewById(R.id.dialog_share_kakao);
        shareOtherAppButton.setOnClickListener(listener);
        shareOtherAppButton.setVisibility(View.VISIBLE);
        return this;
    }

    public ImageDialog setShareGoogleDriveButton(View.OnClickListener listener) {
        shareLayout.setVisibility(View.VISIBLE);
        ImageButton shareOtherAppButton = (ImageButton)findViewById(R.id.dialog_share_gdrive);
        shareOtherAppButton.setOnClickListener(listener);
        shareOtherAppButton.setVisibility(View.VISIBLE);
        return this;
    }

    public ImageDialog setShareFacebookButton(View.OnClickListener listener) {
        shareLayout.setVisibility(View.VISIBLE);
        ImageButton shareOtherAppButton = (ImageButton)findViewById(R.id.dialog_share_facebook);
        shareOtherAppButton.setOnClickListener(listener);
        shareOtherAppButton.setVisibility(View.VISIBLE);
        return this;
    }

    public ImageDialog setShareOtherAppButton(View.OnClickListener listener) {
        shareLayout.setVisibility(View.VISIBLE);
        ImageButton shareOtherAppButton = (ImageButton)findViewById(R.id.dialog_share_others);
        shareOtherAppButton.setOnClickListener(listener);
        shareOtherAppButton.setVisibility(View.VISIBLE);
        return this;
    }
}
