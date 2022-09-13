package com.tudomart.store.ui.activities.orders;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tudomart.store.model.order.ModelOrderList;
import com.tudomart.store.ui.activities.BaseActivity;
import com.tudomart.store.ui.activities.DispatchedActivity;
import com.tudomart.store.ui.activities.OrderAcceptActivity;
import com.tudomart.store.ui.activities.PackingActivity;
import com.tudomart.store.ui.activities.ReadyToDispatchActivity;
import com.tudomart.store.utils.Utils;

import com.tudomart.store.R;

import com.tudomart.store.interfaces.dashboard.InterFragmentCommunicator;
import com.tudomart.store.model.order.ModelOrderList;
import com.tudomart.store.ui.activities.BaseActivity;
import com.tudomart.store.ui.activities.DispatchedActivity;
import com.tudomart.store.ui.activities.OrderAcceptActivity;
import com.tudomart.store.ui.activities.PackingActivity;
import com.tudomart.store.ui.activities.ReadyToDispatchActivity;
import com.tudomart.store.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PackingOrdersActivity extends BaseActivity implements InterFragmentCommunicator {

    private ArrayList<ModelOrderList> data = new ArrayList<>();
    private ImageView mBackIcon;
    private TextView mTitleToolbar;
    private RecyclerView mPackingRecycler;
    private ImageView mImgError;
    private TextView mTxtErrorTitle;
    private TextView mTxtErrorMessage;
    private Button mBtnRetry;
    private RelativeLayout mErrorLayout;
    private LinearLayout mErrorParentLayout;
    private ProgressBar mLoadingProgress;
    private RelativeLayout mLoadingLayout;
    int status = 0;
      long hours = 0,minutes = 0,days=0;
    public int itemcount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_orders);
        initViews();
        initToolbar();
        mLoadingLayout.setVisibility(View.GONE);

       // addData();
       /* initRecycler();*/

    }


    @Override
    protected void onResume() {
        super.onResume();

       // addData();
       /* initRecycler();*/

    }

    private void initToolbar() {
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTitleToolbar.setText("Packing Orders");
    }

   /* private void initRecycler() {
        mPackingRecycler.setAdapter(new AdapterListSplittedOrders(PackingOrdersActivity.this, data, new AdapterListSplittedOrders.AdapterCallback() {
            @Override
            public void onClickItem(String orderId, int orderStatus, String timeStamp) {
                if (orderStatus == 0) {
                    startActivity(new Intent(getApplicationContext(), OrderAcceptActivity.class).putExtra("ORDER_ID",orderId));
                } else if (orderStatus == 1) {
                    startActivity(new Intent(getApplicationContext(), PackingActivity.class).putExtra("ORDER_ID",orderId));
                }  else if (orderStatus == 2) {
                    startActivity(new Intent(getApplicationContext(), ReadyToDispatchActivity.class).putExtra("ORDER_ID",orderId));
                }
                else if (orderStatus == 3) {
                    startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID",orderId)
                            .putExtra("DATE_KEY",timeStamp).putExtra("STATUS",orderStatus));
                }  else if (orderStatus == 4) {
                    startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID",orderId)
                            .putExtra("DATE_KEY",timeStamp).putExtra("STATUS",orderStatus));
                }

                else if (orderStatus == 5){
                    startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID",orderId)
                            .putExtra("DATE_KEY",timeStamp).putExtra("STATUS",orderStatus));
                }

                else if (orderStatus == 6) {
                    startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID", orderId)
                            .putExtra("DATE_KEY", timeStamp).putExtra("STATUS", orderStatus));
                }

            }
        }));
    }*/

   /* private void addData() {
        mLoadingLayout.setVisibility(View.VISIBLE);

        RequestQueue rQueue = Volley.newRequestQueue(PackingOrdersActivity.this);
        JSONObject requestBody = new JSONObject();

        try {

            requestBody.put("strPageLimit", "");
            requestBody.put("strSkipCount", 0);
            requestBody.put("strLoginUserId", new UserSessionManager(PackingOrdersActivity.this).getUserDetails().get("userid")*//*new UserSessionManager(requireActivity()).getUserId()*//*);
            requestBody.put("strShopId", new UserSessionManager(PackingOrdersActivity.this).getUserDetails().get("shop")*//* new UserSessionManager(requireActivity()).getUserId()*//*);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.PACKED_ORDERS_DETAILS_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                mLoadingLayout.setVisibility(View.GONE);
                try {
                    if (response.getBoolean("success")) {

                        data.clear();
                        mPackingRecycler.setVisibility(View.VISIBLE);

                        JSONArray jsonArray = response.getJSONArray("data").getJSONArray(0);
                        itemcount = response.getJSONArray("data").getJSONObject(1).getInt("totalOrdersCount");


                        //Log.d("Response", response.toString());

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String orderId = jsonArray.getJSONObject(i).getString("strOrderID");
                            String paymentType = jsonArray.getJSONObject(i).getString("strPaymentMode");
                            String amount = jsonArray.getJSONObject(i).getString("intGrandTotal");
                            String itemsCount = jsonArray.getJSONObject(i).getString("intTotalItemQuantity");
                            String order_status = jsonArray.getJSONObject(i).getString("strStoreStatus");
                            if (order_status.equalsIgnoreCase("PACKING")) {
                                status = 1;
                            }
                            else if (order_status.equalsIgnoreCase("CANCELLED"))
                            {
                                status = 6;
                            }
                            String timeStamp = getNeDate(jsonArray.getJSONObject(i).getString("dateCreateDateAndTime"));
                            //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                            String duration = getFormattedDate(jsonArray.getJSONObject(i).getString("dateCreateDateAndTime"));

                            String timeSlot = "",deliveryType = "";
                          if (jsonArray.getJSONObject(i).has("arrayTimeSlot"))
                            {
                                timeSlot = getSlotDate(jsonArray.getJSONObject(i).getJSONArray("arrayTimeSlot").getJSONObject(0).getString("dateSlot"))+" "+jsonArray.getJSONObject(i).getJSONArray("arrayTimeSlot").getJSONObject(0).getString("strDisplayName");

                                if (jsonArray.getJSONObject(i).getJSONArray("arrayTimeSlot").getJSONObject(0).has("strDeliveryType"))
                                {
                                    deliveryType = jsonArray.getJSONObject(i).getJSONArray("arrayTimeSlot").getJSONObject(0).getString("strDeliveryType");
                                }

                            }
                            else
                            {
                                timeSlot = "";
                                deliveryType = "";
                            }


                            data.add(new ModelOrderList(orderId, paymentType, amount, itemsCount, status, duration, timeStamp,timeSlot,deliveryType));
                        }

                        mTitleToolbar.setText("Packing Orders (" + itemcount + ")");

                        //initRecycler();

                    } else {
                        mLoadingLayout.setVisibility(View.GONE);
                        mPackingRecycler.setVisibility(View.GONE);
                        if (response.getString("message") == " No Data Found") { // user has made no orders


                            mErrorLayout.setVisibility(View.VISIBLE);
                            mTxtErrorMessage.setText(response.getString("message"));

                        }
                        else
                            {

                                mErrorLayout.setVisibility(View.VISIBLE);
                                mTxtErrorMessage.setText(response.getString("message"));

                           *//* MyDialogSheet dialogSheet = new MyDialogSheet(PackingOrdersActivity.this);
                            dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                    .setMessage(response.getString("message"))
                                    .setPositiveButton(getString(R.string.dialog_button_retry),
                                            new DialogSheet.OnPositiveClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    addData();
                                                }
                                            })
                                    .setBackgroundColor(ContextCompat.getColor(PackingOrdersActivity.this, R.color.white))
                                    .setButtonsColorRes(R.color.colorAccent)
                                    .setNegativeButton(getString(R.string.dialog_button_cancel),
                                            null);
                            dialogSheet.show();*//*


                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mLoadingLayout.setVisibility(View.GONE);
                mPackingRecycler.setVisibility(View.GONE);

                MyDialogSheet dialogSheet = new MyDialogSheet(PackingOrdersActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(PackingOrdersActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addData();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(PackingOrdersActivity.this, R.color.white))
                        .setButtonsColorRes(R.color.colorAccent)
                        .setNegativeButton(getString(R.string.dialog_button_cancel),
                                null);
                dialogSheet.show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", *//*new UserSessionManager(PackingOrdersActivity.this).getUserDetails().get("token")()*//*  new UserSessionManager(PackingOrdersActivity.this).getUserDetails().get("token") *//*new UserSessionManager(requireActivity()).getToken()*//*);
                return params;
            }
        };

       RequestController.getInstance().addToRequestQueue(clearNotifRequest);


    }*/


    public String getFormattedDate(String notificationDate) {
        if (notificationDate.isEmpty()) {
            return "";
        }
        long notifDate = 0;

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC+04:00"));  //Timezone is IST!!!


            String splitDate[] = notificationDate.split("T");
            Date date = sdf.parse(splitDate[0] + " " + splitDate[1].substring(0,8));

            Calendar notifRecTime = Calendar.getInstance();
            notifRecTime.setTimeInMillis(notifDate);

            Calendar now = Calendar.getInstance();
            Date currentTime = Calendar.getInstance().getTime();
            Date date1 = date;// sdf.parse(String.valueOf(date))
            Date date2 = sdf.parse("2020-05-26 20:35:55");

            long different = currentTime.getTime() - date1.getTime();

            System.out.println("startDate : " + date1);
            System.out.println("endDate : "+ currentTime);
            System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            System.out.printf(
                    "%d days, %d hours, %d minutes, %d seconds%n",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

            hours = elapsedHours; minutes = elapsedMinutes; days = elapsedDays;

            if ( !(date == null) ) {
                notifDate = date.getTime();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }




//1 minute = 60 seconds
//1 hour = 60 x 60 = 3600
//1 day = 3600 x 24 = 86400


        if (days>0)
        {
             return days+"Day "+hours+"Hr "+minutes+"Min" ;
        }
        else
        {
            return hours+"Hr "+minutes+"Min" ;
        }



    }



    public String getNeDate(String notificationDate)
    {
        if (notificationDate.isEmpty()) {
            return "";
        }
        long notifDate = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC+04:00"));  //Timezone is IST!!!

            String splitDate[] = notificationDate.split("T");
            Date date = sdf.parse(splitDate[0] + " " + splitDate[1].substring(0, 8));
            if (!(date == null)) {
                notifDate = date.getTime();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar notifRecTime = Calendar.getInstance();
        notifRecTime.setTimeInMillis(notifDate);

        Calendar now = Calendar.getInstance();

        final String dateTimeFormatString = "EEEE, MMMM d";
        final String timeFormatString = "h:mm aa";


        return DateFormat.format("dd MMM yy,", notifDate).toString()
                + " "
                + DateFormat.format(timeFormatString, notifDate);

    }

    public String getSlotDate(String notificationDate)
    {
        if (notificationDate.isEmpty()) {
            return "";
        }
        long notifDate = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC+04:00"));  //Timezone is IST!!!

            String splitDate[] = notificationDate.split("T");
            Date date = sdf.parse(splitDate[0] + " " + splitDate[1].substring(0, 8));
            if (!(date == null)) {
                notifDate = date.getTime();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar notifRecTime = Calendar.getInstance();
        notifRecTime.setTimeInMillis(notifDate);

        Calendar now = Calendar.getInstance();

        final String dateTimeFormatString = "EEEE, MMMM d";
        final String timeFormatString = "h:mm aa";


        return DateFormat.format("EEEE,dd MMM yyyy,", notifDate).toString();
                /*+ " "
                + DateFormat.format(timeFormatString, notifDate)*/

    }

    private void initViews() {
        mBackIcon = findViewById(R.id.back_icon);
        mTitleToolbar = findViewById(R.id.title_toolbar);
        mPackingRecycler = findViewById(R.id.packingRecycler);
        mImgError = findViewById(R.id.img_error);
        mTxtErrorTitle = findViewById(R.id.txt_error_title);
        mTxtErrorMessage = findViewById(R.id.txt_error_message);
        mBtnRetry = findViewById(R.id.btn_retry);
        mErrorLayout = findViewById(R.id.error_layout);
        mErrorParentLayout = findViewById(R.id.error_parent_layout);
        mLoadingProgress = findViewById(R.id.loadingProgress);
        mLoadingLayout = findViewById(R.id.loadingLayout);
    }


    @Override
    public void onClickOrderItem(String OrderId, int orderStatus, String timeStamp) {
        if (orderStatus == 0) {
            startActivity(new Intent(getApplicationContext(), OrderAcceptActivity.class).putExtra("ORDER_ID",OrderId));
        } else if (orderStatus == 1) {
            startActivity(new Intent(getApplicationContext(), PackingActivity.class).putExtra("ORDER_ID",OrderId));
        }  else if (orderStatus == 2) {
            startActivity(new Intent(getApplicationContext(), ReadyToDispatchActivity.class).putExtra("ORDER_ID",OrderId));
        }
        else if (orderStatus == 3) {
            startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID",OrderId)
                    .putExtra("DATE_KEY",timeStamp).putExtra("STATUS",orderStatus));
        }  else if (orderStatus == 4) {
            startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID",OrderId)
                    .putExtra("DATE_KEY",timeStamp).putExtra("STATUS",orderStatus));
        }

        else if (orderStatus == 5){
            startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID",OrderId)
                    .putExtra("DATE_KEY",timeStamp).putExtra("STATUS",orderStatus));
        }

        else if (orderStatus == 6) {
            startActivity(new Intent(getApplicationContext(), DispatchedActivity.class).putExtra("ORDER_ID", OrderId)
                    .putExtra("DATE_KEY", timeStamp).putExtra("STATUS", orderStatus));
        }
    }

    @Override
    public void updateToolbar(int count) {
        mTitleToolbar.setText("Packing Orders(" + count +")");
    }
    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this,orderId);
    }
}
