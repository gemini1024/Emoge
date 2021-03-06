package com.emoge.app.emoge.ui.server;

import android.graphics.drawable.Drawable;
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
import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.MyStoreGif;
import com.emoge.app.emoge.model.StoreGif;
import com.emoge.app.emoge.utils.GifDownloadTask;
import com.emoge.app.emoge.utils.GlideAvRequester;
import com.emoge.app.emoge.utils.dialog.SweetDialogs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;

/**
 * Created by jh on 17. 8. 3.
 * 서버에 저장된 이미지들 불러오기 위한 Adapter
 */

class StoreGifAdapter extends RecyclerView.Adapter<StoreGifViewHolder> implements FavoritesAccessible {
    private final String LOG_TAG = StoreGifAdapter.class.getSimpleName();

    private Fragment fragment;
    private ArrayList<StoreGif> gifs;
    private Realm realm;
    private DatabaseReference database;
    private ArrayList<String> serverItemKeys;


    StoreGifAdapter(@NonNull Fragment fragment, @NonNull ArrayList<StoreGif> gifs,
                    @NonNull Realm realm, @Nullable DatabaseReference database) {
        this.fragment = fragment;
        this.gifs = gifs;
        this.realm = realm;
        this.database = database;                   // 저장소 탭인 경우 null
        this.serverItemKeys = new ArrayList<>();
    }

    @Override
    public StoreGifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_server_img, parent, false);
        StoreGifViewHolder viewHolder = new StoreGifViewHolder(view);
        viewHolder.setFavoritesAccessible(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StoreGifViewHolder holder, final int position) {
        final StoreGif item = gifs.get(position);

        holder.loading.show();
        Glide.with(fragment).load(Uri.parse(item.getDownloadUrl()))
                .listener(new GlideAvRequester<Drawable>(holder.loading)).into(holder.image);
        holder.title.setText(item.getTitle());
        if(isServerCategory()) {
            holder.favoriteIcon.setSelected(hasFavorites(position));
            holder.favorite.setText(String.valueOf(item.getFavorite()));
        } else {
            holder.favoriteIcon.setSelected(true);
            holder.favorite.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void addFavorite(final int position) {
        updateFavorite(database, serverItemKeys.get(position), -1);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(MyStoreGif.class)
                        .equalTo(MyStoreGif.KEY_FIELD, serverItemKeys.get(position))
                        .findFirst().deleteFromRealm();
                notifyItemChanged(position);
                Toast.makeText(fragment.getContext(), R.string.remove_favorite, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void removeFavorite(final int position) {
        updateFavorite(database, serverItemKeys.get(position), 1);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MyStoreGif myStoreGif = realm.createObject(MyStoreGif.class, serverItemKeys.get(position));
                myStoreGif.setAll(gifs.get(position));
                notifyItemChanged(position);
                Toast.makeText(fragment.getContext(), R.string.add_favorite, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void removeFavoriteWithDialog(final int position) {
        SweetDialogs.showWarningDialog(fragment.getActivity(), R.string.cancel_favorite_title, R.string.cancel_favorite_content)
                .setConfirmText(fragment.getString(R.string.cancel_favorite))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        removeFavoriteInMyStore(position);
                    }
                });
    }

    @Override
    public void downloadImage(final int position) {
        SweetDialogs.showWarningDialog(fragment.getActivity(), R.string.download_gif_title, R.string.download_favorite_content)
                .setConfirmText(fragment.getString(R.string.download_gif_title))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        new GifDownloadTask(fragment.getActivity()).execute(gifs.get(position));
                    }
                });
    }

    private void removeFavoriteInMyStore(final int position) {
        String[] categories = fragment.getResources().getStringArray(R.array.server_category);
        for(String category : categories) {
            updateFavorite(FirebaseDatabase.getInstance().getReference(category),
                    serverItemKeys.get(position), -1);
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(MyStoreGif.class)
                        .equalTo(MyStoreGif.KEY_FIELD, serverItemKeys.get(position))
                        .findFirst().deleteFromRealm();
                removeItem(position);
                Toast.makeText(fragment.getContext(), R.string.remove_favorite, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean isServerCategory() {
        return database != null;
    }

    @Override
    public boolean hasFavorites(int position) {
        return realm.where(MyStoreGif.class).equalTo(MyStoreGif.KEY_FIELD,
                serverItemKeys.get(position)).findFirst() != null;
    }

    private void updateFavorite(DatabaseReference db, String key, final int value) {
        if(db.child(key) != null)
        db.child(key).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                StoreGif serverGif = mutableData.getValue(StoreGif.class);
                if(serverGif != null) {
                    serverGif.setFavorite(serverGif.getFavorite()+value);
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
    void addItem(String key, StoreGif addedGif) {
        boolean added = false;
        for(int i=0; i<gifs.size(); i++) {
            if(gifs.get(i).getFavorite() < addedGif.getFavorite()) {
                serverItemKeys.add(i, key);
                gifs.add(i, addedGif);
                notifyItemInserted(i);
                added = true;
                break;
            }
        }
        if(!added) {
            serverItemKeys.add(key);
            gifs.add(addedGif);
            notifyItemInserted(gifs.size()-1);
        }
    }

    private void removeItem(int position) {
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

}
