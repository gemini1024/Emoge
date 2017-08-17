package com.emoge.app.emoge.utils.dialog;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.utils.GifSharer;
import com.emoge.app.emoge.utils.GlideAvRequester;
import com.github.chrisbanes.photoview.PhotoView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 8. 4.
 * 프레임 확인 및 저장된 움짤 확인 용도의 Dialog
 */

public class ImageDialog extends CustomDialog {
    private static final String LOG_TAG = ImageDialog.class.getSimpleName();

    private Activity activity;
    private Uri fileUri;
    private View shareLayout;
    private RemoveFileCallBack removeFileCallBack;

    public interface RemoveFileCallBack {
        void Task();
    }

    public ImageDialog(Activity activity, Uri fileUri) {
        super(activity, R.layout.dialog_with_image, true);
        this.activity = activity;
        this.fileUri = fileUri;
        shareLayout = findViewById(R.id.dialog_share_container);
        initImage();
        initButtons();
    }

    private void initImage() {
        ((TextView)findViewById(R.id.dialog_image_title)).setText(fileUri.getLastPathSegment());
        final AVLoadingIndicatorView loadingIndicatorView = (AVLoadingIndicatorView)findViewById(R.id.dialog_image_loading);
        PhotoView image = (PhotoView)findViewById(R.id.dialog_image);
        loadingIndicatorView.show();
        Glide.with(activity).asGif()
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true))
                .load(fileUri)
                .listener(new GlideAvRequester<GifDrawable>(loadingIndicatorView))
                .into(image);
    }

    private void initButtons() {
        findViewById(R.id.dialog_bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.dialog_bt_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( shareLayout.getVisibility() == View.GONE ) {
                    showShareButton();
                } else {
                    hideShareButton();
                }
            }
        });
        findViewById(R.id.dialog_bt_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetDialogs.showWarningDialog(activity,
                        R.string.remove_file, R.string.remove_file_content)
                        .setConfirmText(activity.getString(R.string.remove_file))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                removeFile();
                            }
                        });
            }
        });
    }

    public void setRemoveFileCallBack(RemoveFileCallBack removeFileCallBack) {
        this.removeFileCallBack = removeFileCallBack;
    }

    private void removeFile() {
        File file = new File(fileUri.getPath());
        if(file.exists() && file.delete()) {
            if(removeFileCallBack != null) {
                removeFileCallBack.Task();
            }
            dismiss();
            Toast.makeText(activity, R.string.remove_file_result, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, R.string.err_remove_file, Toast.LENGTH_SHORT).show();
        }
    }

    public ImageDialog setHomeButtonListener(View.OnClickListener listener) {
        ImageButton homeButton = (ImageButton)findViewById(R.id.dialog_bt_home);
        homeButton.setOnClickListener(listener);
        homeButton.setVisibility(View.VISIBLE);
        return this;
    }


    public ImageDialog showShareButton() {
        final GifSharer gifSharer = new GifSharer(activity);
        Animation exitAnim = AnimationUtils.loadAnimation(activity, R.anim.enter);
        shareLayout.setAnimation(exitAnim);
        shareLayout.setVisibility(View.VISIBLE);
        findViewById(R.id.dialog_share_kakao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GifSharer(activity).shareOtherApps(fileUri, activity.getString(R.string.app_package_kakao));
            }
        });
        findViewById(R.id.dialog_share_gdrive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gifSharer.shareOtherApps(fileUri, activity.getString(R.string.app_package_google_drive));
            }
        });
        findViewById(R.id.dialog_share_facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gifSharer.shareOtherApps(fileUri, activity.getString(R.string.app_package_facebook));
            }
        });
        findViewById(R.id.dialog_share_others).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gifSharer.shareOtherApps(fileUri);
            }
        });
        return this;
    }

    private ImageDialog hideShareButton() {
        Animation exitAnim = AnimationUtils.loadAnimation(activity, R.anim.exit);
        shareLayout.setAnimation(exitAnim);
        shareLayout.setVisibility(View.GONE);
        return this;
    }

    public ImageDialog setShareServerButton(View.OnClickListener listener) {
        shareLayout.setVisibility(View.VISIBLE);
        ImageButton shareServerButton = (ImageButton)findViewById(R.id.dialog_share_server);
        shareServerButton.setOnClickListener(listener);
        shareServerButton.setVisibility(View.VISIBLE);
        return this;
    }
}
