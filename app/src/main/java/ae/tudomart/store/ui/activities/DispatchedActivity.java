package ae.tudomart.store.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.marcoscg.dialogsheet.DialogSheet;

import ae.matajar.store.R;

import ae.tudomart.store.adapters.orders.orderDetails.AdapterOrderDetailsItems;
import ae.tudomart.store.helpers.network.ApiUrl;
import ae.tudomart.store.helpers.network.RequestController;
import ae.tudomart.store.helpers.network.VolleyErrorHandler;
import ae.tudomart.store.helpers.sharedPref.UserSessionManager;
import ae.tudomart.store.model.order.orderDetails.ModelOrderItems;
import ae.tudomart.store.ui.customViews.MyDialogSheet;
import ae.tudomart.store.utils.Utils;

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

public class DispatchedActivity extends BaseActivity {
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
    private TextView mUserInfo;
    private TextView textView4;
    private RecyclerView orderItemsRecycler;
    private TextView txtDeliveryAddress;
    private CardView layoutAddress;
    private ImageView imgPaymentType;
    private TextView txtPaymentType;
    private CardView layoutPaymentType;
    private TextView txtSubTotal;
    private TextView txtDeliveryCharge;
    private TextView txtDiscount;
    private TextView txtTotalPrice;
    private CardView layoutPaymentInfo;
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
    private ArrayList<ModelOrderItems> list = new ArrayList<>();
    private TextView edtInvoice;

    String order_id = "";

    Boolean frozen_food = false, hide_frozen = true;
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
    private TextView mTextView4;
    private RecyclerView mOrderItemsRecycler;
    private TextView mEdtInvoice;
    private ImageView mAddInvoiceButton;
    private TextView mTxtSubTotal;
    private TextView mTxtDeliveryCharge;
    private TextView mTxtDiscount;
    private TextView mTxtTotalPrice;
    private CardView mLayoutPaymentInfo;
    private TextView mTxtDeliveryAddress;
    private CardView mLayoutAddress;
    private ImageView mImgPaymentType;
    private TextView mTxtPaymentType;
    private CardView mLayoutPaymentType;
    private NestedScrollView mLayoutData;
    //  private Button mMainButton;
    private LinearLayout mBottomButtons;
    private ImageView mImgError;
    private TextView mTxtErrorTitle;
    private TextView mTxtErrorMessage;
    private Button mBtnRetry;
    private RelativeLayout mErrorLayout;
    private LinearLayout mErrorParentLayout;
    private ProgressBar mLoadingProgress;
    private RelativeLayout mLoadingLayout;

    private TextView txtPriceVariation;
    private LinearLayout priceVarioationLayout;
    private TextView txtDinomination;
    private CardView dinominationLayout;

    private double price_variation;
    private String dinominationPrice;

    String Date_Button = "";
    int status = 3;

    private TextView mTimeOrderPlaced;
    private TextView mTimeSlotSelected;
    private TextView mTxtSpecialNote;
    private CardView mLayoutSpecialNote;
    private CardView mSlotSelectedLabel;
    private TextView mTxtDeliveryType;
    private CardView mLayoutDeliveryType;

    private TextView txtRewardDiscount;

    String stock = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatched);
        initViews();
        initToolbar();

        Intent intent = getIntent();
        order_id = intent.getStringExtra("ORDER_ID");

        mTxtOrderIdTop.setText(order_id);

        order_id = intent.getStringExtra("ORDER_ID");
        Date_Button = intent.getStringExtra("DATE_KEY");

        status = intent.getIntExtra("STATUS", 3);
        txtOrderIdTop.setText(order_id);
        if (status == 4) {
//            mainButton.setVisibility(VISIBLE);
//            mainButton.setBackgroundColor(getResources().getColor(R.color.green));
//            mainButton.setText("Delivered on " + Date_Button);
            mIcDeliveryStar.setImageDrawable(ContextCompat.getDrawable(DispatchedActivity.this, R.drawable.ic_star_green));
            mTxtDelivered.setTextColor(getResources().getColor(R.color.green));
            mIcDeliveryStar.setImageResource(R.drawable.ic_star_green);
            mIcDeliveryStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_green, getApplicationContext().getTheme()));
        } else if (status == 5) {
            mainButton.setVisibility(VISIBLE);
            mainButton.setBackgroundColor(getResources().getColor(R.color.red));
            mainButton.setText("Returned on " + Date_Button);
            mIcDeliveryStar.setImageDrawable(ContextCompat.getDrawable(DispatchedActivity.this, R.drawable.ic_star_red));
            mTxtDelivered.setTextColor(getResources().getColor(R.color.red));
            mTxtDelivered.setText("Returned");
            mIcDeliveryStar.setImageResource(R.drawable.ic_star_red);
            mIcDeliveryStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_red, getApplicationContext().getTheme()));
        } else if (status == 6) {
            mainButton.setVisibility(VISIBLE);
            mainButton.setBackgroundColor(getResources().getColor(R.color.cancelled));
            mainButton.setText("Cancelled on " + Date_Button);
            mIcDeliveryStar.setImageDrawable(ContextCompat.getDrawable(DispatchedActivity.this, R.drawable.ic_star_cancelled));
            mTxtDelivered.setTextColor(getResources().getColor(R.color.cancelled));
            mTxtDelivered.setText("Cancelled");
            mIcDeliveryStar.setImageResource(R.drawable.ic_star_cancelled);
            mIcDeliveryStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_cancelled, getApplicationContext().getTheme()));
        } else {
            mainButton.setVisibility(VISIBLE);
            mainButton.setBackgroundColor(getResources().getColor(R.color.green));
            mainButton.setText("Dispatched on " + Date_Button);
        }

        // mainButton.setBackgroundColor(getResources().getColor(R.color.green));

        addData();
    }

    private void initRecycler() {


        orderItemsRecycler.setAdapter(new AdapterOrderDetailsItems(list, DispatchedActivity.this, new AdapterOrderDetailsItems.AdapterEvents() {
            @Override
            public void onItemClick(ModelOrderItems data) {
            }
        }));


    }

    private void addData() {
        mLoadingLayout.setVisibility(VISIBLE);

        RequestQueue rQueue = Volley.newRequestQueue(DispatchedActivity.this);
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("strLoginUserId", new UserSessionManager(DispatchedActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strStoreId", new UserSessionManager(DispatchedActivity.this).getUserDetails().get("shop")/* new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strOrderID", order_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.ALL_ORDERS_DETAILS_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                mLoadingLayout.setVisibility(GONE);
                try {
                    if (response.getBoolean("success")) {

                        list.clear();
                        mOrderItemsRecycler.setVisibility(VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").length(); i++) {

                            String imageUrl = "";
                            try {
                                //JSONObject imgObject = (JSONObject) jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getJSONArray("strImagUrl").get(0);

                                imageUrl = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getJSONArray("strImagUrl").getJSONObject(0).getString("imageUrl");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String itemName = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getString("strProductName");
                            String amount = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getString("intTotalAmount");
                            String itemsCount = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getString("intQuantity");
                            String blnCheck = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getString("blnCheck");

                            String itemsUnit = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getString("strBarcode") /*+ " " + jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getString("strUnit")*/;
                            frozen_food = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getBoolean("blnFrozenFood");

                            if (jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).has("strStockAvailability")) {

                                stock = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getString("strStockAvailability");

                                if (stock.equals("C")) {
                                    stock = "Critical Stock : " + String.format("%.0f", Double.parseDouble(jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getString("intStockCount")));
                                }
                            }
                            String barcode = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getString("strBarcode");

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
                            mTextView4.setVisibility(GONE);
                        } else {
                            mTextView4.setVisibility(VISIBLE);
                        }

                        String invoice_number = jsonArray.getJSONObject(0).getString("strInvoiceNumber");
                        edtInvoice.setText(invoice_number);

                        if (invoice_number.length() > 0) {
                            mAddInvoiceButton.setBackgroundColor(Color.TRANSPARENT);
                            mAddInvoiceButton.setImageDrawable(ContextCompat.getDrawable(DispatchedActivity.this, R.drawable.ic_check));
                            edtInvoice.setText(edtInvoice.getText());
                            mAddInvoiceButton.setEnabled(false);
                        }

                        String total_amount_top = jsonArray.getJSONObject(0).getString("intGrandTotal");
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
                            String userPhone = jsonArray.getJSONObject(0).getJSONArray("arrayUser").getJSONObject(0).getString("strPhone");
                            Delivery_Address += "\n" + userPhone;
                        }

                        dinominationPrice = jsonArray.getJSONObject(0).optString("intCurrencyDenomination");
                        if (!dinominationPrice.isEmpty()) {
                            dinominationLayout.setVisibility(VISIBLE);
                        } else {
                            dinominationLayout.setVisibility(GONE);
                        }
                        txtDinomination.setText(dinominationPrice);

                        if (jsonArray.getJSONObject(0).has("objExtraAmount") && jsonArray.getJSONObject(0).getJSONObject("objExtraAmount").length() > 0) {
                            priceVarioationLayout.setVisibility(VISIBLE);
                            price_variation = jsonArray.getJSONObject(0).getJSONObject("objExtraAmount").optDouble("intAmount");
                        } else {
                            priceVarioationLayout.setVisibility(GONE);
                        }
                        txtPriceVariation.setText("AED " + String.format("%.2f", Double.parseDouble(String.valueOf(price_variation))));


                        String payment_type = jsonArray.getJSONObject(0).getString("strPaymentMode");
                        String sub_total = jsonArray.getJSONObject(0).getString("intSubTotal");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String rewardDiscount = jsonArray.getJSONObject(0).optString("intRewardDiscount");
                        txtRewardDiscount.setText("AED " + String.format("%.2f", Double.parseDouble(String.valueOf(rewardDiscount))));


                        String delivery_charges = jsonArray.getJSONObject(0).getString("intDeliveryCharge");
                        String discounts = "0.00";
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String total_amount_bottom = jsonArray.getJSONObject(0).getString("intGrandTotal");
                        String total_no_Items = jsonArray.getJSONObject(0).getString("intTotalItemQuantity");

                        mTxtDeliveryAddress.setText(Delivery_Address);
                        mTxtDeliveryCharge.setText("AED " + String.format("%.2f", Double.parseDouble(delivery_charges)));

                        mTxtSubTotal.setText("AED " + String.format("%.2f", Double.parseDouble(sub_total)));

                        mTxtPaymentType.setText(payment_type);

                        mTxtPriceTop.setText(String.format("%.2f", Double.parseDouble(total_amount_top)));
                        mTxtTotalPrice.setText("AED " + String.format("%.2f", Double.parseDouble(total_amount_bottom)));

                        mTxtDiscount.setText("AED " + String.format("%.2f", Double.parseDouble(discounts)));
                        mTxtDayOfDate.setText(total_no_Items);


                        if (jsonArray.getJSONObject(0).has("dateDeliverOrReturnTimeStamp")) {
                            if (jsonArray.getJSONObject(0).getString("dateDeliverOrReturnTimeStamp") != null || !jsonArray.getJSONObject(0).getString("dateDeliverOrReturnTimeStamp").equals("null")) {
                                Date_Button = (jsonArray.getJSONObject(0).getString("dateDeliverOrReturnTimeStamp"));

                                Log.d("Time", Date_Button);

                                if (Date_Button.length() == 0 || (Date_Button == null || Date_Button.equals("null") || Date_Button.equals(""))) {
                                    Date_Button = getIntent().getStringExtra("DATE_KEY");
                                } else {
                                    Date_Button = getNeDate(jsonArray.getJSONObject(0).getString("dateDeliverOrReturnTimeStamp"));
                                    if (status == 4) {
                                        mainButton.setBackgroundColor(getResources().getColor(R.color.green));
                                        mainButton.setText("Delivered on " + Date_Button);
                                        mIcDeliveryStar.setImageDrawable(ContextCompat.getDrawable(DispatchedActivity.this, R.drawable.ic_star_green));
                                        mTxtDelivered.setTextColor(getResources().getColor(R.color.green));
                                        mIcDeliveryStar.setImageResource(R.drawable.ic_star_green);
                                        mIcDeliveryStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_green, getApplicationContext().getTheme()));
                                    } else if (status == 5) {
                                        mainButton.setBackgroundColor(getResources().getColor(R.color.red));
                                        mainButton.setText("Returned on " + Date_Button);
                                        mIcDeliveryStar.setImageDrawable(ContextCompat.getDrawable(DispatchedActivity.this, R.drawable.ic_star_red));
                                        mTxtDelivered.setTextColor(getResources().getColor(R.color.red));
                                        mTxtDelivered.setText("Returned");
                                        mIcDeliveryStar.setImageResource(R.drawable.ic_star_red);
                                        mIcDeliveryStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_red, getApplicationContext().getTheme()));
                                    } else if (status == 6) {
                                        mainButton.setBackgroundColor(getResources().getColor(R.color.cancelled));
                                        mainButton.setText("Cancelled on " + Date_Button);
                                        mIcDeliveryStar.setImageDrawable(ContextCompat.getDrawable(DispatchedActivity.this, R.drawable.ic_star_cancelled));
                                        mTxtDelivered.setTextColor(getResources().getColor(R.color.cancelled));
                                        mTxtDelivered.setText("Cancelled");
                                        mIcDeliveryStar.setImageResource(R.drawable.ic_star_cancelled);
                                        mIcDeliveryStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_cancelled, getApplicationContext().getTheme()));
                                    } else {
                                        mainButton.setBackgroundColor(getResources().getColor(R.color.green));
                                        mainButton.setText("Dispatched on " + Date_Button);
                                    }
                                }
                            }
                        }


                        String timeSlot = "", deliveryType = "";
                        if (jsonArray.getJSONObject(0).has("arrayTimeSlot")) {
                            timeSlot = getSlotDate(jsonArray.getJSONObject(0).getJSONArray("arrayTimeSlot").getJSONObject(0).optString("dateSlot")) + " " + jsonArray.getJSONObject(0).getJSONArray("arrayTimeSlot").getJSONObject(0).optString("strDisplayName");

                            mTimeSlotSelected.setText(timeSlot);

                            String timeCreated = getCreatedDate(jsonArray.getJSONObject(0).getString("dateCreateDateAndTime"));
                            mTimeOrderPlaced.setText(timeCreated);

                            String specialNote = jsonArray.getJSONObject(0).getString("strSpecialInst");

                            if (specialNote.length() > 0) {
                                mTxtSpecialNote.setText(specialNote);
                                mLayoutSpecialNote.setVisibility(VISIBLE);
                            } else {
                                mLayoutSpecialNote.setVisibility(GONE);
                            }

                            if (jsonArray.getJSONObject(0).getJSONArray("arrayTimeSlot").getJSONObject(0).has("strDeliveryType")) {
                                deliveryType = jsonArray.getJSONObject(0).getJSONArray("arrayTimeSlot").getJSONObject(0).getString("strDeliveryType");

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

                        initRecycler();

                    } else {
                        mLoadingLayout.setVisibility(GONE);
                        mOrderItemsRecycler.setVisibility(GONE);
                        if (response.getString("message") == " No Data Found") { // user has made no orders

                            mErrorLayout.setVisibility(VISIBLE);
                            mTxtErrorMessage.setText(response.getString("message"));

                        } else {

                            mErrorLayout.setVisibility(VISIBLE);
                            mTxtErrorMessage.setText(response.getString("message"));

                           /* MyDialogSheet dialogSheet = new MyDialogSheet(DispatchedActivity.this);
                            dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                    .setMessage(response.getString("message"))
                                    .setPositiveButton(getString(R.string.dialog_button_retry),
                                            new DialogSheet.OnPositiveClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    addData();
                                                }
                                            })
                                    .setBackgroundColor(ContextCompat.getColor(DispatchedActivity.this, R.color.white))
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
                Utils.checkVolleyError(DispatchedActivity.this, error);
                MyDialogSheet dialogSheet = new MyDialogSheet(DispatchedActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(DispatchedActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addData();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(DispatchedActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(DispatchedActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(clearNotifRequest);
    }

    private void initToolbar() {
        titleToolbar.setText("Order Dispatched");
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        txtPriceVariation = findViewById(R.id.txt_price_variation);
        priceVarioationLayout = findViewById(R.id.priceVariationLayout);
        txtDinomination = findViewById(R.id.txt_denomination);
        dinominationLayout = findViewById(R.id.dinominationLayout);

        txtRewardDiscount = findViewById(R.id.txt_reward_discount);

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
        mUserInfo = findViewById(R.id.userInfo);
        textView4 = findViewById(R.id.textView4);
        orderItemsRecycler = findViewById(R.id.orderItemsRecycler);
        txtDeliveryAddress = findViewById(R.id.txt_delivery_address);
        layoutAddress = findViewById(R.id.layout_address);
        imgPaymentType = findViewById(R.id.img_payment_type);
        txtPaymentType = findViewById(R.id.txt_payment_type);
        layoutPaymentType = findViewById(R.id.layout_payment_type);
        txtSubTotal = findViewById(R.id.txt_sub_total);
        txtDeliveryCharge = findViewById(R.id.txt_delivery_charge);
        txtDiscount = findViewById(R.id.txtDiscount);
        txtTotalPrice = findViewById(R.id.txt_total_price);
        layoutPaymentInfo = findViewById(R.id.layout_payment_info);
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
        mEdtInvoice = findViewById(R.id.edtInvoice);
        mAddInvoiceButton = findViewById(R.id.addInvoiceButton);
        mTxtSubTotal = findViewById(R.id.txt_sub_total);
        mTxtDeliveryCharge = findViewById(R.id.txt_delivery_charge);
        mTxtDiscount = findViewById(R.id.txtDiscount);
        mTxtTotalPrice = findViewById(R.id.txt_total_price);
        mLayoutPaymentInfo = findViewById(R.id.layout_payment_info);
        mTxtDeliveryAddress = findViewById(R.id.txt_delivery_address);
        mLayoutAddress = findViewById(R.id.layout_address);
        mImgPaymentType = findViewById(R.id.img_payment_type);
        mTxtPaymentType = findViewById(R.id.txt_payment_type);
        mLayoutPaymentType = findViewById(R.id.layout_payment_type);
        mLayoutData = findViewById(R.id.layoutData);
        //     mMainButton = findViewById(R.id.mainButton);
        mBottomButtons = findViewById(R.id.bottomButtons);
        mImgError = findViewById(R.id.img_error);
        mTxtErrorTitle = findViewById(R.id.txt_error_title);
        mTxtErrorMessage = findViewById(R.id.txt_error_message);
        mBtnRetry = findViewById(R.id.btn_retry);
        mErrorLayout = findViewById(R.id.error_layout);
        mErrorParentLayout = findViewById(R.id.error_parent_layout);
        mLoadingProgress = findViewById(R.id.loadingProgress);
        mLoadingLayout = findViewById(R.id.loadingLayout);
    }

    public void onClickOrderedLayout(View view) {
        if (mUserInfo.getVisibility() == VISIBLE) {
            mUserInfo.setVisibility(GONE);
        } else {
            mUserInfo.setText("Ordered by Aeth Analytica on 14 Apr 20, 10:10AM");
            mUserInfo.setVisibility(VISIBLE);
        }
    }

    public void onClickPackingLayout(View view) {
        if (mUserInfo.getVisibility() == VISIBLE) {
            mUserInfo.setVisibility(GONE);
        } else {
            mUserInfo.setText("Accepted by Aeth Analytica on 14 Apr 20, 10:10AM");
            mUserInfo.setVisibility(VISIBLE);
        }
    }

    public void onClickReadyToDispatchLayout(View view) {
        if (mUserInfo.getVisibility() == VISIBLE) {
            mUserInfo.setVisibility(GONE);
        } else {
            mUserInfo.setText("Packed by Aeth Analytica on 14 Apr 20, 10:10AM");
            mUserInfo.setVisibility(VISIBLE);
        }
    }

    public void onClickDispatched(View view) {
        if (mUserInfo.getVisibility() == VISIBLE) {
            mUserInfo.setVisibility(GONE);
        } else {
            mUserInfo.setText("Item Collected by Aeth Analytica on 14 Apr 20, 10:10AM");
            mUserInfo.setVisibility(VISIBLE);
        }
    }

    public void onClickAddInvoiceNumber(final View view) {
        if (edtInvoice.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter a valid Invoice number", Toast.LENGTH_LONG).show();
        } else {

            callSaveInvoiceNumber();

            /*final P07FancyAlert alert = new P07FancyAlert(DispatchedActivity.this);
            alert.setMessage("Invoice number has been noted");
            alert.setButton("Continue", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    ImageView button = (ImageView) view;
                    button.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check));
                    edtInvoice.setText(edtInvoice.getText());
                    edtInvoice.setEnabled(false);
                    view.setEnabled(false);

                }
            });
            alert.setGif(R.raw.animation_success);
            alert.show();*/
        }
    }

    private void callSaveInvoiceNumber() {


    }

    public String getNeDate(String notificationDate) {
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
