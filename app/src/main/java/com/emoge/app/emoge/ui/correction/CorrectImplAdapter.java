package com.emoge.app.emoge.ui.correction;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.ui.frame.FrameAddImplAdapter;
import com.emoge.app.emoge.ui.frame.FrameAdder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 17. 8. 1.
 */

public class CorrectImplAdapter extends FrameAddImplAdapter implements Correctable {
    private static final String LOG_TAG = CorrectImplAdapter.class.getSimpleName();

    private Correcter correcter;
    private List<Frame> stageFrames;

    public CorrectImplAdapter(@NonNull RecyclerView recyclerView,
                              @NonNull List<Frame> frames,
                              @NonNull FrameAdder frameAdder,
                              Correcter correcter) {
        super(recyclerView, frames, frameAdder);
        this.correcter = correcter;
        this.stageFrames = new ArrayList<>();
    }

    @Override
    public boolean move(int fromPosition, int toPosition) {
        if(! stageFrames.isEmpty()) {
            stageFrames.add(toPosition, stageFrames.remove(fromPosition));
        }
        return super.move(fromPosition, toPosition);
    }

    @NonNull
    @Override
    public Frame getItem(int position) {
        if(inRange(position)) {
            if(stageFrames.isEmpty()) {
                Log.d(LOG_TAG, "stage empty");
                return getFrames().get(position);
            } else {
                Log.d(LOG_TAG, String.valueOf(stageFrames.get(position).getId()));
                return stageFrames.get(position);
            }
        } else {
            Log.e(LOG_TAG, "잘못된 frame list 접근");
            return new Frame(0, null);
        }
    }

    @Override
    public void setBrightness(int value) {
        clearStage();
        stageFrames = correcter.setBrightness(getFrames(), value);
        notifyDataSetChanged();
        if(stageFrames.isEmpty()) {
            Log.d(LOG_TAG, "setBrightness empty");
        }
    }

    @Override
    public void setContrast(int value) {
        clearStage();
        stageFrames = correcter.setContrast(getFrames(), value);
        notifyDataSetChanged();
    }

    @Override
    public void setGamma(int value) {
        clearStage();
        stageFrames = correcter.setGamma(getFrames(), value);
        notifyDataSetChanged();
    }

    @Override
    public void apply() {
        super.clear();
        super.setFrames(stageFrames);
        stageFrames = new ArrayList<>();
    }

    @Override
    public void reset() {
        clearStage();
        stageFrames = new ArrayList<>();
    }

    private void clearStage() {
        for(Frame frame : stageFrames) {
            if(frame.getBitmap() != null) {
                frame.getBitmap().recycle();
            }
        }
        stageFrames.clear();
        Log.d(LOG_TAG, "cleared");
    }
}
