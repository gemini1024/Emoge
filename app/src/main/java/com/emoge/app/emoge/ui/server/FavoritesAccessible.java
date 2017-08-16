package com.emoge.app.emoge.ui.server;

/**
 * Created by jh on 17. 8. 16.
 * Firebase, Realm 에서 접근해서 Server item, favorite item 처리
 */

interface FavoritesAccessible {
    boolean isServerCategory();
    boolean hasFavorites(int position);
    void addFavorite(int position);
    void removeFavorite(int position);
    void removeFavoriteWithDialog(int position);
    void downloadImage(int position);
}
