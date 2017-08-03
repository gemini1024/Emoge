package com.emoge.app.emoge.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.StoreGif;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

/**
 * Created by jh on 17. 7. 30.
 * GIF 이미지 공유
 */

public class GifSharer {

    public static void shareOtherApps(Activity activity, Uri sharingGifFile) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/gif");
        shareIntent.putExtra(Intent.EXTRA_STREAM, sharingGifFile);
        activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.share_gif)));
    }

    public static void shareServer(final String category, final String title, Uri sharingGifFile) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://emoge-50942.appspot.com");
        StorageReference mountainsRef = storageRef.child(UUID.randomUUID().toString());

        UploadTask uploadTask = mountainsRef.putFile(sharingGifFile);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                Uri downloadUri = taskSnapshot.getDownloadUrl();
                sendServer(category, title,  downloadUri);
            }
        });
    }

    private static void sendServer(String category, String title, Uri downloadUri) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(category);
        StoreGif user = new StoreGif(title, downloadUri.toString(), 0);
        database.push().setValue(user);
    }
}
