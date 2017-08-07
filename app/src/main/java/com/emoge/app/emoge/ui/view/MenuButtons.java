package com.emoge.app.emoge.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

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
    private static final String LOG_TAG = MenuButtons.class.getSimpleName();

    private static final int ANIMATION_DELAY    = 400;
    private static final Rect IMAGE_PADDING     = new Rect(24,24,24,24);

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
                    .normalColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                    .highlightedColor(ContextCompat.getColor(context, R.color.colorPrimary))
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
                    .normalImageRes(imageIds.getResourceId(i,0))
                    .imagePadding(IMAGE_PADDING)
                    .normalText(titles[i])
                    .normalColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                    .highlightedColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .listener(correcter);
            bmb.addBuilder(builder);
        }
        imageIds.recycle();
    }


    // Animation
    public static void showWithAnimation(@NonNull BoomMenuButton bmb) {
        if(View.GONE == bmb.getVisibility() || View.INVISIBLE == bmb.getVisibility()) {
            ScaleAnimation anim = new ScaleAnimation(0, 1, 0, 1,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(ANIMATION_DELAY);
            anim.setInterpolator(new OvershootInterpolator());
            bmb.startAnimation(anim);
            bmb.setVisibility(View.VISIBLE);
        }
    }

    public static void hideWithAnimation(@NonNull BoomMenuButton bmb) {
        if(View.VISIBLE == bmb.getVisibility()) {
            ScaleAnimation anim = new ScaleAnimation(1, 0, 1, 0,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(ANIMATION_DELAY);
            anim.setInterpolator(new OvershootInterpolator());
            bmb.startAnimation(anim);
            bmb.setVisibility(View.GONE);
        }
    }

}
