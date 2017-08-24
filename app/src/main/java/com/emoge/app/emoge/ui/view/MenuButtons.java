package com.emoge.app.emoge.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.ui.correction.Correcter;
import com.emoge.app.emoge.ui.frame.FrameAdder;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;

/**
 * Created by jh on 17. 8. 1.
 * BoomMenuButton 설정
 */

public class MenuButtons {
    private static final String LOG_TAG = MenuButtons.class.getSimpleName();

    private static final int BUTTON_BIG_RADIUS  = 48;
    private static final Rect IMAGE_PADDING     = new Rect(Util.dp2px(8),Util.dp2px(8),Util.dp2px(8),Util.dp2px(8));
    private static final Rect IMAGE_BIG_SIZE
            = new Rect(Util.dp2px(0),Util.dp2px(0),Util.dp2px(BUTTON_BIG_RADIUS*2),Util.dp2px(BUTTON_BIG_RADIUS*2));

    public static void buildAddButton(@NonNull final Context context,
                               @NonNull final BoomMenuButton bmb,
                               @NonNull final FrameAdder frameAdder) {
        String[] titles = context.getResources().getStringArray(R.array.frame_add_title);
        String[] subTitles = context.getResources().getStringArray(R.array.frame_add_sub);
        TypedArray imageIds = context.getResources().obtainTypedArray(R.array.frame_add_image);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            final HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(imageIds.getResourceId(i,0))
                    .imagePadding(IMAGE_PADDING)
                    .normalText(titles[i])
                    .subNormalText(subTitles[i])
                    .normalColorRes(R.color.colorPrimaryDark)
                    .highlightedTextColorRes(R.color.colorPrimary)
                    .listener(frameAdder);
            bmb.addBuilder(builder);
        }
        imageIds.recycle();
    }

    public static void buildSelectButton(@NonNull final Context context,
                                  @NonNull final BoomMenuButton bmb,
                                  @NonNull final Correcter correcter) {
        String[] titles = context.getResources().getStringArray(R.array.correct_title);
        TypedArray imageIds = context.getResources().obtainTypedArray(R.array.correct_image);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .isRound(false)
                    .buttonRadius(Util.dp2px(BUTTON_BIG_RADIUS))
                    .textTopMargin(Util.dp2px(0))
                    .imageRect(IMAGE_BIG_SIZE)
                    .shadowEffect(false)
                    .normalImageRes(imageIds.getResourceId(i,0))
                    .normalText(titles[i])
                    .normalColorRes(android.R.color.transparent)
                    .highlightedTextColorRes(R.color.colorPrimary)
                    .listener(correcter);
            bmb.addBuilder(builder);
        }
        imageIds.recycle();
    }

}
