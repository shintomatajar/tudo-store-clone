package com.tudomart.store.ui.activities.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tudomart.store.ui.activities.BaseActivity;
import com.tudomart.store.ui.activities.DispatchedActivity;
import com.tudomart.store.ui.activities.OrderAcceptActivity;
import com.tudomart.store.ui.activities.PackingActivity;
import com.tudomart.store.ui.activities.ReadyToDispatchActivity;
import com.tudomart.store.utils.Utils;

import com.tudomart.store.R;

import com.tudomart.store.interfaces.dashboard.InterFragmentCommunicator;
import com.tudomart.store.ui.activities.BaseActivity;
import com.tudomart.store.ui.activities.DispatchedActivity;
import com.tudomart.store.ui.activities.OrderAcceptActivity;
import com.tudomart.store.ui.activities.PackingActivity;
import com.tudomart.store.ui.activities.ReadyToDispatchActivity;
import com.tudomart.store.utils.Utils;

public class NewOrdersActivity extends BaseActivity implements InterFragmentCommunicator {
    private ImageView backIcon;
    private TextView titleToolbar;
    // private SwipeRefreshLayout swipeRefreshLayout;
    //private boolean isSwipeRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_orders);
        initViews();
        initToolbar();
        // initSwipeRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initViews();
        initToolbar();
        // initSwipeRefresh();

    }


   /* private void initSwipeRefresh() {

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.red));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                isSwipeRefresh = true;
                finish();
                startActivity(getIntent());
            }
        });
    }*/

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
        titleToolbar.setText("New Orders (0)");
        // swipeRefreshLayout = findViewById(R.id.srlDashSwipeRefresh);
    }

    @Override
    public void onClickOrderItem(String OrderId, int orderStatus, String timeStamp) {
        if (orderStatus == 0) {
            startActivity(new Intent(getApplicationContext(), OrderAcceptActivity.class)
                    .putExtra("ORDER_ID", OrderId));
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
        titleToolbar.setText("New Orders(" + count + ")");
    }

    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this, orderId);
    }
}
