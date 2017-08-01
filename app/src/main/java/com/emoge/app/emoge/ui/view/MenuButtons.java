package com.emoge.app.emoge.ui.view;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.ui.correction.Correcter;
import com.emoge.app.emoge.ui.frame.FrameAdder;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

/**
 * Created by jh on 17. 8. 1.
 * BoomMenuButton 설정
 */

public class MenuButtons {

    public static void buildAddButton(@NonNull final Resources res,
                               @NonNull final BoomMenuButton bmb,
                               @NonNull final FrameAdder frameAdder) {
        String[] titles = res.getStringArray(R.array.frame_add_title);
        String[] subTitles = res.getStringArray(R.array.frame_add_sub);
        TypedArray imageIds = res.obtainTypedArray(R.array.frame_add_image);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            final HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(imageIds.getResourceId(i,0))
                    .normalText(titles[i])
                    .subNormalText(subTitles[i])
                    .normalColor(Color.GRAY)
                    .listener(frameAdder);
            bmb.addBuilder(builder);
        }
        imageIds.recycle();
    }

    public static void buildSelectButton(@NonNull final Resources res,
                                  @NonNull final BoomMenuButton bmb,
                                  @NonNull final Correcter correcter) {
        String[] titles = res.getStringArray(R.array.correct_title);
        TypedArray imageIds = res.obtainTypedArray(R.array.correct_image);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(imageIds.getResourceId(i,0))
                    .normalText(titles[i])
                    .normalColor(Color.GRAY)
                    .listener(correcter);
            bmb.addBuilder(builder);
        }
        imageIds.recycle();
    }
}
