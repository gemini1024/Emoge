package com.emoge.app.emoge.ui.license;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.License;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LicenseActivity extends AppCompatActivity {

    @BindView(R.id.license_list)
    RecyclerView mLicenseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ButterKnife.bind(this);

        ImageButton backButton = (ImageButton) findViewById(R.id.toolbar_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String[] titles = getResources().getStringArray(R.array.licenses_title);
        String[] contents = getResources().getStringArray(R.array.licenses_content);
        ArrayList<License> licenses = new ArrayList<>();
        for(int i=0; i<titles.length; i++) {
            licenses.add(new License(titles[i], contents[i]));
        }

        LicenseAdapter mLicenseAdapter = new LicenseAdapter(licenses);
        mLicenseView.setHasFixedSize(true);
        mLicenseView.setLayoutManager(new LinearLayoutManager(this));
        mLicenseView.setAdapter(mLicenseAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
