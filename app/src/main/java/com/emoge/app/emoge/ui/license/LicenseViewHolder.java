package com.emoge.app.emoge.ui.license;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.emoge.app.emoge.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jh on 17. 8. 13.
 */

class LicenseViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.license_item_title)
    TextView title;
    @BindView(R.id.license_item_content)
    TextView content;

    LicenseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
