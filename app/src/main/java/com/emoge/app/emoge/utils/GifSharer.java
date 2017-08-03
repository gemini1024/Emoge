package com.emoge.app.emoge.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.emoge.app.emoge.R;

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
}
