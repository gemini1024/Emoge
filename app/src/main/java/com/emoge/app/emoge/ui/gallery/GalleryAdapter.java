package com.emoge.app.emoge.ui.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.emoge.app.emoge.R;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 17. 8. 8.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {
    private final String LOG_TAG = GalleryAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<File> files;
    private List<String> format;
    private RequestOptions placeholderOption;
    private boolean canSendMsg;

    GalleryAdapter(Context context, List<String> format, ArrayList<File> files, boolean canSendMsg) {
        this.context = context;
        this.format = format;
        this.files = files;
        this.canSendMsg = canSendMsg;
        this.placeholderOption = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565).placeholder(R.drawable.img_no_image);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, int position) {
        final File file = files.get(position);

        Glide.with(context).load(file).apply(placeholderOption).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canSendMsg) {
                    EventBus.getDefault().post(file);
                }
            }
        });
    }

    boolean addItem(File file) {
        if(ImageFormatChecker.inFormat(file, format)) {
            files.add(file);
            return true;
        }
        return false;
    }


    boolean addItemWithoutNotify(File file) {
        if(addItem(file)) {
            return true;
        }
        return false;
    }

    void clear() {
        clear();
        notifyDataSetChanged();
    }

    void clearWithoutNotify() {
        files.clear();
    }


    @Override
    public int getItemCount() {
        return files.size();
    }

}
