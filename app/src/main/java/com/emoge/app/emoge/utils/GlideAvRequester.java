package com.emoge.app.emoge.utils;

import android.support.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by jh on 17. 8. 15.
 * 여러 군데서 Glide 의 listener 로 AVLoadingIndicatorView 를 적용하기 위함.
 */

public class GlideAvRequester<T> implements RequestListener<T> {
    AVLoadingIndicatorView loadingIndicatorView;

    public GlideAvRequester(AVLoadingIndicatorView loadingIndicatorView) {
        this.loadingIndicatorView = loadingIndicatorView;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(T resource, Object model, Target<T> target, DataSource dataSource, boolean isFirstResource) {
        loadingIndicatorView.hide();
        return false;
    }
}
