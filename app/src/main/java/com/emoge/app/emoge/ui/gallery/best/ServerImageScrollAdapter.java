package com.emoge.app.emoge.ui.gallery.best;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.StoreGif;
import com.emoge.app.emoge.ui.gallery.OnGalleryClickListener;
import com.emoge.app.emoge.utils.GifDownloadTask;
import com.emoge.app.emoge.utils.GlideAvRequester;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jh on 17. 8. 17.
 * Gallery 에 서버 사진을 띄우기 위한 Adapter
 */

public class ServerImageScrollAdapter extends RecyclerView.Adapter<ServerImageViewHolder>
        implements OnGalleryClickListener {

    private static final int FOOTER_VIEW = 9000;

    private Activity activity;
    private List<StoreGif> storeGifs;

    public ServerImageScrollAdapter(Activity activity, List<StoreGif> storeGifs) {
        this.activity = activity;
        this.storeGifs = storeGifs;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == storeGifs.size()) {
            return FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public ServerImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_server_img, parent, false);
        ServerImageViewHolder viewHolder;
        if(viewType == FOOTER_VIEW) {
            viewHolder = new FooterViewHolder(activity, view);
        } else {
            viewHolder = new BestImageViewHolder(this, view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ServerImageViewHolder holder, int position) {
        if(position < storeGifs.size()) {
            holder.rank.setText(String.format("%d위 : %s", position+1, storeGifs.get(position).getTitle()));
            holder.loading.show();
            Glide.with(activity)
                    .load(Uri.parse(storeGifs.get(position).getDownloadUrl()))
                    .apply(RequestOptions.centerCropTransform())
                    .listener(new GlideAvRequester<Drawable>(holder.loading))
                    .into(holder.image);
        } else {    // Footer
            holder.rank.setVisibility(View.GONE);
            holder.loading.hide();
            Glide.with(activity)
                    .load("")
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_more))
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return storeGifs.size()+1;
    }

    @Override
    public void onItemClickListener(final int position) {
        SweetDialogs.showWarningDialog(activity, R.string.download_gif_title, R.string.download_favorite_content)
                .setConfirmText(activity.getString(R.string.download_gif_title))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        new GifDownloadTask(activity).execute(storeGifs.get(position));
                    }
                });
    }
}
