package com.emoge.app.emoge.ui.frame;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.makeramen.dragsortadapter.DragSortAdapter;
import com.makeramen.dragsortadapter.NoForegroundShadowBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jh on 17. 7. 25.
 */

public class FrameAdapter extends DragSortAdapter<FrameAdapter.FrameViewHolder> {
    public static final String LOG_TAG = FrameAdapter.class.getSimpleName();

    private final Context context;
    private final List<Frame> frames;

    public FrameAdapter(Context context, RecyclerView recyclerView, List<Frame> frames) {
        super(recyclerView);
        this.context = context;
        this.frames = frames;
    }

    @Override
    public long getItemId(int position) {
        return frames.get(position).getId();
    }

    @Override
    public int getPositionForId(long id) {
        return frames.indexOf(new Frame(id, null));
    }

    @Override
    public boolean move(int fromPosition, int toPosition) {
        frames.add(toPosition, frames.remove(fromPosition));
        return true;
    }

    @Override
    public FrameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_frame, parent, false);
        FrameViewHolder holder = new FrameViewHolder(this, view);
        view.setOnClickListener(holder);
        view.setOnLongClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(FrameViewHolder holder, int position) {
        if( frames.get(position) != null ) {
            Glide.with(context).load(frames.get(position).getImage()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return frames.size();
    }


    static class FrameViewHolder extends DragSortAdapter.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.frame_item_image)
        ImageView image;

        public FrameViewHolder(DragSortAdapter<?> dragSortAdapter, View itemView) {
            super(dragSortAdapter, itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "clicked");
        }

        @Override
        public boolean onLongClick(View v) {
            startDrag();
            return true;
        }

        @Override
        public View.DragShadowBuilder getShadowBuilder(View itemView, Point touchPoint) {
            return new NoForegroundShadowBuilder(itemView, touchPoint);
        }
    }
}
