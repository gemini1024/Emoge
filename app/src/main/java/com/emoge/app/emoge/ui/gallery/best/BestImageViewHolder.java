package com.emoge.app.emoge.ui.gallery.best;

import android.view.View;

import com.emoge.app.emoge.ui.gallery.OnGalleryClickListener;

/**
 * Created by jh on 17. 8. 17.
 * ServerImageViewHolder 중 실질적 이미지 ViewHolder. 클릭시 다운로드
 */

class BestImageViewHolder extends ServerImageViewHolder {

    private OnGalleryClickListener onGalleryClickListener;

    BestImageViewHolder(OnGalleryClickListener onGalleryClickListener, View itemView) {
        super(itemView);
        this.onGalleryClickListener = onGalleryClickListener;
    }

    @Override
    public void onClick(View v) {
        onGalleryClickListener.onItemClickListener(getAdapterPosition());
    }
}
