package com.tudomart.store.ui.activities.staticScreens;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tudomart.store.ui.activities.BaseActivity;
import com.tudomart.store.utils.Utils;

import com.tudomart.store.R;

import com.tudomart.store.ui.activities.BaseActivity;
import com.tudomart.store.utils.Utils;

public class LegalActivity extends BaseActivity {

    private ImageView backIcon;
    private TextView titleToolbar;
    private WebView webLegal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);
        backIcon = findViewById(R.id.back_icon);
        titleToolbar = findViewById(R.id.title_toolbar);
        webLegal = findViewById(R.id.web_legal);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleToolbar.setText("Legal");

        webLegal.loadUrl("file:///android_asset/docs/app_legal.html");
        webLegal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        webLegal.setLongClickable(false);
    }
    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this,orderId);
    }
}
