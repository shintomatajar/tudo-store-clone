package com.tudomart.store.ui.activities.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tudomart.store.ui.customViews.P07FancyAlert;

import com.tudomart.store.R;

import com.tudomart.store.ui.customViews.P07FancyAlert;

public class ResetPasswordActivity extends AppCompatActivity {

    private ImageView backIcon;
    private TextView titleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        backIcon = findViewById(R.id.back_icon);
        titleToolbar = findViewById(R.id.title_toolbar);
        titleToolbar.setText("Ready to dispatch");
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onClickChangePassword(View view) {
        final P07FancyAlert alert = new P07FancyAlert(ResetPasswordActivity.this);
        alert.setMessage("Password has been changed");
        alert.setButton("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        alert.setGif(R.raw.animation_success);
        alert.show();
    }
}
