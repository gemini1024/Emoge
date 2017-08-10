package com.emoge.app.emoge.ui.history;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.model.History;
import com.emoge.app.emoge.ui.correction.CorrectImplAdapter;
import com.emoge.app.emoge.ui.correction.Correcter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 17. 8. 9.
 */

public class HistoryImplAdapter extends RecyclerView.Adapter<HistoryViewHolder> implements HistoryAccessible {
    private final String LOG_TAG = HistoryImplAdapter.class.getSimpleName();

    private CorrectImplAdapter frameAdapter;
    private ArrayList<History> histories;
    private List<Frame> originalFrames;
    private int lastRefIndex;

    public HistoryImplAdapter(CorrectImplAdapter frameAdapter, ArrayList<History> histories) {
        this.frameAdapter = frameAdapter;
        this.histories = histories;
        this.originalFrames = new ArrayList<>();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        final History history = histories.get(position);

        if(position == histories.size()-1) {
            holder.itemView.setText("원본");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rollbackOrigin();
                }
            });
        } else {
            holder.itemView.setText(String.valueOf(histories.size()-position-1));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rollbackPosition(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public void setOriginalFrames() {
        if(originalFrames.isEmpty()) {
            histories.add(new History(Correcter.DEFAULT_BRIGHTNESS,
                    Correcter.DEFAULT_CONTRAST, Correcter.DEFAULT_GAMMA));
        }
        List<Frame> frames = frameAdapter.getFrames();
        for(Frame frame : frames) {
            originalFrames.add(new Frame(frame.getId(),
                    frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true)));
        }
        Log.i(LOG_TAG, "original frame size : "+originalFrames.size());
    }


    @Override
    public void addHistory() {
        History history = frameAdapter.getModifiedValues();
        if(!isDefaultHistory(history)) {
            removeHistory(lastRefIndex);
            histories.add(0, frameAdapter.getModifiedValues());
            notifyDataSetChanged();
            lastRefIndex = 0;
        }
    }

    private boolean isDefaultHistory(History history) {
        return history.getAppliedFilter() == null
                && history.getModifiedBrightness() == Correcter.DEFAULT_BRIGHTNESS
                && history.getModifiedContrast() == Correcter.DEFAULT_CONTRAST
                && history.getModifiedGamma() == Correcter.DEFAULT_GAMMA;
    }

    @Override
    public void rollbackPosition(int position) {
        rollbackOrigin();
        for(int i=histories.size()-2; i>=position; i--) {
            History history = histories.get(i);
            if(history.getAppliedFilter() == null) {
                correct(Correcter.CORRECT_BRIGHTNESS, history.getModifiedBrightness());
                correct(Correcter.CORRECT_CONTRAST, history.getModifiedContrast());
                correct(Correcter.CORRECT_GAMMA, history.getModifiedGamma());
            } else {
                frameAdapter.setFilter(history.getAppliedFilter());
            }
        }
        Log.i(LOG_TAG, "rollback : "+position);
        lastRefIndex = position;
    }

    private void correct(int type, int value) {
        frameAdapter.correct(type, value);
        frameAdapter.clearPreviousFrames();
        frameAdapter.apply();
    }

    @Override
    public void rollbackOrigin() {
        frameAdapter.clear();
        for(Frame frame : originalFrames) {
            frameAdapter.addItem(new Frame(frame.getId(),
                    frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true)));
        }
        Log.i(LOG_TAG, "rollback all");
        lastRefIndex = histories.size()-1;
    }

    private void removeHistory(int index) {
        for(int i=index-1; i>=0; i--) {
            histories.remove(i);
            Log.d(LOG_TAG, index+"");
        }
    }

    @Override
    public void clearHistory() {
        histories.clear();
        if(!originalFrames.isEmpty()) {
            for(Frame frame : originalFrames) {
                if(frame.getBitmap() != null) {
                    frame.getBitmap().recycle();
                }
            }
            originalFrames.clear();
            notifyDataSetChanged();
        }
    }

}
