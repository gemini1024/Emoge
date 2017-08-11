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
 * Gallery 에 사진을 띄우기 위한 Adapter
 */

class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {
    private final String LOG_TAG = GalleryAdapter.class.getSimpleName();

    private Fragment fragment;                  // 호출한 Fragment (GalleryFragment)
    private ArrayList<File> files;              // File 리스트
    private List<String> format;                // 포함시킬 파일 Format (ImageFormatChecker)
    private RequestOptions placeholderOption;   // PlaceHolder 적용 위함
    private boolean canSendMsg;                 // EventBus Message 발생 여부 (보정화면에서 true)

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

        Glide.with(fragment).asBitmap().load(file).apply(placeholderOption).into(holder.image);
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
        if(ImageFormatChecker.GIF_FORMAT == format) {
            holder.type.setText("GIF");
            holder.type.setVisibility(View.VISIBLE);
        }
    }

    // 해당 포맷(format)만 추가 함.
    boolean addItem(File file) {
        if(addItemWithoutNotify(file)) {
            notifyItemInserted(files.size() - 1);
            return true;
        }
        return false;
    }

    boolean addItemWithoutNotify(File file) {
        if(ImageFormatChecker.inFormat(file, format)) {
            files.add(file);
            return true;
        }
        return false;
    }

    void clear() {
        clearWithoutNotify();
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
