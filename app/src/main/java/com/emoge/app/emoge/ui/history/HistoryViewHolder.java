package com.emoge.app.emoge.ui.history;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.emoge.app.emoge.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jh on 17. 8. 9.
 */

class HistoryViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.history_item)
    TextView itemView;

    HistoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
