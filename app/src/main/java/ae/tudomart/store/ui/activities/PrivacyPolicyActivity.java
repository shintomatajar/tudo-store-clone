package ae.tudomart.store.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import ae.matajar.store.R;

import ae.tudomart.store.utils.Utils;

public class PrivacyPolicyActivity extends BaseActivity {

    private ImageView backIcon;
    private TextView titleToolbar;
    private WebView webPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        backIcon = findViewById(R.id.back_icon);
        titleToolbar = findViewById(R.id.title_toolbar);
        webPrivacy = findViewById(R.id.web_privacy);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleToolbar.setText("Privacy Policy");

        webPrivacy.loadUrl("file:///android_asset/docs/app_privacy_policy.html");
        webPrivacy.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        webPrivacy.setLongClickable(false);

    }
    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this,orderId);
    }
}
