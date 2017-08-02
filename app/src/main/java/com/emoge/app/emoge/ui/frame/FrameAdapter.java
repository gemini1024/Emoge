package com.emoge.app.emoge.ui.frame;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.utils.CustomDialogController;
import com.emoge.app.emoge.utils.Dialogs;
import com.makeramen.dragsortadapter.DragSortAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by jh on 17. 7. 25.
 * Draggable RecyclerView Adapter
 */

public class FrameAdapter extends DragSortAdapter<FrameViewHolder> {
    private static final String LOG_TAG = FrameAdapter.class.getSimpleName();

    private static final int MAX_ITEM_SIZE = 10;

    private List<Frame> frames;
    private Activity activity;

    FrameAdapter(@NonNull RecyclerView recyclerView,
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
        return new FrameViewHolder(this, view);
    }

    @Override
    public void onBindViewHolder(final FrameViewHolder holder, int position) {
        final Frame frame = getItem(position);

        holder.image.setImageBitmap(frame.getBitmap());
        holder.image.setVisibility(getDraggingId() == frame.getId() ? View.INVISIBLE : View.VISIBLE);
        holder.image.postInvalidate();
        setDialog(holder.image, frame.getId());
        holder.image.setOnLongClickListener(holder);
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

    public void setDialogTargetWhenOnItemClick(Activity activity) {
        this.activity = activity;
    }

    private void setDialog(View view, final long id) {
        if(activity != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CustomDialogController controller = Dialogs.showImageDialog(activity,
                            frames.get(getPositionForId(id)).getBitmap());
                    controller.setRemoveButtonListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    removeItem(id);
                                    controller.dismiss();
                                }
                            });
                }
            });
        }
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
            try {
                frames.add(item);
                notifyItemInserted(frames.size() - 1);
                return true;
            } catch (IllegalStateException e) {
                Log.e(LOG_TAG, e.getClass().getSimpleName(), e);
                return false;
            }
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

    public void removeItem(long id) {
        removeItem(getPositionForId(id));
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
}
