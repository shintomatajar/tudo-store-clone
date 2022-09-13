package com.tudomart.store.ui.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tudomart.store.R;

public class OTPAuth extends AppCompatActivity {

    private ImageView backIcon;
    private TextView titleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_auth);
        backIcon = findViewById(R.id.back_icon);
        titleToolbar = findViewById(R.id.title_toolbar);
        titleToolbar.setText("Verify OTP");
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void usePassword(View view) {
        startActivity(new Intent(getApplicationContext(), PasswordAuth.class)
                .setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
        );
        finish();
    }

    public void btnClickVerify(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
