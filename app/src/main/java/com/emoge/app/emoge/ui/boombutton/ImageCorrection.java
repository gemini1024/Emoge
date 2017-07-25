package com.emoge.app.emoge.ui.boombutton;

import android.app.Activity;
import android.graphics.Color;

import com.emoge.app.emoge.R;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

/**
 * Created by jh on 17. 7. 17.
 */

public class ImageCorrection {


    public void buildSelectButton(final Activity activity, final BoomMenuButton bmb) {
        String[] names = {"밝기 보정", "대비 보정", "감마 보정"};
        int[] images = {R.drawable.ic_healing_24dp, R.drawable.ic_healing_24dp, R.drawable.ic_healing_24dp};

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(images[i])
                    .normalText(names[i])
                    .normalColor(Color.GRAY);
            bmb.addBuilder(builder);
        }
    }

}
