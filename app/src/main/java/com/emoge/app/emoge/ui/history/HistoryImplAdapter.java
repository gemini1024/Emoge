package com.emoge.app.emoge.ui.history;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.model.History;
import com.emoge.app.emoge.ui.correction.Correctable;
import com.emoge.app.emoge.ui.correction.Correcter;
import com.emoge.app.emoge.utils.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 17. 8. 9.
 * Correction History Adapter
 */

public class HistoryImplAdapter extends RecyclerView.Adapter<HistoryViewHolder> implements HistoryAccessible {
    private final String LOG_TAG = HistoryImplAdapter.class.getSimpleName();

    private Activity activity;
    private Correctable correctImplAdapter;
    private ArrayList<History> histories;
    private List<Frame> originalFrames;
    private int lastRefIndex;

    public HistoryImplAdapter(Activity activity, Correctable correctImplAdapter, ArrayList<History> histories) {
        this.activity = activity;
        this.correctImplAdapter = correctImplAdapter;
        this.histories = histories;
        this.originalFrames = new ArrayList<>();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        HistoryViewHolder viewHolder = new HistoryViewHolder(view);
        viewHolder.setHistoryAccessible(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        holder.itemView.setText(position == 0?
                holder.itemView.getContext().getString(R.string.history_origin) : String.valueOf(position));
        holder.cursor.setVisibility(lastRefIndex == position? View.VISIBLE : View.GONE);
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
        Logger.i(LOG_TAG, "original frame size : "+originalFrames.size());
    }

    public boolean isEmpty() {
        return histories == null || histories.size() < 2;
    }


    @Override
    public void addHistory() {
        History history = correctImplAdapter.getModifiedValues();
        if(!isDefaultHistory(history)) {
            removeHistory(lastRefIndex);
            histories.add(correctImplAdapter.getModifiedValues());
            lastRefIndex = histories.size()-1;
            notifyDataSetChanged();
        }
    }

    // 한 단계씩 되돌리기. ( 원본인경우 false )
    @Override
    public synchronized boolean rollbackOneStep() {
        if(lastRefIndex > 0) {
            rollbackPosition(lastRefIndex-1);
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
    public synchronized void rollbackPosition(int position) {
        rollbackOrigin();
        for(int i=1; i<=position; i++) {
            EventBus.getDefault().post(histories.get(i));
        }
        Logger.i(LOG_TAG, "rollback : "+position);
        lastRefIndex = position;
        notifyDataSetChanged();
    }

    @Override
    public void rollbackOrigin() {
        correctImplAdapter.clear();
        for(Frame frame : originalFrames) {
            correctImplAdapter.addItem(new Frame(frame.getId(),
                    frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true)));
        }
        Logger.i(LOG_TAG, "rollback all");
        lastRefIndex = histories.size()-1;
    }

    private void removeHistory(int index) {
        for(int i=histories.size()-1; i>index; i--) {
            histories.remove(i);
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
                    frame.setBitmap(null);
                }
            }
            originalFrames.clear();
            notifyDataSetChanged();
        }
    }

}
