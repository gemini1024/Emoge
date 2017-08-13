package com.emoge.app.emoge.ui.gallery;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder>
        implements LocalImageLoadable {
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
    public void onBindViewHolder(final GalleryViewHolder holder, final int position) {
        final File file = files.get(position);

        Glide.with(fragment).asBitmap().load(file).apply(placeholderOption).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canSendMsg) {
                    EventBus.getDefault().post(file);
                } else {
                    final ImageDialog imageDialog = new ImageDialog(fragment.getActivity(), Uri.fromFile(file));
                    imageDialog.setRemoveButtonListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeFile(position);
                            imageDialog.dismiss();
                        }
                    });
                    imageDialog.show();
                }
            }
        });
        if(ImageFormatChecker.GIF_FORMAT == format) {
            holder.type.setText("GIF");
            holder.type.setVisibility(View.VISIBLE);
        }
    }

    private void removeFile(int position) {
        if(files.get(position).delete()) {
            removeItem(position);
            Toast.makeText(fragment.getContext(), R.string.remove_file, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(fragment.getContext(), R.string.err_remove_file, Toast.LENGTH_SHORT).show();
        }
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
    public boolean addItemWithoutNotify(File file) {
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
