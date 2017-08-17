package com.emoge.app.emoge.ui.gallery.best;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
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
    private int[] rankColors;

    public ServerImageScrollAdapter(Activity activity, List<StoreGif> storeGifs) {
        this.activity = activity;
        this.storeGifs = storeGifs;
        getRankColors();
    }

    private void getRankColors() {
        TypedArray ta = activity.getResources().obtainTypedArray(R.array.rank_color);
        rankColors = new int[ta.length()];
        for(int i=0; i<ta.length(); i++) {
            rankColors[i] = ta.getColor(i, 0);
        }
        ta.recycle();
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
            if(position < rankColors.length) {
                holder.image.setLabelText(String.format(activity.getString(R.string.rank_number), position+1));
                holder.image.setLabelBackgroundColor(rankColors[position]);
            }
            holder.loading.show();
            Glide.with(activity)
                    .load(Uri.parse(storeGifs.get(position).getDownloadUrl()))
                    .apply(RequestOptions.centerCropTransform())
                    .listener(new GlideAvRequester<Drawable>(holder.loading))
                    .into(holder.image);
        } else {    // Footer
            holder.image.setLabelText(activity.getString(R.string.rank_out));
            holder.image.setLabelBackgroundColor(ResourcesCompat
                    .getColor(activity.getResources(), R.color.colorPrimaryDark, null));
            holder.image.setBackgroundColor(ResourcesCompat
                    .getColor(activity.getResources(), R.color.colorPrimary, null));
            holder.loading.hide();
            Glide.with(activity)
                    .load("")
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_more_bk))
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
