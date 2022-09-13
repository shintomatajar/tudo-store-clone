package ae.tudomart.store.ui.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.marcoscg.dialogsheet.DialogSheet;
import ae.matajar.store.R;
import ae.tudomart.store.helpers.sharedPref.UserSessionManager;
import ae.tudomart.store.ui.activities.BaseActivity;
import ae.tudomart.store.ui.activities.auth.MainActivity;
import ae.tudomart.store.ui.customViews.MyDialogSheet;
import ae.tudomart.store.ui.customViews.P07FancyAlert;
import ae.tudomart.store.utils.Utils;

public class SettingsActivity extends BaseActivity {
    int CHANGE_PASS_REQ = 1998;
    int DEACTIVATE_REQ = 1997;
    private ImageView backIcon;
    private TextView titleToolbar;
    private SwitchMaterial serviceSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        backIcon = findViewById(R.id.back_icon);
        titleToolbar = findViewById(R.id.title_toolbar);
        serviceSwitch = findViewById(R.id.service_switch);
        titleToolbar.setText("Settings");
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this,orderId);
    }

    public void openChangePassword(View view) {
        startActivityForResult(new Intent(getApplicationContext(), PasswordAuth.class), CHANGE_PASS_REQ);
    }

    public void startDeactivate(View view) {
        startActivityForResult(new Intent(getApplicationContext(), PasswordAuth.class), DEACTIVATE_REQ);
    }

    public void changelanguage(View view) {
    }

    public void onClickLogout(View view) {
        MyDialogSheet dialog = new MyDialogSheet(SettingsActivity.this);
        dialog.setTitle("Logout?");
        dialog.setMessage("Are you sure to Logout");
        dialog.setPositiveButton("Yes", new DialogSheet.OnPositiveClickListener() {
            @Override
            public void onClick(View v) {
                new UserSessionManager(SettingsActivity.this).logoutUser();
                finishAffinity();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHANGE_PASS_REQ) {
                startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
            } else if (requestCode == DEACTIVATE_REQ) {
                final P07FancyAlert alert = new P07FancyAlert(SettingsActivity.this);
                alert.setMessage("Account has been deactivated");
                alert.setButton("Continue", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
                alert.setGif(R.raw.animation_success);
                alert.show();
            }
        }
    }
}
