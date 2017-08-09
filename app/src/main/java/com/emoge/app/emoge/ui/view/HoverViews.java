package com.emoge.app.emoge.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.emoge.app.emoge.R;

/**
 * Created by jh on 17. 8. 9.
 * Android View Hover 여러 군데서 쉽게 쓰기 위함
 */

public class HoverViews {
    public static final int BLUR_DURATION = 300;

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
        blurLayout.setBlurDuration(BLUR_DURATION);
        blurLayout.enableBlurBackground(false);
    }

    public void setText(String text) {
        if(hoverView != null) {
            TextView textView = (TextView) hoverView.findViewById(R.id.hover_text);
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
            blurLayout.addChildAppearAnimator(hoverView, R.id.hover_text, Techniques.FadeInUp);
            blurLayout.addChildDisappearAnimator(hoverView, R.id.hover_text, Techniques.FadeOutDown);
        }
    }

    public void setDownloadButton(View.OnClickListener listener) {
        if(hoverView != null) {
            ImageView imageView = (ImageView)hoverView.findViewById(R.id.hover_download);
            imageView.setOnClickListener(listener);
            imageView.setVisibility(View.VISIBLE);
            blurLayout.addChildAppearAnimator(hoverView, R.id.hover_download,
                    Techniques.FlipInX, BLUR_DURATION, 0);
            blurLayout.addChildDisappearAnimator(hoverView, R.id.hover_download,
                    Techniques.FlipOutX, BLUR_DURATION, 250);
        }
    }

    public void setFavoriteButton(View.OnClickListener listener) {
        if(hoverView != null) {
            ImageView imageView = (ImageView)hoverView.findViewById(R.id.hover_favorite);
            imageView.setOnClickListener(listener);
            imageView.setVisibility(View.VISIBLE);
            blurLayout.addChildAppearAnimator(hoverView, R.id.hover_favorite,
                    Techniques.FlipInX, BLUR_DURATION, 125);
            blurLayout.addChildDisappearAnimator(hoverView, R.id.hover_favorite,
                    Techniques.FlipOutX, BLUR_DURATION, 125);
        }
    }

    public void setMoreButton(View.OnClickListener listener) {
        if(hoverView != null) {
            ImageView imageView = (ImageView)hoverView.findViewById(R.id.hover_more);
            imageView.setOnClickListener(listener);
            imageView.setVisibility(View.VISIBLE);
            blurLayout.addChildAppearAnimator(hoverView, R.id.hover_more,
                    Techniques.FlipInX, BLUR_DURATION, 250);
            blurLayout.addChildDisappearAnimator(hoverView, R.id.hover_more,
                    Techniques.FlipOutX, BLUR_DURATION, 0);
        }
    }


    public void setFavoriteSelected(boolean selected) {
        if(hoverView != null) {
            ImageView imageView = (ImageView)hoverView.findViewById(R.id.hover_favorite);
            imageView.setSelected(selected);
        }
    }

    public void dismissHover() {
        blurLayout.dismissHover();
    }

    public void removeHover() {
        blurLayout.clearAnimation();
        blurLayout.removeView(hoverView);
    }

}
