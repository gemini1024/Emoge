package com.emoge.app.emoge.ui.server;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.emoge.app.emoge.R;

/**
 * Created by jh on 17. 8. 3.
 * 서버 메인 페이지
 */
public class ServerActivity extends AppCompatActivity {
    private final String LOG_TAG = ServerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        findViewById(R.id.toolbar).setElevation(0.f);

        ImageButton backButton = (ImageButton) findViewById(R.id.toolbar_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CategoryPagerAdapter mCategoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), new Category(getResources()));
        ViewPager mViewPager = (ViewPager) findViewById(R.id.server_container);
        mViewPager.setAdapter(mCategoryPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.server_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
