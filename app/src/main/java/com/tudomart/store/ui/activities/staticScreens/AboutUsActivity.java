package com.tudomart.store.ui.activities.staticScreens;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tudomart.store.ui.activities.BaseActivity;
import com.tudomart.store.utils.Utils;

import com.tudomart.store.R;

import com.tudomart.store.ui.activities.BaseActivity;
import com.tudomart.store.utils.Utils;

public class AboutUsActivity extends BaseActivity {

    private ImageView backIcon;
    private TextView titleToolbar;
    private TextView textVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        backIcon = findViewById(R.id.back_icon);
        titleToolbar = findViewById(R.id.title_toolbar);
        textVersion = findViewById(R.id.text_version);
        titleToolbar.setText("About Us");
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        try {
            textVersion.setText(getString(R.string.txt_version,getPackageManager().getPackageInfo(getPackageName(),0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this,orderId);
    }
}
