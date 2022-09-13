package com.tudomart.store.ui.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ProgressDialog;
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

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marcoscg.dialogsheet.DialogSheet;
import com.tudomart.store.utils.Utils;

import com.tudomart.store.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.tudomart.store.adapters.orders.orderDetails.AdapterOrderDetailsItems;
import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.helpers.network.RequestController;
import com.tudomart.store.helpers.network.VolleyErrorHandler;
import com.tudomart.store.helpers.sharedPref.UserSessionManager;
import com.tudomart.store.model.order.orderDetails.ModelOrderItems;
import com.tudomart.store.ui.activities.dash.DashboardActivity;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.ui.customViews.P07FancyAlert;
import com.tudomart.store.utils.Utils;

public class OrderAcceptActivity extends BaseActivity {
    private ImageView mBackIcon;
    private TextView mTitleToolbar;
    private TextView mTxtDayOfDate;
    private TextView mTxtMonthOfDate;
    private TextView mTxtOrderIdTop;
    private TextView mTxtPriceTop;
    private LinearLayout mHeader;
    private ImageView mIcOrderStar;
    private TextView mTxtOrdered;
    private LinearLayout mOrderedStar;
    private ImageView mIcDeliveryStar;
    private TextView mTxtDelivered;
    private TextView mUserInfo;
    private TextView mTextView4;
    private RecyclerView mOrderItemsRecycler;
    private TextView mTxtDeliveryAddress;
    private CardView mLayoutAddress;
    private ImageView mImgPaymentType;
    private TextView mTxtPaymentType;
    private CardView mLayoutPaymentType;
    private TextView mTxtSubTotal;
    private TextView mTxtDeliveryCharge;
    private TextView mTxtDiscount;
    private TextView txtDiscountName;
    private TextView mTxtTotalPrice;
    private CardView mLayoutPaymentInfo;
    private TextView mSecondButton;
    private NestedScrollView mLayoutData;
    private Button mMainButton;
    private LinearLayout mBottomButtons;
    private ImageView mImgError;
    private TextView mTxtErrorTitle;
    private TextView mTxtErrorMessage;
    private Button mBtnRetry;
    private RelativeLayout mErrorLayout;
    private LinearLayout mErrorParentLayout;
    private ProgressBar mLoadingProgress;
    private RelativeLayout mLoadingLayout;
    private RelativeLayout order_details_layout;

    private TextView txtPriceVariation;
    private LinearLayout priceVariationLayout;
    private TextView txtDinominationPrice;
    private CardView dinominationLayout;

    private double dinominationPrice;
    private double price_variation;

    private TextView txtRewardDiscount;


    private ArrayList<ModelOrderItems> list = new ArrayList<>();

    String order_id = "";
    Boolean frozen_food = false, hide_frozen = true;
    private TextView mTimeOrderPlaced;
    private TextView mTimeSlotSelected;
    private TextView mTxtSpecialNote;
    private CardView mLayoutSpecialNote;
    private CardView mSlotSelectedLabel;
    private TextView mTxtDeliveryType;
    private CardView mLayoutDeliveryType;

    private DashboardIntent dashboardIntent;


    String stock = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_accept);
        initViews();

        Intent intent = getIntent();
        order_id = intent.getStringExtra("ORDER_ID");

        mTxtOrderIdTop.setText(order_id);


        mMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogSheet dialog = new MyDialogSheet(OrderAcceptActivity.this);
                dialog.setTitle("Confirm Order");
                dialog.setMessage("Are you sure to confirm this order");
                dialog.setPositiveButton("Yes", new DialogSheet.OnPositiveClickListener() {
                    @Override
                    public void onClick(View v) {

                        callAcceptOrder();

                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });

        mOrderedStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserInfo.getVisibility() == VISIBLE) {
                    mUserInfo.setVisibility(GONE);
                } else {
                    mUserInfo.setVisibility(VISIBLE);
                }
            }
        });
        fetchOrderDetails(order_id);


    }


    private void addData() {
        mLoadingLayout.setVisibility(VISIBLE);

        RequestQueue rQueue = Volley.newRequestQueue(OrderAcceptActivity.this);
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("strLoginUserId", new UserSessionManager(OrderAcceptActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strStoreId", new UserSessionManager(OrderAcceptActivity.this).getUserDetails().get("shop")/* new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strOrderID", order_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.ALL_ORDERS_DETAILS_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d("NEW ORDR", response.toString());
                mLoadingLayout.setVisibility(GONE);
                try {
                    if (response.getBoolean("success")) {

                        mMainButton.setEnabled(true);

                        list.clear();


                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").length(); i++) {

                            frozen_food = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getBoolean("blnFrozenFood");

                            if (frozen_food) {
                                hide_frozen = false;
                                break;
                            }
                        }

                        if (hide_frozen) {

                            mTextView4.setVisibility(GONE);

                        } else {
                            mTextView4.setVisibility(VISIBLE);

                            /*final ProgressDialog loginProgress = new ProgressDialog(OrderAcceptActivity.this);
                            loginProgress.setMessage("Please Wait...");
                            loginProgress.show();

                            loginProgress.dismiss();*/
                        }


                        for (int i = 0; i < jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").length(); i++) {

                            String imageUrl = "";
                            try {
                                //  JSONObject imgObject = (JSONObject) jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getJSONArray("strImagUrl").get(0);

                                imageUrl = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getJSONArray("strImagUrl").getJSONObject(0).optString("imageUrl");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String itemName = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("strProductName");
                            String amount = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("intTotalAmount");
                            String itemsCount = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("intQuantity");
                            String blnCheck = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("blnCheck");
                            String itemsUnit = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("strBarcode") /*+ " " + jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("strUnit")*/;
                            frozen_food = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getBoolean("blnFrozenFood");

                            if (jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).has("strStockAvailability")) {

                                stock = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("strStockAvailability");

                                if (stock.equals("C")) {
                                    stock = "Critical Stock : " + String.format("%.0f", Double.parseDouble(jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("intStockCount")));
                                }
                            }
                            String barcode = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("strBarcode");

                            list.add(new ModelOrderItems(imageUrl, itemsCount, amount, itemName, itemsUnit, frozen_food, stock, barcode, blnCheck));

                        }


                        String total_amount_top = jsonArray.getJSONObject(0).optString("intGrandTotal");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String Delivery_Address = jsonArray.getJSONObject(0).getJSONArray("arrayAddress").getJSONObject(0).optString("strUserName")
                                + "\n" +
                                jsonArray.getJSONObject(0).getJSONArray("arrayAddress").getJSONObject(0).optString("strAddress") +
                                "\n" + jsonArray.getJSONObject(0).getJSONArray("arrayAddress").getJSONObject(0).optString("strEmirate")
                                + "\n" + jsonArray.getJSONObject(0).getJSONArray("arrayAddress").getJSONObject(0).optString("strLandmark")
                                + "\n" + jsonArray.getJSONObject(0).getJSONArray("arrayAddress").getJSONObject(0).optString("strPhone");

                        String strPlace = jsonArray.getJSONObject(0).getJSONArray("arrayAddress").getJSONObject(0).optString("strLocation");
                        String strEmirate = jsonArray.getJSONObject(0).getJSONArray("arrayAddress").getJSONObject(0).optString("strEmirate");
                        TextView txtEmirate = findViewById(R.id.txtPlace);
                        if (!strPlace.equalsIgnoreCase("null")) {
                            txtEmirate.setText(strPlace);
                        } else {
                            txtEmirate.setText(strEmirate);
                        }
                        if (jsonArray.getJSONObject(0).has("arrayUser")) {
                            String userPhone = jsonArray.getJSONObject(0).getJSONArray("arrayUser").getJSONObject(0).optString("strPhone");
                            Delivery_Address += "\n" + userPhone;
                        }

                        String payment_type = jsonArray.getJSONObject(0).optString("strPaymentMode");
                        String sub_total = jsonArray.getJSONObject(0).optString("intSubTotal");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String delivery_charges = jsonArray.getJSONObject(0).optString("intDeliveryCharge");
                        String discounts = jsonArray.getJSONObject(0).optString("intDiscount");
                        JSONArray promoArray = jsonArray.getJSONObject(0).getJSONArray("arrayPromocodeDetals");
                        if (promoArray.length() > 0) {
                            JSONObject promoObj = (JSONObject) promoArray.get(0);
                            String promoName = promoObj.optString("strGroupName");
                            if (!promoName.isEmpty()) {
                                txtDiscountName.setText("Discounts(" + promoName + ")");
                            }

                        }
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String total_amount_bottom = jsonArray.getJSONObject(0).optString("intGrandTotal");
                        String total_no_Items = jsonArray.getJSONObject(0).optString("intTotalItemQuantity");

                        String rewardDiscount = jsonArray.getJSONObject(0).optString("intRewardDiscount");
                        txtRewardDiscount.setText("AED " + (rewardDiscount));


                        dinominationPrice = jsonArray.getJSONObject(0).optDouble("intCurrencyDenomination");
                        if (dinominationPrice > 0) {
                            dinominationLayout.setVisibility(VISIBLE);
                        } else {
                            dinominationLayout.setVisibility(GONE);
                        }
                        txtDinominationPrice.setText("AED " + String.format("%.2f", Double.parseDouble(String.valueOf(dinominationPrice))));


                        if (jsonArray.getJSONObject(0).has("objExtraAmount") && jsonArray.getJSONObject(0).getJSONObject("objExtraAmount").length() > 0) {
                            priceVariationLayout.setVisibility(VISIBLE);
                            price_variation = jsonArray.getJSONObject(0).getJSONObject("objExtraAmount").optDouble("intAmount");
                        } else {
                            priceVariationLayout.setVisibility(GONE);
                        }
                        txtPriceVariation.setText("AED " + String.format("%.2f", Double.parseDouble(String.valueOf(price_variation))));

                        mTxtDeliveryAddress.setText(Delivery_Address);
                        mTxtDeliveryCharge.setText("AED " + String.format("%.2f", Double.parseDouble(delivery_charges)));

                        mTxtSubTotal.setText("AED " + String.format("%.2f", Double.parseDouble(sub_total)));

                        mTxtPaymentType.setText(payment_type);

                        mTxtPriceTop.setText(String.format("%.2f", Double.parseDouble(total_amount_top)));
                        mTxtTotalPrice.setText("AED " + String.format("%.2f", Double.parseDouble(total_amount_bottom)));

                        mTxtDiscount.setText("AED " + String.format("%.2f", Double.parseDouble(discounts)));
                        mTxtDayOfDate.setText(total_no_Items);

                        String timeSlot = "", deliveryType = "";
                        if (jsonArray.getJSONObject(0).has("arrayTimeSlot")) {
                            timeSlot = getSlotDate(jsonArray.getJSONObject(0).getJSONArray("arrayTimeSlot").getJSONObject(0).optString("dateSlot")) + " " + jsonArray.getJSONObject(0).getJSONArray("arrayTimeSlot").getJSONObject(0).optString("strDisplayName");

                            mTimeSlotSelected.setText(timeSlot);

                            String timeCreated = getCreatedDate(jsonArray.getJSONObject(0).optString("dateCreateDateAndTime"));
                            mTimeOrderPlaced.setText(timeCreated);

                            String specialNote = jsonArray.getJSONObject(0).optString("strSpecialInst");

                            if (specialNote.length() > 0) {
                                mTxtSpecialNote.setText(specialNote);
                                mLayoutSpecialNote.setVisibility(VISIBLE);
                            } else {
                                mLayoutSpecialNote.setVisibility(GONE);
                            }

                            if (jsonArray.getJSONObject(0).getJSONArray("arrayTimeSlot").getJSONObject(0).has("strDeliveryType")) {
                                deliveryType = jsonArray.getJSONObject(0).getJSONArray("arrayTimeSlot").getJSONObject(0).optString("strDeliveryType");

                                mTxtDeliveryType.setText(deliveryType);

                            } else {
                                mLayoutDeliveryType.setVisibility(GONE);
                            }


                        } else {
                            timeSlot = "";
                            mSlotSelectedLabel.setVisibility(GONE);
                            mLayoutSpecialNote.setVisibility(GONE);
                            mLayoutDeliveryType.setVisibility(GONE);
                        }


                       /* final ProgressDialog loginProgress = new ProgressDialog(OrderAcceptActivity.this);
                        loginProgress.setMessage("Please Wait...");
                        loginProgress.show();*/


                        mLoadingLayout.setVisibility(GONE);

                        mOrderItemsRecycler.setVisibility(VISIBLE);

                        mOrderItemsRecycler.setAdapter(new AdapterOrderDetailsItems(list, OrderAcceptActivity.this, new AdapterOrderDetailsItems.AdapterEvents() {
                            @Override
                            public void onItemClick(ModelOrderItems data) {
                            }
                        }));


                    } else {
                        mLoadingLayout.setVisibility(GONE);
                        mOrderItemsRecycler.setVisibility(GONE);
                        if (response.optString("message") == " No Data Found") { // user has made no orders

                            mErrorLayout.setVisibility(VISIBLE);
                            mTxtErrorMessage.setText(response.optString("message"));

                        } else {
                            mErrorLayout.setVisibility(VISIBLE);
                            mTxtErrorMessage.setText(response.optString("message"));

                          /*  MyDialogSheet dialogSheet = new MyDialogSheet(OrderAcceptActivity.this);
                            dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                    .setMessage(response.optString("message"))
                                    .setPositiveButton(getString(R.string.dialog_button_retry),
                                            new DialogSheet.OnPositiveClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    addData();
                                                }
                                            })
                                    .setBackgroundColor(ContextCompat.getColor(OrderAcceptActivity.this, R.color.white))
                                    .setButtonsColorRes(R.color.colorAccent)
                                    .setNegativeButton(getString(R.string.dialog_button_cancel),
                                            null);
                            dialogSheet.show();*/


                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mLoadingLayout.setVisibility(GONE);
                mOrderItemsRecycler.setVisibility(GONE);
                Utils.checkVolleyError(OrderAcceptActivity.this, error);
                MyDialogSheet dialogSheet = new MyDialogSheet(OrderAcceptActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(OrderAcceptActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addData();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(OrderAcceptActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(OrderAcceptActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(clearNotifRequest);

    }


    private void callAcceptOrder() {

        frozen_food = false;

        // mLoadingLayout.setVisibility(VISIBLE);

        final ProgressDialog loginProgress = new ProgressDialog(OrderAcceptActivity.this);
        loginProgress.setMessage("Please Wait...");
        loginProgress.setCancelable(false);
        loginProgress.show();

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("strLoginUserId", new UserSessionManager(OrderAcceptActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strOrderId", order_id/* new UserSessionManager(requireActivity()).getUserId()*/);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d("Request", requestBody.toString());
        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.MY_ORDERS_ACCEPT_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loginProgress.dismiss();

                // mLoadingLayout.setVisibility(GONE);
                try {
                    if (response.getBoolean("success")) {


                        //Log.d("Response", response.toString());

                        // JSONArray jsonArray = response.getJSONArray("data");

                        final P07FancyAlert alert = new P07FancyAlert(OrderAcceptActivity.this);
                        alert.setMessage("Order has been accepted");
                        alert.setButton("Continue", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alert.dismiss();
                                mMainButton.setText("Accepted");
                                mMainButton.setBackgroundColor(getResources().getColor(R.color.green));
                                mMainButton.setEnabled(false);
                                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                OrderAcceptActivity.this.finish();
//                                dashboardIntent = new DashboardIntent();
//                                dashboardIntent.dashboardIntent(getApplicationContext(), DashboardActivity.class);
//                                OrderAcceptActivity.this.finish();

                            }
                        });
                        alert.setGif(R.raw.animation_success);
                        alert.show();

                    } else {

                        MyDialogSheet dialogSheet = new MyDialogSheet(OrderAcceptActivity.this);
                        dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                .setMessage(response.optString("message"))
                                .setPositiveButton(getString(R.string.dialog_button_retry),
                                        new DialogSheet.OnPositiveClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                callAcceptOrder();
                                            }
                                        })
                                .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white))
                                .setButtonsColorRes(R.color.colorAccent)
                                .setNegativeButton(getString(R.string.dialog_button_cancel),
                                        null);
                        dialogSheet.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loginProgress.dismiss();
                Utils.checkVolleyError(OrderAcceptActivity.this, error);
                MyDialogSheet dialogSheet = new MyDialogSheet(OrderAcceptActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(OrderAcceptActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callAcceptOrder();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(OrderAcceptActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(OrderAcceptActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(clearNotifRequest);


    }

    private void initViews() {

        txtRewardDiscount = findViewById(R.id.txt_reward_discount);

        dinominationLayout = findViewById(R.id.dinominationLayout);
        txtDinominationPrice = findViewById(R.id.txt_denomination);
        txtPriceVariation = findViewById(R.id.txtPriceVariatioAmount);
        priceVariationLayout = findViewById(R.id.priceVariationLayout);

        mSlotSelectedLabel = findViewById(R.id.slot_selected_label);
        mTxtDeliveryType = findViewById(R.id.txt_delivery_type);
        mLayoutDeliveryType = findViewById(R.id.layout_delivery_type);

        mTimeOrderPlaced = findViewById(R.id.time_order_placed);
        mTimeSlotSelected = findViewById(R.id.time_slot_selected);
        mTxtSpecialNote = findViewById(R.id.txt_special_note);
        mLayoutSpecialNote = findViewById(R.id.layout_special_note);

        mBackIcon = findViewById(R.id.back_icon);
        mTitleToolbar = findViewById(R.id.title_toolbar);
        mTxtDayOfDate = findViewById(R.id.txt_day_of_date);
        mTxtMonthOfDate = findViewById(R.id.txt_month_of_date);
        mTxtOrderIdTop = findViewById(R.id.txt_order_id_top);
        mTxtPriceTop = findViewById(R.id.txt_price_top);
        mHeader = findViewById(R.id.header);
        mIcOrderStar = findViewById(R.id.ic_order_star);
        mTxtOrdered = findViewById(R.id.txt_ordered);
        mOrderedStar = findViewById(R.id.orderedStar);
        mIcDeliveryStar = findViewById(R.id.ic_delivery_star);
        mTxtDelivered = findViewById(R.id.txt_delivered);
        mUserInfo = findViewById(R.id.userInfo);
        mTextView4 = findViewById(R.id.textView4);
        mOrderItemsRecycler = findViewById(R.id.orderItemsRecycler);
        mTxtDeliveryAddress = findViewById(R.id.txt_delivery_address);
        mLayoutAddress = findViewById(R.id.layout_address);
        mImgPaymentType = findViewById(R.id.img_payment_type);
        mTxtPaymentType = findViewById(R.id.txt_payment_type);
        mLayoutPaymentType = findViewById(R.id.layout_payment_type);
        mTxtSubTotal = findViewById(R.id.txt_sub_total);
        mTxtDeliveryCharge = findViewById(R.id.txt_delivery_charge);
        mTxtDiscount = findViewById(R.id.txtDiscount);
        txtDiscountName = findViewById(R.id.txtDiscountName);
        mTxtTotalPrice = findViewById(R.id.txt_total_price);
        mLayoutPaymentInfo = findViewById(R.id.layout_payment_info);
        mSecondButton = findViewById(R.id.secondButton);
        mLayoutData = findViewById(R.id.layoutData);
        mMainButton = findViewById(R.id.mainButton);
        mBottomButtons = findViewById(R.id.bottomButtons);
        mImgError = findViewById(R.id.img_error);
        mTxtErrorTitle = findViewById(R.id.txt_error_title);
        mTxtErrorMessage = findViewById(R.id.txt_error_message);
        mBtnRetry = findViewById(R.id.btn_retry);
        mErrorLayout = findViewById(R.id.error_layout);
        mErrorParentLayout = findViewById(R.id.error_parent_layout);
        mLoadingProgress = findViewById(R.id.loadingProgress);
        mLoadingLayout = findViewById(R.id.loadingLayout);

        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        mTitleToolbar.setText("New Order");
        order_details_layout = findViewById(R.id.order_details_layout);

    }


    public void fetchOrderDetails(String orderId) {
        getData();

    }

    private void getData() {

        addData();
    }


    public void onClickPrint(View view) {

    }

    public void onClickCancel(View view) {
        final MyDialogSheet dialog = new MyDialogSheet(OrderAcceptActivity.this);
        dialog.setTitle("Cancel Order");
        dialog.setMessage("Are you sure to cancel this order");
        dialog.setPositiveButton("Yes", new DialogSheet.OnPositiveClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }


    public String getSlotDate(String notificationDate) {
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

    public String getCreatedDate(String notificationDate) {
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


        return DateFormat.format("EEEE,dd MMM yyyy,", notifDate).toString()
                + " "
                + DateFormat.format(timeFormatString, notifDate);

    }

    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this, orderId);
    }
}
