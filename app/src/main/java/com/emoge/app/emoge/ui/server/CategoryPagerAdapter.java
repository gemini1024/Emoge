package com.emoge.app.emoge.ui.server;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by jh on 17. 8. 3.
 * 서버 카테고리 Page Adapter
 */
class CategoryPagerAdapter extends FragmentPagerAdapter {
    private final String LOG_TAG = CategoryPagerAdapter.class.getSimpleName();

    private Category category;

    CategoryPagerAdapter(FragmentManager fm, Category category) {
        super(fm);
        this.category = category;
    }

    @Override
    public Fragment getItem(int position) {
        return CategoryFragment.newInstance(category.getCategoryName(position));
    }

    @Override
    public int getCount() {
        return category.getCategoryNum();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return category.getCategoryName(position);
    }

}