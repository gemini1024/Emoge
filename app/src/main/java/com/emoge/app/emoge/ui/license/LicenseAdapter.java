package com.emoge.app.emoge.ui.license;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.License;

import java.util.List;

/**
 * Created by jh on 17. 8. 13.
 */

class LicenseAdapter extends RecyclerView.Adapter<LicenseViewHolder> {

    private List<License> licenses;

    public LicenseAdapter(List<License> licenses) {
        this.licenses = licenses;
    }

    @Override
    public LicenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_license, parent, false);
        return new LicenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LicenseViewHolder holder, int position) {
        final License item = licenses.get(position);

        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return licenses.size();
    }
}
