package com.emoge.app.emoge.ui.frame;

import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emoge.app.emoge.R;
import com.makeramen.dragsortadapter.DragSortAdapter;
import com.makeramen.dragsortadapter.NoForegroundShadowBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jh on 17. 8. 2.
 * Frame 확인을 위한 ViewHolder
 */

class FrameViewHolder extends DragSortAdapter.ViewHolder implements
        View.OnClickListener, View.OnLongClickListener {

    @BindView(R.id.frame_item_image)
    ImageView image;
    @BindView(R.id.frame_item_number)
    TextView number;

    private OnFrameClickListener frameClickListener;

    FrameViewHolder(DragSortAdapter<?> dragSortAdapter, View itemView) {
        super(dragSortAdapter, itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    void setFrameClickListener(OnFrameClickListener frameClickListener) {
        this.frameClickListener = frameClickListener;
    }

    @Override
    public void onClick(View v) {
        frameClickListener.onFrameClick(getItemId());
    }

    @Override
    public boolean onLongClick(View v) {
        startDrag();
        return false;
    }

    @Override
    public View.DragShadowBuilder getShadowBuilder(View itemView, Point touchPoint) {
        return new NoForegroundShadowBuilder(itemView, touchPoint);
    }
}