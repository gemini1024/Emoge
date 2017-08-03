package com.emoge.app.emoge.ui.server;

/**
 * Created by jh on 17. 8. 3.
 */

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class CategoryPagerAdapter extends FragmentPagerAdapter {
    private final String LOG_TAG = CategoryPagerAdapter.class.getSimpleName();

    static final int CATEGORY_HUMOR     = 0;
    static final int CATEGORY_HEALING   = 1;
    static final int CATEGORY_HUMAN     = 2;
    static final int CATEGORY_STORE     = 3;
    static final int CATEGORY_NUM       = 4;


    CategoryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CategoryFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return CATEGORY_NUM;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getCategoryName(position);
    }

    @NonNull
    static CharSequence getCategoryName(int category) {
        switch (category) {
            case CATEGORY_HUMOR:
                return "유머";
            case CATEGORY_HEALING:
                return "힐링";
            case CATEGORY_HUMAN:
                return "인물";
            default:
                return "저장소";
        }
    }
}