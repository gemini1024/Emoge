package com.emoge.app.emoge.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.emoge.app.emoge.R;

/**
 * Created by jh on 17. 8. 9.
 * Android View Hover 여러 군데서 쉽게 쓰기 위함
 */

public class HoverViews {
    private Context context;
    private BlurLayout blurLayout;
    private View hoverView;

    public HoverViews(Context context, BlurLayout blurLayout) {
        this.context = context;
        this.blurLayout = blurLayout;
        this.hoverView = LayoutInflater.from(context).inflate(R.layout.item_hover, null);
    }

    public void buildHoverView() {
        blurLayout.setHoverView(hoverView);
        blurLayout.setBlurDuration(550);
        blurLayout.addChildAppearAnimator(hoverView, R.id.hover_download, Techniques.FlipInX, 550, 0);
        blurLayout.addChildAppearAnimator(hoverView, R.id.hover_more, Techniques.FlipInX, 550, 250);

        blurLayout.addChildDisappearAnimator(hoverView, R.id.hover_download, Techniques.FlipOutX, 550, 250);
        blurLayout.addChildDisappearAnimator(hoverView, R.id.hover_more, Techniques.FlipOutX, 550, 0);

        blurLayout.addChildAppearAnimator(hoverView, R.id.hover_text, Techniques.FadeInUp);
        blurLayout.addChildDisappearAnimator(hoverView, R.id.hover_text, Techniques.FadeOutDown);
    }

    public void setText(String text) {
        if(hoverView != null) {
            ((TextView) hoverView.findViewById(R.id.hover_text)).setText(text);
        }
    }

    public void setDownloadButton(View.OnClickListener listener) {
        if(hoverView != null) {
            hoverView.findViewById(R.id.hover_download).setOnClickListener(listener);
        }
    }

    public void setMoreButton(View.OnClickListener listener) {
        if(hoverView != null) {
            hoverView.findViewById(R.id.hover_more).setOnClickListener(listener);
        }
    }
}
