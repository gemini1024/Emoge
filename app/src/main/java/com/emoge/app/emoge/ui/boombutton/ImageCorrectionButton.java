package com.emoge.app.emoge.ui.boombutton;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.emoge.app.emoge.R;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

/**
 * Created by jh on 17. 7. 17.
 */

public class ImageCorrectionButton {

    public void buildSelectButton(@NonNull final Activity activity, @NonNull final BoomMenuButton bmb) {
        String[] titles = activity.getResources().getStringArray(R.array.correct_title);
        TypedArray imageIds = activity.getResources().obtainTypedArray(R.array.correct_image);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(imageIds.getResourceId(i,0))
                    .normalText(titles[i])
                    .normalColor(Color.GRAY);
            bmb.addBuilder(builder);
        }
        imageIds.recycle();
    }

}
