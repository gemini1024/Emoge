package com.emoge.app.emoge.ui.server;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.emoge.app.emoge.R;

public class ServerActivity extends AppCompatActivity {
    private final String LOG_TAG = ServerActivity.class.getSimpleName();

    private CategoryPagerAdapter mCategoryPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        ImageButton backButton = (ImageButton) findViewById(R.id.toolbar_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCategoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.server_container);
        mViewPager.setAdapter(mCategoryPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.server_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


}
