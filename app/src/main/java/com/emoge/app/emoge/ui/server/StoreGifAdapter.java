package com.emoge.app.emoge.ui.server;

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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidviewhover.BlurLayout;
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.MyStoreGif;
import com.emoge.app.emoge.model.StoreGif;
import com.emoge.app.emoge.ui.view.HoverViews;
import com.emoge.app.emoge.utils.GifDownloadTask;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by jh on 17. 8. 3.
 * 서버에 저장된 이미지들 불러오기 위한 Adapter
 */

class StoreGifAdapter extends RecyclerView.Adapter<StoreGifViewHolder> {
    private final String LOG_TAG = StoreGifAdapter.class.getSimpleName();

    private Fragment fragment;
    private ArrayList<StoreGif> gifs;
    private RequestOptions placeholderOption;
    private Realm realm;

    StoreGifAdapter(Fragment fragment, ArrayList<StoreGif> gifs) {
        this.fragment = fragment;
        this.gifs = gifs;
        this.placeholderOption = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565).placeholder(R.drawable.img_loading);
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    public StoreGifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_server_img, parent, false);
        return new StoreGifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StoreGifViewHolder holder, int position) {
        final StoreGif item = gifs.get(position);

        Glide.with(fragment).load(Uri.parse(item.getDownloadUrl())).apply(placeholderOption).into(holder.image);
        holder.title.setText(item.getTitle());

        setHoverByGif(holder.container, item);
    }


    private void setHoverByGif(BlurLayout blurLayout, final StoreGif storeGif) {
        final HoverViews hover = new HoverViews(fragment.getContext(), blurLayout);

        String url = storeGif.getDownloadUrl();
        final String id = url.substring( url.lastIndexOf('/')+1, url.length());
        hover.buildHoverView();
        if( realm.where(MyStoreGif.class).equalTo(MyStoreGif.KEY_FIELD, id).findFirst() != null) {
            hover.setFavoriteSelected(true);
        } else {
            hover.setFavoriteSelected(false);
        }

        hover.setDownloadButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada)
                        .duration(550)
                        .playOn(v);
                new GifDownloadTask(fragment.getActivity()).execute(storeGif);
            }
        });
        hover.setFavoriteButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada)
                        .duration(HoverViews.BLUR_DURATION)
                        .playOn(v);
                hover.dismissHover();
                applyRealm(hover, id, storeGif);
            }
        });
    }

    private void applyRealm(final HoverViews hover, final String id, final StoreGif storeGif) {
        final MyStoreGif myStoreGif = realm.where(MyStoreGif.class)
                .equalTo(MyStoreGif.KEY_FIELD, id).findFirst();
        if(myStoreGif != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    myStoreGif.deleteFromRealm();
                    if( isMyStoreGif(storeGif) ) {
                        gifs.remove(storeGif);
                        notifyDataSetChanged();
                        hover.removeHover();
                    } else {
                        hover.setFavoriteSelected(false);
                    }
                    Toast.makeText(fragment.getContext(), R.string.remove_favorite, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    MyStoreGif myStoreGif = realm.createObject(MyStoreGif.class, id);
                    myStoreGif.setAll(storeGif);
                    hover.setFavoriteSelected(true);
                    Toast.makeText(fragment.getContext(), R.string.add_favorite, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return gifs.size();
    }




    // 조작
    void addItem(StoreGif storeGif) {
        gifs.add(storeGif);
        notifyItemInserted(gifs.size()-1);
    }

    void clear() {
        gifs.clear();
        notifyDataSetChanged();
    }

    boolean isEmpty() {
        return gifs.isEmpty();
    }

    boolean isMyStoreGif(StoreGif storeGif) {
        return storeGif.getFavorite() == -1;
    }


}
