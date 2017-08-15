package com.emoge.app.emoge.ui.server;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emoge.app.emoge.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jh on 17. 8. 3.
 * 서버에 저장된 이미지들을 위한 ViewHolder
 */

class StoreGifViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.server_item_card)
    CardView card;
    @BindView(R.id.server_item_image_loading)
    AVLoadingIndicatorView loading;
    @BindView(R.id.server_item_image)
    ImageView image;
    @BindView(R.id.server_item_title)
    TextView title;
    @BindView(R.id.server_item_favorite_icon)
    ImageView favoriteIcon;
    @BindView(R.id.server_item_favorite)
    TextView favorite;

    StoreGifViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}