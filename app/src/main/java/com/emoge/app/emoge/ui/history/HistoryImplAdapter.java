package com.emoge.app.emoge.ui.history;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.model.PaletteMessage;
import com.emoge.app.emoge.ui.correction.CorrectImplAdapter;
import com.emoge.app.emoge.ui.correction.Correcter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 17. 8. 9.
 */

public class HistoryImplAdapter extends RecyclerView.Adapter<HistoryViewHolder> implements HistoryAccessible {
    private final String LOG_TAG = HistoryImplAdapter.class.getSimpleName();

    private Activity activity;
    private CorrectImplAdapter frameAdapter;
    private ArrayList<PaletteMessage> histories;
    private List<Frame> originalFrames;
    private String[] correctTitles;

    public HistoryImplAdapter(Activity activity, CorrectImplAdapter frameAdapter, ArrayList<PaletteMessage> histories) {
        this.activity = activity;
        this.frameAdapter = frameAdapter;
        this.histories = histories;
        this.originalFrames = new ArrayList<>();
        this.correctTitles = activity.getResources().getStringArray(R.array.correct_title);
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        final PaletteMessage history = histories.get(position);

        if(position == 0) {
            holder.itemView.setText("원본");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rollbackOrigin();
                }
            });
        } else if(history.getType() >= 0 && history.getType() < correctTitles.length) {
            holder.itemView.setText(correctTitles[history.getType()]);
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
            histories.add(new PaletteMessage(Correcter.CORRECT_ADD, 0));
        }
        List<Frame> frames = frameAdapter.getFrames();
        for(Frame frame : frames) {
            originalFrames.add(new Frame(frame.getId(),
                    frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true)));
        }
        Log.i(LOG_TAG, "original frame size : "+originalFrames.size());
    }


    @Override
    public void addHistory(PaletteMessage correction) {
        histories.add(correction);
        notifyItemInserted(histories.size() - 1);
    }

    @Override
    public void rollbackPosition(int position) {
        rollbackOrigin();
        for(int i=0; i<position; i++) {
            PaletteMessage paletteMessage = histories.get(i);
            frameAdapter.correct(paletteMessage.getType(), paletteMessage.getValue());
            frameAdapter.apply();
        }
        Log.i(LOG_TAG, "rollback : "+position);
    }

    @Override
    public void rollbackOrigin() {
        frameAdapter.clear();
        for(Frame frame : originalFrames) {
            frameAdapter.addItem(new Frame(frame.getId(),
                    frame.getBitmap().copy(Bitmap.Config.ARGB_8888, true)));
        }
        Log.i(LOG_TAG, "rollback all");
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
