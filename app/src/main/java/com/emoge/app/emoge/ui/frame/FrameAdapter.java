package com.emoge.app.emoge.ui.frame;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
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

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jh on 17. 7. 25.
 * Draggable RecyclerView Adapter
 */

public class FrameAdapter extends DragSortAdapter<FrameAdapter.FrameViewHolder> {
    private static final String LOG_TAG = FrameAdapter.class.getSimpleName();

    private final Context context;
    private final List<Frame> frames;

    public FrameAdapter(@NonNull Context context,
                        @NonNull RecyclerView recyclerView,
                        @NonNull List<Frame> frames) {
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

    public List<Frame> getFrames() {
        return Collections.unmodifiableList(frames);
    }

    public void addItem(@NonNull Frame item) {
        frames.add(item);
        notifyItemInserted(frames.size()-1);
    }

    private boolean inRange(int position) {
        return position >= 0 && position < frames.size();
    }

    @NonNull
    public Frame getItem(int position) {
        if(inRange(position)) {
            return frames.get(position);
        } else {
            Log.e(LOG_TAG, context.getString(R.string.inaccessible_frame_list));
            return frames.get(0);
        }
    }

    public void deleteItem(int position) {
        if(inRange(position)) {
            frames.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, frames.size());
        } else {
            Log.e(LOG_TAG, context.getString(R.string.inaccessible_frame_list));
        }
    }

    public void clear() {
        frames.clear();
        notifyDataSetChanged();
    }


    static class FrameViewHolder extends DragSortAdapter.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.frame_item_image)
        ImageView image;

        FrameViewHolder(DragSortAdapter<?> dragSortAdapter, View itemView) {
            super(dragSortAdapter, itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
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
