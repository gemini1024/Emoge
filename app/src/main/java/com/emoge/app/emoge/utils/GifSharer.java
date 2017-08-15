package com.emoge.app.emoge.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.StoreGif;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 7. 30.
 * GIF 이미지 공유
 */

public class GifSharer {

    private Activity activity;
    private SweetAlertDialog loadingDialog;

    public GifSharer(Activity activity) {
        this.activity = activity;
    }

    public void shareOtherApps(final Uri sharingGifFile) {
        NetworkStatus.executeWithCheckingNetwork(activity, new NetworkStatus.RequireIntentTask() {
            @Override
            public void Task() {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("image/gif");
                shareIntent.putExtra(Intent.EXTRA_STREAM, sharingGifFile);
                activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.share_gif)));
            }
        });
    }

    public void shareOtherApps(final Uri sharingGifFile, final String destPackage) {
        NetworkStatus.executeWithCheckingNetwork(activity, new NetworkStatus.RequireIntentTask() {
            @Override
            public void Task() {
                if( activity.getPackageManager().getLaunchIntentForPackage(destPackage) != null ) {
                    Intent shareIntent = ShareCompat.IntentBuilder.from(activity).getIntent().setPackage(destPackage);
                    shareIntent.setAction(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("image/gif");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, sharingGifFile);
                    activity.startActivity(shareIntent);
                } else {
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + destPackage)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=" + destPackage)));
                    }
                }
            }
        });
    }

    void shareServer(final String category, final String title, final Uri sharingGifFile) {
        NetworkStatus.executeWithCheckingNetwork(activity, new NetworkStatus.RequireIntentTask() {
            @Override
            public void Task() {
                UploadTask uploadTask = sendFile(sharingGifFile);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showError();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        if(downloadUri != null) {
                            sendServerDB(category, new StoreGif(title, downloadUri.toString(), 0));
                        } else {
                            showError();
                        }
                    }
                });
            }
        });
    }


    private UploadTask sendFile(Uri sharingGifFile) {
        loadingDialog = SweetDialogs.showLoadingProgressDialog(activity, R.string.upload_gif_loading);

        StorageReference storageRef = FirebaseStorage.getInstance()
                .getReferenceFromUrl(activity.getString(R.string.firebase_bucket))
                .child(activity.getString(R.string.firebase_directory));
        StorageReference imageRef = storageRef.child(UUID.randomUUID().toString());

        return imageRef.putFile(sharingGifFile);
    }

    private void sendServerDB(String category, StoreGif storeGif) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(category);
        database.push().setValue(storeGif).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                showSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showError();
            }
        });
    }

    private void showError() {
        SweetDialogs.showErrorDialog(activity, R.string.err_upload_gif_title, R.string.err_upload_gif_content);
        if(loadingDialog != null) {
            loadingDialog.dismissWithAnimation();
        }
    }

    private void showSuccess() {
        SweetDialogs.showSuccessDialog(activity, R.string.upload_gif_title, R.string.upload_gif_content);
        if(loadingDialog != null) {
            loadingDialog.dismissWithAnimation();
        }
    }
}
