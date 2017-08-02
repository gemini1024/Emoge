package com.emoge.app.emoge.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.emoge.app.emoge.R;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by jh on 17. 8. 2.
 * 커스텀 다이얼로그 제어
 */

public class CustomDialogController {
    private View innerLayout;
    private View shareLayout;

    private Dialog resultDialog;

    CustomDialogController(Activity activity) {
        innerLayout = activity.getLayoutInflater().inflate(R.layout.dialog_with_image, null);
        shareLayout = innerLayout.findViewById(R.id.dialog_share_container);
    }


    public void setImage(Bitmap bitmap) {
        PhotoView image = (PhotoView)innerLayout.findViewById(R.id.dialog_image);
        image.setImageBitmap(bitmap);
        image.setVisibility(View.VISIBLE);
    }


    public void setImage(Activity activity, Uri uri) {
        PhotoView image = (PhotoView)innerLayout.findViewById(R.id.dialog_image);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.format(DecodeFormat.PREFER_RGB_565).placeholder(R.drawable.img_no_image);
        Glide.with(activity).load(uri).apply(requestOptions).into(image);
        image.setVisibility(View.VISIBLE);
    }

    public void setRemoveButtonListener(View.OnClickListener listener) {
        Button removeButton = (Button) innerLayout.findViewById(R.id.dialog_bt_remove);
        removeButton.setOnClickListener(listener);
        removeButton.setVisibility(View.VISIBLE);
    }

    public void setShareKakaoButton(View.OnClickListener listener) {
        shareLayout.setVisibility(View.VISIBLE);
        ImageButton shareKakaoButton = (ImageButton) innerLayout.findViewById(R.id.dialog_share_kakao);
        shareKakaoButton.setOnClickListener(listener);
        shareKakaoButton.setVisibility(View.VISIBLE);
    }

    Dialog build(AlertDialog.Builder builder) {
        builder.setView(innerLayout);
        resultDialog = builder.create();
        return resultDialog;
    }


    void show() {
        if(resultDialog != null) {
            resultDialog.show();
        }
    }

    public void dismiss() {
        if(resultDialog != null) {
            resultDialog.dismiss();
        }
    }

}
