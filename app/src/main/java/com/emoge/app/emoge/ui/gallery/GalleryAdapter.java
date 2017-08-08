package com.emoge.app.emoge.ui.gallery;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.utils.dialog.ImageDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 17. 8. 8.
 */

class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {
    private final String LOG_TAG = GalleryAdapter.class.getSimpleName();

    private Fragment fragment;
    private ArrayList<File> files;
    private List<String> format;
    private RequestOptions placeholderOption;
    private boolean canSendMsg;

    GalleryAdapter(Fragment fragment, List<String> format, ArrayList<File> files, boolean canSendMsg) {
        this.fragment = fragment;
        this.format = format;
        this.files = files;
        this.canSendMsg = canSendMsg;
        this.placeholderOption = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565).placeholder(R.drawable.img_loading);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, int position) {
        final File file = files.get(position);

        Glide.with(fragment).load(file).apply(placeholderOption).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canSendMsg) {
                    EventBus.getDefault().post(file);
                } else {
                    new ImageDialog(fragment.getActivity(), Uri.fromFile(file)).show();
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
