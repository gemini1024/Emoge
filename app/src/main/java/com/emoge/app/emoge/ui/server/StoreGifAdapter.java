package com.emoge.app.emoge.ui.server;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

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
    private DatabaseReference database;
    private ArrayList<String> serverItemKeys;


    StoreGifAdapter(@NonNull Fragment fragment, @NonNull ArrayList<StoreGif> gifs,
                    @NonNull Realm realm, @Nullable DatabaseReference database) {
        this.fragment = fragment;
        this.gifs = gifs;
        this.placeholderOption = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565).placeholder(R.drawable.img_loading);
        this.realm = realm;
        this.database = database;                   // 저장소 탭인 경우 null
        this.serverItemKeys = new ArrayList<>();
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
        if(item.getFavorite() > -1) {
            holder.favorite.setText(String.valueOf(item.getFavorite()));
        } else {
            holder.favoriteIcon.setVisibility(View.GONE);
            holder.favorite.setVisibility(View.GONE);
        }

        setHoverByGif(holder.container, position);
    }

    private boolean inRealm(int position) {
        return realm.where(MyStoreGif.class).equalTo(MyStoreGif.KEY_FIELD,
                serverItemKeys.get(position)).findFirst() != null;
    }

    // HoverView 로 설정
    private void setHoverByGif(BlurLayout blurLayout, final int position) {
        final HoverViews hover = new HoverViews(fragment.getContext(), blurLayout);

        hover.buildHoverView();
        if(database == null || inRealm(position)) {
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
                new GifDownloadTask(fragment.getActivity()).execute(gifs.get(position));
            }
        });
        hover.setFavoriteButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada)
                        .duration(HoverViews.BLUR_DURATION)
                        .playOn(v);
                hover.dismissHover();
                applyRealm(hover, position);
            }
        });
    }

    // Realm 접근
    private void applyRealm(final HoverViews hover, final int position) {
        if(database == null || inRealm(position)) {
            // 담아가기 해제
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(MyStoreGif.class)
                            .equalTo(MyStoreGif.KEY_FIELD, serverItemKeys.get(position))
                            .findFirst().deleteFromRealm();
                    if( isMyStoreGif(gifs.get(position)) ) {
                        String[] categories = fragment.getResources().getStringArray(R.array.server_category);
                        for(String category : categories) {
                            updateFavorite(FirebaseDatabase.getInstance().getReference(category),
                                    serverItemKeys.get(position), gifs.get(position), -1);
                        }
                        removeItem(position);
                        hover.removeHover();
                    } else {
                        updateFavorite(database, serverItemKeys.get(position), gifs.get(position), -1);
                        hover.setFavoriteSelected(false);
                        notifyItemChanged(position);
                    }
                    Toast.makeText(fragment.getContext(), R.string.remove_favorite, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // 담아가기
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    updateFavorite(database, serverItemKeys.get(position), gifs.get(position), 1);
                    MyStoreGif myStoreGif = realm.createObject(MyStoreGif.class, serverItemKeys.get(position));
                    myStoreGif.setAll(gifs.get(position));
                    hover.setFavoriteSelected(true);
                    notifyItemChanged(position);
                    Toast.makeText(fragment.getContext(), R.string.add_favorite, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateFavorite(DatabaseReference db, String key, final StoreGif targetGif, int value) {
        targetGif.setFavorite(targetGif.getFavorite()+value);
        if(db.child(key) != null)
        db.child(key).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                StoreGif serverGif = mutableData.getValue(StoreGif.class);
                if(serverGif != null) {
                    serverGif.setAll(targetGif);
                }
                mutableData.setValue(serverGif);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return gifs.size();
    }




    // 조작
    void addItem(String key, StoreGif storeGif) {
        serverItemKeys.add(key);
        gifs.add(storeGif);
        notifyItemInserted(gifs.size()-1);
    }

    void removeItem(int position) {
        gifs.remove(gifs.get(position));
        serverItemKeys.remove(serverItemKeys.get(position));
        notifyDataSetChanged();
    }

    void clear() {
        if(gifs != null) {
            gifs.clear();
        }
        if(serverItemKeys != null) {
            serverItemKeys.clear();
        }
        notifyDataSetChanged();
    }

    boolean isEmpty() {
        return gifs.isEmpty();
    }

    private boolean isMyStoreGif(StoreGif storeGif) {
        return storeGif.getFavorite() == -1;
    }

}
