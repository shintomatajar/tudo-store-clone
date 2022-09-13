package ae.tudomart.store.ui.activities.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ae.matajar.store.R;

public class AuthOTPVerification extends AppCompatActivity {

    private ImageView backIcon;
    private TextView titleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_o_t_p_verification);
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
}
