package ae.tudomart.store.ui.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ae.matajar.store.R;

public class PasswordAuth extends AppCompatActivity {

    private ImageView backIcon;
    private TextView titleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_auth);
        backIcon = findViewById(R.id.back_icon);
        titleToolbar = findViewById(R.id.title_toolbar);
        initToolbar();
    }

    private void initToolbar() {
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleToolbar.setText("Verify Password");
    }

    public void useOtp(View view) {
        startActivity(new Intent(getApplicationContext(), OTPAuth.class)
                .setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
        );
        finish();
    }

    public void confirmPassword(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
