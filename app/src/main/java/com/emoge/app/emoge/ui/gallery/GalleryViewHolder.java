package com.emoge.app.emoge.ui.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emoge.app.emoge.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jh on 17. 8. 8.
 * 갤러리 사진 View Holder
 */

class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.gallery_item_image)
    ImageView image;
    @BindView(R.id.gallery_item_loading)
    AVLoadingIndicatorView loading;
    @BindView(R.id.gallery_item_type)
    TextView type;

    private OnGalleryClickListener onGalleryClickListener;

    GalleryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);
    }

    void setOnGalleryClickListener(OnGalleryClickListener onGalleryClickListener) {
        this.onGalleryClickListener = onGalleryClickListener;
    }

    @Override
    public void onClick(View v) {
        onGalleryClickListener.onItemClickListener(getAdapterPosition());
    }
}
