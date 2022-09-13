package com.tudomart.store.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tudomart.store.utils.Utils;

import com.tudomart.store.R;
import com.tudomart.store.interfaces.dashboard.InterFragmentCommunicator;
import com.tudomart.store.utils.Utils;

public class AllOrdersActivity extends BaseActivity implements InterFragmentCommunicator {
    private ImageView backIcon;
    private TextView titleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);
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
        titleToolbar.setText("All Orders (0)");
    }

    @Override
    public void onClickOrderItem(String OrderId, int orderStatus, String timeStamp) {

//        if (orderStatus == 0) {
//            startActivity(new Intent(getApplicationContext(), OrderAcceptActivity.class).putExtra("ORDER_ID",OrderId));
//        } else if (orderStatus == 1) {
//            startActivity(new Intent(getApplicationContext(), PackingActivity.class).putExtra("ORDER_ID",OrderId));
//        }  else if (orderStatus == 2) {
//            startActivity(new Intent(getApplicationContext(), ReadyToDispatchActivity.class).putExtra("ORDER_ID",OrderId));
//        }
//        else if (orderStatus == 3) {
//            startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID",OrderId)
//                    .putExtra("DATE_KEY",timeStamp).putExtra("STATUS",orderStatus));
//        }  else if (orderStatus == 4) {
//            startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID",OrderId)
//                    .putExtra("DATE_KEY",timeStamp).putExtra("STATUS",orderStatus));
//        }
//
//        else if (orderStatus == 5){
//            startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID",OrderId)
//                    .putExtra("DATE_KEY",timeStamp).putExtra("STATUS",orderStatus));
//        }
    }

    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this,orderId);
    }
    @Override
    public void updateToolbar(int count) {
        titleToolbar.setText("All Orders(" + count +")");
    }
}
