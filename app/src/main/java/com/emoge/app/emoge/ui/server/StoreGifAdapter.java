package com.emoge.app.emoge.ui.server;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.StoreGif;

import java.util.ArrayList;

/**
 * Created by jh on 17. 8. 3.
 * 서버에 저장된 이미지들 불러오기 위한 Adapter
 */

class StoreGifAdapter extends RecyclerView.Adapter<StoreGifViewHolder> {
    private final String LOG_TAG = StoreGifAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<StoreGif> gifs;
    private RequestOptions placeholderOption;

    StoreGifAdapter(Context context, ArrayList<StoreGif> gifs) {
        this.context = context;
        this.gifs = gifs;
        this.placeholderOption = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565).placeholder(R.drawable.img_loading);
    }

    @Override
    public StoreGifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_server_img, parent, false);
        return new StoreGifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StoreGifViewHolder holder, int position) {
        final StoreGif item = gifs.get(position);

        Glide.with(context).load(Uri.parse(item.getDownloadUrl())).apply(placeholderOption).into(holder.image);
        holder.title.setText(item.getTitle());
        // TODO : favorite

    }

    void addItem(StoreGif storeGif) {
        gifs.add(storeGif);
        notifyItemInserted(gifs.size()-1);
    }

    void clear() {
        gifs.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return gifs.size();
    }

}
