package ae.tudomart.store.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ae.matajar.store.R;
import ae.tudomart.store.utils.DummyData;
import ae.tudomart.store.utils.Utils;

import java.util.Random;

public class NotificationsActivity extends BaseActivity {


    private ImageView backIcon;
    private TextView titleToolbar;
    private TextView txtQuotes;
    private LinearLayout noData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initViews();
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleToolbar.setText("Notifications");
        Random rn = new Random();
        int range = DummyData.quotes.length + 1;
        txtQuotes.setText(DummyData.quotes[rn.nextInt(range)]);
    }

    private void initViews() {

        backIcon = findViewById(R.id.back_icon);
        titleToolbar = findViewById(R.id.title_toolbar);
        txtQuotes = findViewById(R.id.txt_quotes);
        noData = findViewById(R.id.noData);

    }
    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this,orderId);
    }
}
