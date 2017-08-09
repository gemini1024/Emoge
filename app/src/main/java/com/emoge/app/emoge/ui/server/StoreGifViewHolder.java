package com.emoge.app.emoge.ui.server;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidviewhover.BlurLayout;
import com.emoge.app.emoge.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jh on 17. 8. 3.
 * 서버에 저장된 이미지들을 위한 ViewHolder
 */

class StoreGifViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.server_item_container)
    BlurLayout container;
    @BindView(R.id.server_item_image)
    ImageView image;
    @BindView(R.id.server_item_title)
    TextView title;

    StoreGifViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}