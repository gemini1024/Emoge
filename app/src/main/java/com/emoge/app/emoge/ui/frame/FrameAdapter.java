package com.emoge.app.emoge.ui.frame;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

    private static final int MAX_ITEM_SIZE = 10;

    private List<Frame> frames;

    public FrameAdapter(@NonNull RecyclerView recyclerView,
                        @NonNull List<Frame> frames) {
        super(recyclerView);
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
        final Frame frame = getItem(position);

        holder.image.setImageBitmap(frame.getBitmap());
        holder.image.setVisibility(getDraggingId() == frame.getId() ? View.INVISIBLE : View.VISIBLE);
        holder.image.postInvalidate();
    }

    @Override
    public int getItemCount() {
        return frames.size();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        clear();
    }

    // 기본 조작
    @NonNull
    public List<Frame> getFrames() {
        return Collections.unmodifiableList(frames);
    }

    protected void setFrames(List<Frame> frames) {
        this.frames = frames;
    }

    public void reverse() {
        Collections.reverse(frames);
        notifyDataSetChanged();
    }

    public boolean addItem(@NonNull Frame item) {
        if( MAX_ITEM_SIZE > frames.size() ) {
            frames.add(item);
            notifyItemInserted(frames.size() - 1);
            return true;
        } else {
            // TODO : Alert
            Log.e(LOG_TAG, "item size 초과");
            return false;
        }
    }

    protected boolean inRange(int position) {
        return position >= 0 && position < frames.size();
    }

    @NonNull
    public Frame getItem(int position) {
        if(inRange(position)) {
            return frames.get(position);
        } else {
            Log.e(LOG_TAG, "잘못된 frame list 접근");
            return new Frame(0, null);
        }
    }

    public void removeItem(int position) {
        if(inRange(position)) {
            frames.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, frames.size());
        } else {
            Log.e(LOG_TAG, "잘못된 frame list 접근");
        }
    }

    public void clear() {
        if(!frames.isEmpty()) {
            for (Frame frame : frames) {
                if(frame.getBitmap() != null) {
                    frame.getBitmap().recycle();
                }
            }
            frames.clear();
            notifyDataSetChanged();
        }
    }


    // 뷰 홀더
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
            // TODO : Zoom or Remove
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
