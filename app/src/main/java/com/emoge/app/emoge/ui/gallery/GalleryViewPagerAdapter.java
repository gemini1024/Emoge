package com.emoge.app.emoge.ui.gallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by jh on 17. 8. 8.
 */

public class GalleryViewPagerAdapter extends FragmentPagerAdapter {
    private final String LOG_TAG = GalleryViewPagerAdapter.class.getSimpleName();

    public GalleryViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return GalleryFragment.newInstance(null, position);
    }

    @Override
    public int getCount() {
        return 2; // +store
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == ImageFormatChecker.IMAGE_TYPE) {
            return "이미지에서 추가";
        } else {
            return "움짤에서 추가";
        }
    }

}
