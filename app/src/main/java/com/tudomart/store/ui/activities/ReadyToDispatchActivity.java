package com.tudomart.store.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputEditText;
import com.marcoscg.dialogsheet.DialogSheet;
import com.tudomart.store.adapters.orders.orderDetails.AdapterOrderDetailsItems;
import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.ui.customViews.P07FancyAlert;
import com.tudomart.store.utils.Utils;

import com.tudomart.store.R;

import com.tudomart.store.adapters.orders.orderDetails.AdapterOrderDetailsItems;
import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.helpers.network.RequestController;
import com.tudomart.store.helpers.network.VolleyErrorHandler;
import com.tudomart.store.helpers.sharedPref.UserSessionManager;
import com.tudomart.store.model.order.orderDetails.ModelOrderItems;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.ui.customViews.P07FancyAlert;
import com.tudomart.store.utils.Utils;

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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ReadyToDispatchActivity extends BaseActivity {
    private ImageView backIcon;
    private TextView titleToolbar;
    private TextView txtDayOfDate;
    private TextView txtMonthOfDate;
    private TextView txtOrderIdTop;
    private TextView txtPriceTop;
    private LinearLayout header;
    private ImageView icOrderStar;
    private TextView txtOrdered;
    private LinearLayout orderedStar;
    private ImageView icDeliveryStar;
    private TextView txtDelivered;
    private TextView userInfo;
    private TextView textView4;
    private RecyclerView orderItemsRecycler;
    private TextView txtSubTotal;
    private TextView txtDeliveryCharge;
    private TextView txtDiscount;
    private TextView txtTotalPrice;
    private CardView layoutPaymentInfo;
    private TextView txtDeliveryAddress;
    private CardView layoutAddress;
    private ImageView imgPaymentType;
    private TextView txtPaymentType;
    private CardView layoutPaymentType;
    private NestedScrollView layoutData;
    private Button mainButton;
    private LinearLayout bottomButtons;
    private ImageView imgError;
    private TextView txtErrorTitle;
    private TextView txtErrorMessage;
    private Button btnRetry;
    private RelativeLayout errorLayout;
    private LinearLayout errorParentLayout;
    private ProgressBar loadingProgress;
    private RelativeLayout loadingLayout;
    private EditText edtInvoice;
    String order_id = "";
    boolean frozen_food = false, hide_frozen = true;
    ImageView button;
    private ArrayList<ModelOrderItems> list = new ArrayList<>();

    private TextView mTimeOrderPlaced;
    private TextView mTimeSlotSelected;
    private TextView mTxtSpecialNote;
    private CardView mLayoutSpecialNote;
    private CardView mSlotSelectedLabel;
    private TextView mTxtDeliveryType;
    private CardView mLayoutDeliveryType;

    private TextView txtPriceVariation;
    private LinearLayout priceVarioationLayout;
    private TextView txtDinomination;
    private CardView dinominationLayout;

    private double price_variation;
    private double dinominationPrice;

    private TextInputEditText mTxtCardAmount;
    private TextInputEditText mTxtCashAmount;

    private Boolean paymentTypeBool = false;

    private CardView splitAmountType;

    private TextView txtRewardDiscount;
    private Button mainBtnStore;


    String stock = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_to_dispatch);
        initLayout();

        Intent intent = getIntent();
        order_id = intent.getStringExtra("ORDER_ID");

        txtOrderIdTop.setText(order_id);

        getData();
        initInvoiceText();


        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleToolbar.setText("Ready to Dispatch");


    }

    private void initLayout() {

        mainBtnStore = findViewById(R.id.mainButtonStore);
        txtRewardDiscount = findViewById(R.id.txt_reward_discount);
        splitAmountType = findViewById(R.id.layout_split_amount_type);

        mTxtCardAmount = findViewById(R.id.txt_card_amount);
        mTxtCashAmount = findViewById(R.id.txt_cash_amount);

        txtPriceVariation = findViewById(R.id.txt_price_variation);
        priceVarioationLayout = findViewById(R.id.priceVariationLayout);
        txtDinomination = findViewById(R.id.txt_denomination);
        dinominationLayout = findViewById(R.id.dinominationLayout);

        mTxtDeliveryType = findViewById(R.id.txt_delivery_type);
        mLayoutDeliveryType = findViewById(R.id.layout_delivery_type);

        mSlotSelectedLabel = findViewById(R.id.slot_selected_label);
        mTimeOrderPlaced = findViewById(R.id.time_order_placed);
        mTimeSlotSelected = findViewById(R.id.time_slot_selected);
        mTxtSpecialNote = findViewById(R.id.txt_special_note);
        mLayoutSpecialNote = findViewById(R.id.layout_special_note);

        edtInvoice = findViewById(R.id.edtInvoice);

        backIcon = findViewById(R.id.back_icon);
        titleToolbar = findViewById(R.id.title_toolbar);
        txtDayOfDate = findViewById(R.id.txt_day_of_date);
        txtMonthOfDate = findViewById(R.id.txt_month_of_date);
        txtOrderIdTop = findViewById(R.id.txt_order_id_top);
        txtPriceTop = findViewById(R.id.txt_price_top);
        header = findViewById(R.id.header);
        icOrderStar = findViewById(R.id.ic_order_star);
        txtOrdered = findViewById(R.id.txt_ordered);
        orderedStar = findViewById(R.id.orderedStar);
        icDeliveryStar = findViewById(R.id.ic_delivery_star);
        txtDelivered = findViewById(R.id.txt_delivered);
        userInfo = findViewById(R.id.userInfo);
        textView4 = findViewById(R.id.textView4);
        orderItemsRecycler = findViewById(R.id.orderItemsRecycler);
        txtSubTotal = findViewById(R.id.txt_sub_total);
        txtDeliveryCharge = findViewById(R.id.txt_delivery_charge);
        txtDiscount = findViewById(R.id.txtDiscount);
        txtTotalPrice = findViewById(R.id.txt_total_price);
        layoutPaymentInfo = findViewById(R.id.layout_payment_info);
        txtDeliveryAddress = findViewById(R.id.txt_delivery_address);
        layoutAddress = findViewById(R.id.layout_address);
        imgPaymentType = findViewById(R.id.img_payment_type);
        txtPaymentType = findViewById(R.id.txt_payment_type);
        layoutPaymentType = findViewById(R.id.layout_payment_type);
        layoutData = findViewById(R.id.layoutData);
        mainButton = findViewById(R.id.mainButton);
        bottomButtons = findViewById(R.id.bottomButtons);
        imgError = findViewById(R.id.img_error);
        txtErrorTitle = findViewById(R.id.txt_error_title);
        txtErrorMessage = findViewById(R.id.txt_error_message);
        btnRetry = findViewById(R.id.btn_retry);
        errorLayout = findViewById(R.id.error_layout);
        errorParentLayout = findViewById(R.id.error_parent_layout);
        loadingProgress = findViewById(R.id.loadingProgress);
        loadingLayout = findViewById(R.id.loadingLayout);
        button = findViewById(R.id.addInvoiceButton);
    }

    public void onClickReadyToDispatchLayout(View view) {
        if (userInfo.getVisibility() == VISIBLE) {
            userInfo.setVisibility(GONE);
        } else {
            userInfo.setText("Order packed by aeth Analytica on 19 Apr 20, 10:10PM");
            userInfo.setVisibility(VISIBLE);
        }
    }

    public void onClickPackingLayout(View view) {
        if (userInfo.getVisibility() == VISIBLE) {
            userInfo.setVisibility(GONE);
        } else {
            userInfo.setText("Order accepted by aeth Analytica on 19 Apr 20, 10:10PM");
            userInfo.setVisibility(VISIBLE);
        }
    }

    public void onClickOrderedLayout(View view) {
        if (userInfo.getVisibility() == VISIBLE) {
            userInfo.setVisibility(GONE);
        } else {
            userInfo.setText("Order placed by aeth Analytica on 19 Apr 20, 10:10PM");
            userInfo.setVisibility(VISIBLE);
        }
    }

    private void getData() {
//        for (int i = 0; i < 2; i++) {
//            /*list.add(new ModelOrderItems(DummyData.drinks[0], "3", "30", "Product Name", false));
//            list.add(new ModelOrderItems(DummyData.drinks[0], "3", "30", "Product Name", true));*/
//        }


        loadingLayout.setVisibility(VISIBLE);

        RequestQueue rQueue = Volley.newRequestQueue(ReadyToDispatchActivity.this);
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("strLoginUserId", new UserSessionManager(ReadyToDispatchActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strStoreId", new UserSessionManager(ReadyToDispatchActivity.this).getUserDetails().get("shop")/* new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strOrderID", order_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d("RequestBody", requestBody.toString());

        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.ALL_ORDERS_DETAILS_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loadingLayout.setVisibility(GONE);
                try {
                    if (response.getBoolean("success")) {

                        //Log.d("RequestBody", response.toString());
                        list.clear();
                        orderItemsRecycler.setVisibility(VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").length(); i++) {

                            String imageUrl = "";
                            try {
                                //JSONObject imgObject = (JSONObject) jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getJSONArray("strImagUrl").get(0);

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


                        for (int i = 0; i < jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").length(); i++) {

                            frozen_food = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getBoolean("blnFrozenFood");

                            if (frozen_food) {
                                hide_frozen = false;
                                break;
                            }
                        }

                        if (hide_frozen) {
                            textView4.setVisibility(GONE);
                        } else {
                            textView4.setVisibility(VISIBLE);
                        }

                        String invoice_number = jsonArray.getJSONObject(0).optString("strInvoiceNumber");
                        edtInvoice.setText(invoice_number);

                        if (invoice_number.length() > 0) {
                            button.setBackgroundColor(Color.TRANSPARENT);
                            button.setImageDrawable(ContextCompat.getDrawable(ReadyToDispatchActivity.this, R.drawable.ic_check));
                            edtInvoice.setText(edtInvoice.getText());
                            button.setEnabled(false);
                        } else {
                            button.setImageDrawable(ContextCompat.getDrawable(ReadyToDispatchActivity.this, R.drawable.ic_add));
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
                        if (payment_type.equalsIgnoreCase("Collect from store")) {
                            paymentTypeBool = true;
                            splitAmountType.setVisibility(VISIBLE);
                            mainButton.setEnabled(false);
                            //                  mainButton.setText("Delivery");
                            mainBtnStore.setVisibility(VISIBLE);
                        } else {
                            mainButton.setVisibility(VISIBLE);
                        }

                        String sub_total = jsonArray.getJSONObject(0).optString("intSubTotal");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String delivery_charges = jsonArray.getJSONObject(0).optString("intDeliveryCharge");
                        String discounts =  jsonArray.getJSONObject(0).getString("intDiscount");;
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String total_amount_bottom = jsonArray.getJSONObject(0).optString("intGrandTotal");
                        String total_no_Items = jsonArray.getJSONObject(0).optString("intTotalItemQuantity");

                        String rewardDiscount = jsonArray.getJSONObject(0).optString("intRewardDiscount");
                        //       txtRewardDiscount.setText(rewardDiscount);
                        txtRewardDiscount.setText("AED " + String.format("%.2f", Double.parseDouble(rewardDiscount)));

                        // dinomination
                        dinominationPrice = jsonArray.getJSONObject(0).optDouble("intCurrencyDenomination");
                        if (dinominationPrice > 0) {
                            dinominationLayout.setVisibility(VISIBLE);
                        } else {
                            dinominationLayout.setVisibility(GONE);
                        }
                        txtDinomination.setText("AED " + String.format("%.2f", Double.parseDouble(String.valueOf(dinominationPrice))));

                        if (jsonArray.getJSONObject(0).has("objExtraAmount") && jsonArray.getJSONObject(0).getJSONObject("objExtraAmount").length() > 0) {
                            priceVarioationLayout.setVisibility(VISIBLE);
                            price_variation = jsonArray.getJSONObject(0).getJSONObject("objExtraAmount").optDouble("intAmount");
                        } else {
                            priceVarioationLayout.setVisibility(GONE);
                        }
                        txtPriceVariation.setText("AED " + String.format("%.2f", Double.parseDouble(String.valueOf(price_variation))));

                        txtDeliveryAddress.setText(Delivery_Address);
                        txtDeliveryCharge.setText("AED " + String.format("%.2f", Double.parseDouble(delivery_charges)));

                        txtSubTotal.setText("AED " + String.format("%.2f", Double.parseDouble(sub_total)));

                        txtPaymentType.setText(payment_type);

                        txtPriceTop.setText(String.format("%.2f", Double.parseDouble(total_amount_top)));

                        txtTotalPrice.setText("AED " + String.format("%.2f", Double.parseDouble(total_amount_bottom)));

                        txtDiscount.setText("AED " + String.format("%.2f", Double.parseDouble(discounts)));
                        txtDayOfDate.setText(total_no_Items);

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


                      /*  final ProgressDialog loginProgress = new ProgressDialog(ReadyToDispatchActivity.this);
                        loginProgress.setMessage("Please Wait...");
                        loginProgress.show();*/


                        orderItemsRecycler.setVisibility(VISIBLE);
                        orderItemsRecycler.setAdapter(new AdapterOrderDetailsItems(list, ReadyToDispatchActivity.this, new AdapterOrderDetailsItems.AdapterEvents() {
                            @Override
                            public void onItemClick(ModelOrderItems data) {
                            }
                        }));


                    } else {
                        loadingLayout.setVisibility(GONE);
                        orderItemsRecycler.setVisibility(GONE);
                        if (response.optString("message") == " No Data Found") { // user has made no orders

                            errorLayout.setVisibility(VISIBLE);
                            txtErrorMessage.setText(response.optString("message"));

                        } else {

                            errorLayout.setVisibility(VISIBLE);
                            txtErrorMessage.setText(response.optString("message"));

                            MyDialogSheet dialogSheet = new MyDialogSheet(ReadyToDispatchActivity.this);
                            dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                    .setMessage(response.optString("message"))
                                    .setPositiveButton(getString(R.string.dialog_button_retry),
                                            new DialogSheet.OnPositiveClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    getData();
                                                }
                                            })
                                    .setBackgroundColor(ContextCompat.getColor(ReadyToDispatchActivity.this, R.color.white))
                                    .setButtonsColorRes(R.color.colorAccent)
                                    .setNegativeButton(getString(R.string.dialog_button_cancel),
                                            null);
                            dialogSheet.show();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadingLayout.setVisibility(GONE);
                orderItemsRecycler.setVisibility(GONE);
                Utils.checkVolleyError(ReadyToDispatchActivity.this, error);
                MyDialogSheet dialogSheet = new MyDialogSheet(ReadyToDispatchActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(ReadyToDispatchActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getData();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(ReadyToDispatchActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(ReadyToDispatchActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(clearNotifRequest);

    }

    public void onClickAddInvoiceNumber(final View view) {
        if (edtInvoice.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter a valid Invoice number", Toast.LENGTH_LONG).show();
        } else {

            callSaveInvoiceNumber();
            /*final P07FancyAlert alert = new P07FancyAlert(ReadyToDispatchActivity.this);
            alert.setMessage("Invoice number has been noted");
            alert.setButton("Continue", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    ImageView button = (ImageView) view;
                    button.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check));
                    edtInvoice.setText(edtInvoice.getText());
                    view.setEnabled(false);

                }
            });
            alert.setGif(R.raw.animation_success);
            alert.show();*/
        }
    }


    public void onClickReadyToDispatch(View view) {
        MyDialogSheet dialog = new MyDialogSheet(ReadyToDispatchActivity.this);
        dialog.setTitle("Change Status");
        dialog.setMessage("Are you sure to update the store status");
        dialog.setPositiveButton("Yes", new DialogSheet.OnPositiveClickListener() {
            @Override
            public void onClick(View v) {

//                if (edtInvoice.getText().toString().length() > 0 && mTxtCardAmount.getText().toString().length() > 0 && mTxtCashAmount.getText().toString().length() > 0) {
//                    mainButton.setEnabled(true);
//                    callTheStoreApiDispatch();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please Enter Card and Cash field !", Toast.LENGTH_LONG).show();
//                }
                if (paymentTypeBool && mTxtCardAmount.getText().toString().length() > 0 && mTxtCashAmount.getText().toString().length() > 0) {
                    callTheStoreApiDispatch();
                    mainBtnStore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            callTheStoreApiDispatch();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Check payment amount field", Toast.LENGTH_SHORT).show();
                }
//                if (edtInvoice.getText().toString().length() > 0) {
//                    // mainButton.setEnabled(true);
//                    callTheStoreApiDispatch();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please Enter Card and Cash field !", Toast.LENGTH_LONG).show();
//                }

            }
               /* final P07FancyAlert alert = new P07FancyAlert(PackingActivity.this);
                alert.setMessage("Order status has been updated");
                alert.setButton("Continue", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        PackingActivity.this.finish();
                    }
                });
                alert.setGif(R.raw.animation_success);
                alert.show();*/
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    private void callTheStoreApiDispatch() {

        paidAmount();
        // callSaveInvoiceNumber_second();
        final ProgressDialog loginProgress = new ProgressDialog(ReadyToDispatchActivity.this);
        loginProgress.setMessage("Please Wait...");
        loginProgress.setCancelable(false);
        loginProgress.show();

        // loadingLayout.setVisibility(VISIBLE);

        JSONObject requestBody = new JSONObject();

        try {

            requestBody.put("strOrderId", order_id);
            //  requestBody.put("strTripNumber", trip_number);
            requestBody.put("strLoginUserId", new UserSessionManager(ReadyToDispatchActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strCashAmount", mTxtCashAmount.getText().toString());
            requestBody.put("strCardAmount", mTxtCardAmount.getText().toString());
            requestBody.put("strTotalCollectedAmount", String.format("%.2f", Double.parseDouble(mTxtCashAmount.getText().toString()) + Double.parseDouble(mTxtCardAmount.getText().toString())));
            requestBody.put("strDeviationAmount", String.format("%.2f", Double.parseDouble(txtPriceTop.getText().toString()) - (Double.parseDouble(mTxtCashAmount.getText().toString()) + Double.parseDouble(mTxtCardAmount.getText().toString()))));
            requestBody.put("strUserId", getIntent().getStringExtra("ORDER_USER_ID"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest allStoreDispachRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.PICKUP_STORE_STATUS, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loginProgress.dismiss();

                try {
                    if (response.getBoolean("success")) {
                        final P07FancyAlert alert = new P07FancyAlert(ReadyToDispatchActivity.this);
                        alert.setMessage("Pickup store order has been accepted");
                        alert.setButton("Continue", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alert.dismiss();
                                mainButton.setText("Store Dispatched");
                                mainButton.setEnabled(false);
                                mainButton.setBackgroundColor(getResources().getColor(R.color.green));
                               /* startActivity(new Intent(getApplicationContext(), PackingOrdersActivity.class));
                                PackingActivity.this.finish();*/

                            }
                        });
                        alert.setGif(R.raw.animation_success);
                        alert.show();
                    } else {
                        MyDialogSheet dialogSheet = new MyDialogSheet(ReadyToDispatchActivity.this);
                        dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                .setMessage(response.optString("message"))
                                .setPositiveButton(getString(R.string.dialog_button_retry),
                                        new DialogSheet.OnPositiveClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                callTheStoreApiDispatch();
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
                Utils.checkVolleyError(ReadyToDispatchActivity.this, error);
                MyDialogSheet dialogSheet = new MyDialogSheet(ReadyToDispatchActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(ReadyToDispatchActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callTheStoreApiDispatch();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(ReadyToDispatchActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(ReadyToDispatchActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };
        RequestController.getInstance().addToRequestQueue(allStoreDispachRequest);
    }


//    private void callSaveInvoiceNumber_second() {
//
//        loadingLayout.setVisibility(VISIBLE);
//
//        RequestQueue rQueue = Volley.newRequestQueue(ReadyToDispatchActivity.this);
//        JSONObject requestBody = new JSONObject();
//
//        try {
//            requestBody.put("strLoginUserId", new UserSessionManager(ReadyToDispatchActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
//            requestBody.put("strStoreId", new UserSessionManager(ReadyToDispatchActivity.this).getUserDetails().get("shop"));
//            requestBody.put("strOrderId", order_id);
//            requestBody.put("strInvoiceNumber", edtInvoice.getText().toString());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Log.d("RequestBody", requestBody.toString());
//
//        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.SAVE_INVOICE_URL, requestBody, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                loadingLayout.setVisibility(GONE);
//                try {
//                    if (response.getBoolean("success")) {
//
//                        Log.d("Response Body", response.toString());
//
//                        /*final P07FancyAlert alert = new P07FancyAlert(BillMyOrderActivity.this);
//                        alert.setMessage("Invoice number has been noted");
//                        alert.setButton("Continue", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                alert.dismiss();
//
//
//
//                            }
//                        });
//                        alert.setGif(R.raw.animation_success);
//                        alert.show();*/
//
//                        mAddInvoiceButton.setBackgroundColor(Color.TRANSPARENT);
//                        mAddInvoiceButton.setImageDrawable(ContextCompat.getDrawable(ReadyToDispatchActivity.this, R.drawable.ic_check));
//                        edtInvoice.setText(edtInvoice.getText());
//                        edtInvoice.setEnabled(false);
//                        mAddInvoiceButton.setEnabled(false);
//
//
//                    } else {
//                        mErrorLayout.setVisibility(GONE);
//
//                        if (response.optString("message") == " No Data Found") { // user has made no orders
//
//                            //errorLayout.setVisibility(VISIBLE);
//
//                        } else {
//
//                            // errorLayout.setVisibility(View.VISIBLE);
//                            MyDialogSheet dialogSheet = new MyDialogSheet(PackingActivity.this);
//                            dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
//                                    .setMessage(response.optString("message"))
//                                    .setPositiveButton(getString(R.string.dialog_button_retry),
//                                            new DialogSheet.OnPositiveClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    callSaveInvoiceNumber_second();
//                                                }
//                                            })
//                                    .setBackgroundColor(ContextCompat.getColor(PackingActivity.this, R.color.white))
//                                    .setButtonsColorRes(R.color.colorAccent)
//                                    .setNegativeButton(getString(R.string.dialog_button_cancel),
//                                            null);
//                            dialogSheet.show();
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                mLoadingLayout.setVisibility(GONE);
//                Utils.checkVolleyError(PackingActivity.this, error);
//                MyDialogSheet dialogSheet = new MyDialogSheet(PackingActivity.this);
//                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
//                        .setMessage(new VolleyErrorHandler(PackingActivity.this).getVolleyError(error))
//                        .setPositiveButton(getString(R.string.dialog_button_retry),
//                                new DialogSheet.OnPositiveClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        callSaveInvoiceNumber_second();
//                                    }
//                                })
//                        .setBackgroundColor(ContextCompat.getColor(PackingActivity.this, R.color.white))
//                        .setButtonsColorRes(R.color.colorAccent)
//                        .setNegativeButton(getString(R.string.dialog_button_cancel),
//                                null);
//                dialogSheet.show();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", new UserSessionManager(PackingActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
//                return params;
//            }
//        };
//
//        rQueue.add(clearNotifRequest);
//
//    }

    private void callSaveInvoiceNumber() {

        loadingLayout.setVisibility(VISIBLE);

        RequestQueue rQueue = Volley.newRequestQueue(ReadyToDispatchActivity.this);
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("strLoginUserId", new UserSessionManager(ReadyToDispatchActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strStoreId", new UserSessionManager(ReadyToDispatchActivity.this).getUserDetails().get("shop"));
            requestBody.put("strOrderId", order_id);
            requestBody.put("strInvoiceNumber", edtInvoice.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d("RequestBody", requestBody.toString());

        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.SAVE_INVOICE_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loadingLayout.setVisibility(GONE);
                try {
                    if (response.getBoolean("success")) {

                        //Log.d("Response Body", response.toString());

                        final P07FancyAlert alert = new P07FancyAlert(ReadyToDispatchActivity.this);
                        alert.setMessage("Invoice number has been noted");
                        alert.setButton("Continue", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                                button.setBackgroundColor(Color.TRANSPARENT);
                                button.setImageDrawable(ContextCompat.getDrawable(ReadyToDispatchActivity.this, R.drawable.ic_check));
                                edtInvoice.setText(edtInvoice.getText());
                                button.setEnabled(false);

                            }
                        });
                        alert.setGif(R.raw.animation_success);
                        alert.show();


                    } else {
                        errorLayout.setVisibility(GONE);

                        if (response.optString("message") == " No Data Found") { // user has made no orders

                            //errorLayout.setVisibility(VISIBLE);

                        } else {

                            // errorLayout.setVisibility(View.VISIBLE);
                            MyDialogSheet dialogSheet = new MyDialogSheet(ReadyToDispatchActivity.this);
                            dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                    .setMessage(response.optString("message"))
                                    .setPositiveButton(getString(R.string.dialog_button_retry),
                                            new DialogSheet.OnPositiveClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    callSaveInvoiceNumber();
                                                }
                                            })
                                    .setBackgroundColor(ContextCompat.getColor(ReadyToDispatchActivity.this, R.color.white))
                                    .setButtonsColorRes(R.color.colorAccent)
                                    .setNegativeButton(getString(R.string.dialog_button_cancel),
                                            null);
                            dialogSheet.show();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadingLayout.setVisibility(GONE);
                Utils.checkVolleyError(ReadyToDispatchActivity.this, error);
                MyDialogSheet dialogSheet = new MyDialogSheet(ReadyToDispatchActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(ReadyToDispatchActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callSaveInvoiceNumber();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(ReadyToDispatchActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(ReadyToDispatchActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(clearNotifRequest);

    }

    private void initInvoiceText() {
        final ImageView addInvoiceButton;
        addInvoiceButton = findViewById(R.id.addInvoiceButton);
        edtInvoice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //addInvoiceButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_check));
                addInvoiceButton.setEnabled(true);

            }
        });
    }

    private void paidAmount() {
        if (mTxtCashAmount.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Cash Amount Paid..!", Toast.LENGTH_LONG).show();


        } else if (mTxtCardAmount.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Card Amount Paid..!", Toast.LENGTH_LONG).show();

            mTxtCardAmount.setText("0.00");

        } else if (mTxtCashAmount.getText().toString().equals("") || mTxtCashAmount.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Amount Paid..!", Toast.LENGTH_LONG).show();

            mTxtCashAmount.setText("0.00");

        } else if (Double.parseDouble(mTxtCashAmount.getText().toString()) + Double.parseDouble(mTxtCardAmount.getText().toString()) > Double.parseDouble(txtPriceTop.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Collected Amount Can't be greater than Order Amount..!", Toast.LENGTH_LONG).show();

        }
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
