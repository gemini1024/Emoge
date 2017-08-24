package com.emoge.app.emoge.ui.gallery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.utils.GlideAvRequester;
import com.emoge.app.emoge.utils.dialog.ImageDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 17. 8. 8.
 * Gallery 에 사진을 띄우기 위한 Adapter
 */

class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder>
        implements LocalImageLoadable, OnGalleryClickListener {
    private final String LOG_TAG = GalleryAdapter.class.getSimpleName();

    private Fragment fragment;                  // 호출한 Fragment (GalleryFragment)
    private ArrayList<File> files;              // File 리스트
    private List<String> format;                // 포함시킬 파일 Format (ImageFormatChecker)
    private boolean canSendMsg;                 // EventBus Message 발생 여부 (보정화면에서 true)

    GalleryAdapter(Fragment fragment, List<String> format, ArrayList<File> files, boolean canSendMsg) {
        this.fragment = fragment;
        this.format = format;
        this.files = files;
        this.canSendMsg = canSendMsg;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        GalleryViewHolder viewHolder = new GalleryViewHolder(view);
        viewHolder.setOnGalleryClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, final int position) {
        final File file = files.get(position);

        holder.loading.show();
        Glide.with(fragment).asBitmap().load(file)
                .listener(new GlideAvRequester<Bitmap>(holder.loading)).into(holder.image);
        if(ImageFormatChecker.GIF_FORMAT == format) {
            holder.type.setText(R.string.frame_gif_label);
            holder.type.setVisibility(View.VISIBLE);
        } else if(ImageFormatChecker.VIDEO_FORMAT == format) {
            holder.type.setText(file.getName());
            holder.type.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onItemClickListener(int position) {
        if(canSendMsg) {
            EventBus.getDefault().post(files.get(position));
        } else {
            showImageDialog(position);
        }
    }

    private void showImageDialog(final int position) {
        final ImageDialog imageDialog = new ImageDialog(fragment.getActivity(), Uri.fromFile(files.get(position)));
        imageDialog.setRemoveFileCallBack(new ImageDialog.RemoveFileCallBack() {
            @Override
            public void Task() {
                removeItem(position);
            }
        });
        imageDialog.show();
    }

    @Override
    public boolean isEmpty() {
        return files.isEmpty();
    }

    // 해당 포맷(format)만 추가 함.
    public boolean addItem(File file) {
        if(addItemWithoutNotify(file)) {
            notifyItemInserted(files.size() - 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean addItemWithoutNotify(@NonNull File file) {
        if(ImageFormatChecker.inFormat(file, format)) {
            files.add(file);
            return true;
        }
        return false;
    }

    private void removeItem(int position) {
        files.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, files.size()-position);
    }

    void clear() {
        files.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

}
