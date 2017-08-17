package com.emoge.app.emoge.ui.gallery.best;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emoge.app.emoge.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jh on 17. 8. 17.
 * GalleryActivity 에 서버의 인기 움짤을 띄우기 위한 ViewHolder
 */

abstract class ServerImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.gallery_item_image)
    ImageView image;
    @BindView(R.id.gallery_item_loading)
    AVLoadingIndicatorView loading;
    @BindView(R.id.gallery_item_type)
    TextView rank;

    ServerImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public abstract void onClick(View v);
}
