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
import com.emoge.app.emoge.ui.correction.Correctable;
import com.emoge.app.emoge.ui.correction.Correcter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 17. 8. 9.
 * Correction History Adapter
 */

public class HistoryImplAdapter extends RecyclerView.Adapter<HistoryViewHolder> implements HistoryAccessible {
    private final String LOG_TAG = HistoryImplAdapter.class.getSimpleName();

    private Correctable correctImplAdapter;
    private ArrayList<History> histories;
    private List<Frame> originalFrames;
    private int lastRefIndex;

    public HistoryImplAdapter(Correctable correctImplAdapter, ArrayList<History> histories) {
        this.correctImplAdapter = correctImplAdapter;
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
        List<Frame> frames = correctImplAdapter.getFrames();
        for(Frame frame : frames) {
            originalFrames.add(new Frame(frame.getId(),
                    frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true)));
        }
        Log.i(LOG_TAG, "original frame size : "+originalFrames.size());
    }


    @Override
    public void addHistory() {
        History history = correctImplAdapter.getModifiedValues();
        if(!isDefaultHistory(history)) {
            removeHistory(lastRefIndex);
            histories.add(0, correctImplAdapter.getModifiedValues());
            notifyDataSetChanged();
            lastRefIndex = 0;
        }
    }

    // 한 단계씩 되돌리기. ( 원본인경우 false )
    @Override
    public boolean rollbackOneStep() {
        if(lastRefIndex < histories.size()-1) {
            rollbackPosition(lastRefIndex+1);
            return true;
        } else {
            return false;
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
                correctImplAdapter.setFilter(history.getAppliedFilter());
                correctImplAdapter.clearPreviousFrames();
                correctImplAdapter.apply();
            }
        }
        Log.i(LOG_TAG, "rollback : "+position);
        lastRefIndex = position;
    }

    private void correct(int type, int value) {
        correctImplAdapter.correct(type, value);
        correctImplAdapter.clearPreviousFrames();
        correctImplAdapter.apply();
    }

    @Override
    public void rollbackOrigin() {
        correctImplAdapter.clear();
        for(Frame frame : originalFrames) {
            correctImplAdapter.addItem(new Frame(frame.getId(),
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
        lastRefIndex = 0;
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
