package com.emoge.app.emoge.ui.boombutton;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.ui.frame.FrameAdder;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;

/**
 * Created by jh on 17. 7. 14.
 */

public class FrameAddButton {

    public void buildAddButton(@NonNull final Activity activity, @NonNull final BoomMenuButton bmb) {
        String[] titles = activity.getResources().getStringArray(R.array.frame_add_title);
        String[] subTitles = activity.getResources().getStringArray(R.array.frame_add_sub);
        TypedArray imageIds = activity.getResources().obtainTypedArray(R.array.frame_add_image);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            final HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(imageIds.getResourceId(i,0))
                    .normalText(titles[i])
                    .subNormalText(subTitles[i])
                    .normalColor(Color.GRAY)
                    .listener(new FrameAdder(activity));
            bmb.addBuilder(builder);
        }
        imageIds.recycle();
    }
}
