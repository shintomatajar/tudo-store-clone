package ae.tudomart.store.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ae.matajar.store.R;

import ae.tudomart.store.interfaces.dashboard.InterFragmentCommunicator;
import ae.tudomart.store.ui.activities.BaseActivity;
import ae.tudomart.store.ui.activities.DispatchedActivity;
import ae.tudomart.store.ui.activities.PackingActivity;
import ae.tudomart.store.ui.activities.ReadyToDispatchActivity;
import ae.tudomart.store.ui.activities.StoreOrderAccept;
import ae.tudomart.store.utils.Utils;

public class PickupFromStore extends BaseActivity implements InterFragmentCommunicator {
    private ImageView backIcon;
    private TextView titleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_from_store);
        initViews();
        initToolbar();
    }


    private void initToolbar() {
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        backIcon = findViewById(R.id.back_icon);
        titleToolbar = findViewById(R.id.title_toolbar);
        titleToolbar.setText("All Store pickup (0)");
    }

    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this, orderId);
    }

    @Override
    public void onClickOrderItem(String OrderId, int orderStatus, String timeStamp) {

        if (orderStatus == 0) {
            startActivity(new Intent(getApplicationContext(), StoreOrderAccept.class).putExtra("ORDER_ID", OrderId));
        } else if (orderStatus == 1) {
            startActivity(new Intent(getApplicationContext(), PackingActivity.class).putExtra("ORDER_ID", OrderId));
        } else if (orderStatus == 2) {
            startActivity(new Intent(getApplicationContext(), ReadyToDispatchActivity.class).putExtra("ORDER_ID", OrderId));
        } else if (orderStatus == 3) {
            startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID", OrderId)
                    .putExtra("DATE_KEY", timeStamp).putExtra("STATUS", orderStatus));
        } else if (orderStatus == 4) {
            startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID", OrderId)
                    .putExtra("DATE_KEY", timeStamp).putExtra("STATUS", orderStatus));
        } else if (orderStatus == 5) {
            startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID", OrderId)
                    .putExtra("DATE_KEY", timeStamp).putExtra("STATUS", orderStatus));
        } else if (orderStatus == 6) {
            startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID", OrderId)
                    .putExtra("DATE_KEY", timeStamp).putExtra("STATUS", orderStatus));
        }
    }

    @Override
    public void updateToolbar(int count) {
        titleToolbar.setText("All store orders(" + count + ")");
    }
}