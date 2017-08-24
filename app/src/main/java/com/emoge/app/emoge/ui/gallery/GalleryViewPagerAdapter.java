package com.emoge.app.emoge.ui.gallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.emoge.app.emoge.R;

/**
 * Created by jh on 17. 8. 8.
 * 움짤 생성 화면에 띄울 이미지&움짤 갤러리 ViewPager 의 Adapter
 */

public class GalleryViewPagerAdapter extends FragmentPagerAdapter {
    private final String LOG_TAG = GalleryViewPagerAdapter.class.getSimpleName();

    private static final int GALLERY_PAGE_NUM = 3;
    private String strAddImage;
    private String strAddGif;
    private String strAddVideo;

    public GalleryViewPagerAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        strAddImage = activity.getString(R.string.add_from_image);
        strAddGif = activity.getString(R.string.add_from_gif);
        strAddVideo = activity.getString(R.string.add_from_video);
    }

    @Override
    public Fragment getItem(int position) {
        return GalleryFragment.newInstance(null, position);
    }

    @Override
    public int getCount() {
        return GALLERY_PAGE_NUM;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == ImageFormatChecker.IMAGE_TYPE) {
            return strAddImage;
        } else if(position == ImageFormatChecker.GIF_TYPE) {
            return strAddGif;
        } else {
            return strAddVideo;
        }
    }

}
