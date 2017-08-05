package com.emoge.app.emoge.ui.frame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.model.FrameStatusMessage;
import com.emoge.app.emoge.utils.dialog.ImageDialog;
import com.makeramen.dragsortadapter.DragSortAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.Comparator;
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

    private int frameWidth;
    private int frameHeight;

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
                    final ImageDialog imageDialog = new ImageDialog(activity,
                            frames.get(getPositionForId(id)).getBitmap());
                    imageDialog.setRemoveButtonListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeItem(id);
                            imageDialog.dismiss();
                        }
                    }).show();
                }
            });
        }
    }


    /**
     * 기본조작들.
     * CRUD 순
     */
    // C
    public boolean addItem(@NonNull Frame item) {
        if (addItemWithoutNotify(item)) {
            notifyItemInserted(frames.size() - 1);
            return true;
        } else {
            return false;
        }
    }

    boolean addItemWithoutNotify(@NonNull Frame item) {
        if( MAX_ITEM_SIZE > frames.size() ) {
            item.setBitmap(applyPreFrameAttr(item.getBitmap()));
            frames.add(item);
            return true;
        } else {
            EventBus.getDefault().post(FrameStatusMessage.FULL);
            return false;
        }
    }

    long nextId() {
        if(frames.isEmpty()) {
            return 1;
        } else {
            return Collections.max(frames, new Comparator<Frame>() {
                @Override
                public int compare(Frame o1, Frame o2) {
                    return Long.compare(o1.getId(), o2.getId());
                }
            }).getId() + 1;
        }
    }

    @NonNull
    private Bitmap applyPreFrameAttr(@NonNull Bitmap source) {
        Bitmap argbBitmap;
        if(frames.isEmpty()) {
            frameWidth = source.getWidth();
            frameHeight = source.getHeight();
            argbBitmap = source.copy(Bitmap.Config.ARGB_8888, true);
            source.recycle();
        } else {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(source, frameWidth, frameHeight, true);
            argbBitmap = scaledBitmap.copy(Bitmap.Config.ARGB_8888, true);
            scaledBitmap.recycle();
            source.recycle();
        }
        return argbBitmap;
    }


    // R
    public boolean isEmpty() {
        return frames.isEmpty();
    }

    public boolean isFull() {
        return MAX_ITEM_SIZE <= frames.size();
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

    @NonNull
    public List<Frame> getFrames() {
        return Collections.unmodifiableList(frames);
    }


    // U
    protected void setFrames(List<Frame> frames) {
        this.frames = frames;
    }

    protected void reverse() {
        Collections.reverse(frames);
        notifyDataSetChanged();
    }



    // D
    public void removeItem(int position) {
        if(inRange(position)) {
            frames.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, frames.size());
            EventBus.getDefault().post(FrameStatusMessage.NOT_FULL);
        } else {
            Log.e(LOG_TAG, "잘못된 frame list 접근");
        }
    }

    public void removeItem(long id) {
        removeItem(getPositionForId(id));
    }

    protected void clear() {
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
