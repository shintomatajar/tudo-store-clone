package com.tudomart.store.ui.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
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

import com.Comman;
import com.PdfDocumentAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.marcoscg.dialogsheet.DialogSheet;
import com.tudomart.store.R;
import com.tudomart.store.adapters.orders.orderDetails.AdapterOrderDetailsItemsPacking;
import com.tudomart.store.api.OrderManagementAPI;
import com.tudomart.store.helpers.EditPriceAlert;
import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.helpers.network.RequestController;
import com.tudomart.store.helpers.network.VolleyErrorHandler;
import com.tudomart.store.helpers.sharedPref.UserSessionManager;
import com.tudomart.store.model.order.orderDetails.ModelOrderItemsPacking;
import com.tudomart.store.model.order.orderDetails.ModelOrderPriceDetails;
import com.tudomart.store.ui.activities.dash.DashboardActivity;
import com.tudomart.store.ui.activities.ordersubstitute.SearchProductsActivity;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.ui.customViews.P07FancyAlert;
import com.tudomart.store.utils.ResponseCallback;
import com.tudomart.store.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class PackingActivity extends BaseActivity {
    private Button notifyButton;
    private ImageView mBackIcon;
    private TextView mTitleToolbar;
    private TextView mTxtDayOfDate;
    private TextView mTxtMonthOfDate;
    private TextView mTxtOrderIdTop;
    private TextView itemLeftCount;
    private LinearLayout mHeader;
    private ImageView mIcOrderStar;
    private TextView mTxtOrdered;
    private LinearLayout mOrderedStar;
    private ImageView mIcDeliveryStar;
    private TextView mTxtDelivered;
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
    private TextView mTxtTotalPrice;
    private CardView mLayoutPaymentInfo;
    private NestedScrollView mLayoutData;
    private Button mMainButton;
    private LinearLayout mBottomButtons;
    private ImageView mImgError;
    private TextView mTxtErrorTitle;
    private TextView mTxtErrorMessage;
    private TextView txtItemsLeft;
    private Button mBtnRetry;
    private RelativeLayout mErrorLayout;
    private LinearLayout mErrorParentLayout;
    private ProgressBar mLoadingProgress;
    private RelativeLayout mLoadingLayout;
    private EditText edtInvoice;
    private ImageView addInvoiceButton;
    private TextView mUserInfo;
    private TextView mEdtInvoice;
    private ImageView mAddInvoiceButton;

    private TextView txtPriceVariation;
    private LinearLayout priceVarioationLayout;
    private TextView txtDinomination;
    private CardView dinominationLayout;
    private TextView txtRewardPoints;
    private TextInputEditText mTxtCardAmount;
    private TextInputEditText mTxtCashAmount;
    private String substituteIcon;

    private TextView bTnPrint;

    private double price_variation;
    private double dinominationPrice;
    private Boolean pickUpStoreBoolean = false;

    private int SKIP_COUNT = 0;
    private int PAGE_LIMIT = 10;

    String order_id = "";
    String trip_number = "";
    Boolean frozen_food = false, hide_frozen = true;

    TextView userInfo;
    private ArrayList<ModelOrderItemsPacking> list = new ArrayList<>();


    private TextView mTxtPriceTop;
    private TextView mTxtItemsLeft;

    private TextView mTimeOrderPlaced;
    private TextView mTimeSlotSelected;
    private TextView mTxtSpecialNote;
    private CardView mLayoutSpecialNote;
    private CardView mSlotSelectedLabel;
    private TextView mTxtDeliveryType;
    private CardView mLayoutDeliveryType;
    private LinearLayout lay_price_variation;
    private Button btnEditPrice;
    private TextView txt_price_variation;
    String stock = "";
    ModelOrderPriceDetails priceDetails;
    DashboardIntent dashboardIntent;

    private AdapterOrderDetailsItemsPacking adapter;
    ArrayList<String> outOfStockItems;

    private boolean onceNotifyMe = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing);

        Intent intent = getIntent();
        order_id = intent.getStringExtra("ORDER_ID");

        initViews();
        boolean notify = false;
        mTxtOrderIdTop.setText(order_id);

        //  mAddInvoiceButton.setImageDrawable(ContextCompat.getDrawable(PackingActivity.this, R.drawable.ic_check));
        initInvoiceText();

//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        bTnPrint.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                createPDFFile(Comman.getAppPath(PackingActivity.this) + "matajar.pdf");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//
//                    }
//                })
//                .check();

        // Notify Me button
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("strOrderId", priceDetails.getData().get(0).getStrOrderID());
                    jsonObj.put("arrayStore", Utils.getStoreJson(new UserSessionManager(PackingActivity.this).getShopIdArray()/* new UserSessionManager(requireActivity()).getUserId()*/));
                    //  jsonObj.put("arrayStore", new UserSessionManager(PackingActivity.this).getShopIdArray());
                    jsonObj.put("strLoginUserId", new UserSessionManager(PackingActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
                    //          jsonObj.put("arrayStore", new UserSessionManager(PackingActivity.this).getShopIdArray());
                    //          jsonObj.put("strLoginUserId", new UserSessionManager(PackingActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
                new OrderManagementAPI(PackingActivity.this).newPushApiCall(jsonObj, new ResponseCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Toast.makeText(PackingActivity.this, "Push has been send..!", Toast.LENGTH_SHORT).show();
                        notifyButton.setBackgroundResource(R.color.red);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(PackingActivity.this, error.toString() + "  push not send..!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        edtInvoice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                // If the event is a key-down event on the "enter" button
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onClickAddInvoiceNumber(view);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        addData();
    }

    private void selectRecommendedProducts(String productId, String fkSubCategoryId) {
        startActivityForResult(
                new Intent(this, SearchProductsActivity.class)
                        .putExtra("products", productId)
                        .putExtra("strSubCategoryId", fkSubCategoryId)
                        .putExtra("order_id", order_id), 1998);
    }

    private void addData() {
       /* for (int i = 0; i < 2; i++) {
            list.add(new ModelOrderItemsPacking(DummyData.drinks[0], "3", "30", "Product Name", false, false));
            list.add(new ModelOrderItemsPacking(DummyData.drinks[0], "3", "30", "Product Name", true, false));
        }*/

        mLoadingLayout.setVisibility(VISIBLE);

        RequestQueue rQueue = Volley.newRequestQueue(PackingActivity.this);
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("strLoginUserId", new UserSessionManager(PackingActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strStoreId", new UserSessionManager(PackingActivity.this).getUserDetails().get("shop")/* new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strOrderID", order_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d("RequestBody", requestBody.toString());

        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.ALL_ORDERS_DETAILS_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                mLoadingLayout.setVisibility(GONE);
                try {
                    if (response.getBoolean("success")) {

                        //mMainButton.setEnabled(true);
                        mMainButton.setEnabled(true);
                        //Log.d("RequestBody", response.toString());
                        list.clear();
                        mOrderItemsRecycler.setVisibility(VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").length(); i++) {

                            String imageUrl = "";
                            try {
                                // JSONObject imgObject = (JSONObject) jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getJSONArray("strImagUrl").get(0);

                                imageUrl = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getJSONArray("strImagUrl").getJSONObject(0).optString("imageUrl");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String itemName = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("strProductName");
                            String amount = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("intTotalAmount");
                            String itemsCount = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("intQuantity");
                            String blnCheck = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("blnCheck");
                            String fkSubCategoryId = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optJSONObject("fkSubCategoryId").optString("fkSubCategoryId");
                            String productId = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("strItemId");
                            String itemsUnit = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("strBarcode") /*+ " " + jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("strUnit")*/;
                            Boolean blnSubstitute = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optBoolean("blnSubstitute", false) /*+ " " + jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("strUnit")*/;
                            frozen_food = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).getBoolean("blnFrozenFood");

                            if (jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).has("strStockAvailability")) {

                                stock = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("strStockAvailability");

                                if (stock.equals("C")) {
                                    stock = "Critical Stock : " + String.format("%.0f", Double.parseDouble(jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("intStockCount")));
                                }
                            }
                            String substituteIcon = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("fkCartId");

//                            Boolean substitutePop = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).has("blnSubstitute");
//                            if (substituteIcon.equals("substitution")) {
//                                setSubstitutePopup(itemName);
//                            } else {
//
//                            }

                            String barcode = jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").getJSONObject(i).optString("strBarcode");
//                            for (int i = 0; i < jsonArray.getJSONObject(0).getJSONArray("arrayProductDetails").length();i++){

                            list.add(new ModelOrderItemsPacking(imageUrl, itemsCount, amount, itemName, itemsUnit, frozen_food, false, stock, barcode, blnCheck, productId, order_id, fkSubCategoryId, blnSubstitute, substituteIcon));

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

                        String invoice_number = jsonArray.getJSONObject(0).optString("strInvoiceNumber");
                        edtInvoice.setText(invoice_number);

                        if (invoice_number.length() > 0) {
                            mAddInvoiceButton.setBackgroundColor(Color.TRANSPARENT);
                            mAddInvoiceButton.setImageDrawable(ContextCompat.getDrawable(PackingActivity.this, R.drawable.ic_check));
                            edtInvoice.setText(edtInvoice.getText());
                            mAddInvoiceButton.setEnabled(false);
                            //  edtInvoice.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);
                        } else {
                            mAddInvoiceButton.setImageDrawable(ContextCompat.getDrawable(PackingActivity.this, R.drawable.ic_add));
                        }


                        String total_amount_top = jsonArray.getJSONObject(0).optString("intGrandTotal");

                        // SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        // txtPriceTop.setText(String.format("%.2f", Double.parseDouble(total_amount_top)));

                        String Delivery_Address = jsonArray.getJSONObject(0).getJSONArray("arrayAddress").getJSONObject(0).optString("strUserName")
                                + "\n" +
                                jsonArray.getJSONObject(0).getJSONArray("arrayAddress").getJSONObject(0).optString("strAddress") +
                                "\n" + jsonArray.getJSONObject(0).getJSONArray("arrayAddress").getJSONObject(0).optString("strLocation")
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

                        if (payment_type.equals("Collect from store")) pickUpStoreBoolean = true;


                        String sub_total = jsonArray.getJSONObject(0).optString("intSubTotal");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String delivery_charges = jsonArray.getJSONObject(0).optString("intDeliveryCharge");
                        String discounts =  jsonArray.getJSONObject(0).getString("intDiscount");;
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String total_amount_bottom = jsonArray.getJSONObject(0).optString("intGrandTotal");
                        String total_no_Items = jsonArray.getJSONObject(0).optString("intTotalItemQuantity");

                        dinominationPrice = jsonArray.getJSONObject(0).optDouble("intCurrencyDenomination");
                        if (dinominationPrice > 0) {
                            dinominationLayout.setVisibility(VISIBLE);
                        } else {
                            dinominationLayout.setVisibility(GONE);
                        }
                        txtDinomination.setText("AED " + String.format("%.2f", Double.parseDouble(String.valueOf(dinominationPrice))));

                        // Adding reward points
                        String rewardPoints = jsonArray.getJSONObject(0).optString("intRewardDiscount");
                        txtRewardPoints.setText("AED " + String.format("%.2f", Double.parseDouble(String.valueOf(rewardPoints))));


                        if (jsonArray.getJSONObject(0).has("objExtraAmount") && jsonArray.getJSONObject(0).getJSONObject("objExtraAmount").length() > 0) {
                            priceVarioationLayout.setVisibility(VISIBLE);
                            price_variation = jsonArray.getJSONObject(0).getJSONObject("objExtraAmount").optDouble("intAmount");
                        } else {
                            priceVarioationLayout.setVisibility(GONE);
                        }
                        //  txtPriceVariation.setText("AED " + String.format("%.2f", Double.parseDouble(String.valueOf(price_variation))));

                        mTxtDeliveryAddress.setText(Delivery_Address);
                        mTxtDeliveryCharge.setText("AED " + String.format("%.2f", Double.parseDouble(delivery_charges)));

                        mTxtSubTotal.setText("AED " + String.format("%.2f", Double.parseDouble(sub_total)));

                        mTxtPaymentType.setText(payment_type);

                        mTxtPriceTop.setText(String.format("%.2f", Double.parseDouble(total_amount_top)));
                        mTxtTotalPrice.setText("AED " + String.format("%.2f", Double.parseDouble(total_amount_bottom)));

                        mTxtDiscount.setText("AED " + String.format("%.2f", Double.parseDouble(discounts)));
                        mTxtDayOfDate.setText(total_no_Items);
                        itemLeftCount.setText("" + list.size());
                        mTxtDayOfDate.setText(list.size() + "");
                        itemLeftCount.setText("" + total_no_Items);

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

                        mOrderItemsRecycler.setVisibility(VISIBLE);
                        adapter = new AdapterOrderDetailsItemsPacking(list, PackingActivity.this, new AdapterOrderDetailsItemsPacking.AdapterEvents() {
                            @Override
                            public void onItemClick(ModelOrderItemsPacking dataItem, ArrayList<ModelOrderItemsPacking> data) {
                                int itemsleft = 0;
                                for (int i = 0; i < data.size(); i++) {
                                    if (!data.get(i).getItemSelecetd()) {
                                        itemsleft++;
                                    }
                                }
                                itemLeftCount.setText("" + itemsleft);
                                if (itemsleft == 0) {
                                    itemLeftCount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                                    txtItemsLeft.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                                    mMainButton.setEnabled(true);
                                } else {
                                    itemLeftCount.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                                    txtItemsLeft.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));

                                    // mMainButton.setEnabled(false);

                                }

                            }

                            @Override
                            public void onSubtituteClick(ModelOrderItemsPacking dataItem) {
                                Boolean isSubstituted = dataItem.getBlnSubstitute();
                                if (isSubstituted) {
                                    showSubstitutionDialog(dataItem);
                                } else {
                                    selectRecommendedProducts(dataItem.getProductId(), dataItem.getfkSubCategoryId());
                                }
                            }

                            @Override
                            public void deleteSubstiture(ModelOrderItemsPacking delteItem, int position) {
                                deleteSubstituteProducts(delteItem);
                            }
                        });
                        mOrderItemsRecycler.setAdapter(adapter);
                        priceDetails = new Gson().fromJson(response.toString(), ModelOrderPriceDetails.class);
                        JSONObject objExtraAmount = ((JSONObject) jsonArray.get(0)).getJSONObject("objExtraAmount"); //applied amount of price variation
                        if (objExtraAmount.has("intAmount")) {
                            double intAmount = objExtraAmount.getDouble("intAmount");
                            if (intAmount != 0) {
                                lay_price_variation.setVisibility(VISIBLE);
                                txt_price_variation.setText("AED " + Utils.toFixed(intAmount));
                                btnEditPrice.setVisibility(GONE);
                            }
                        }
                    } else {
                        mLoadingLayout.setVisibility(GONE);
                        mOrderItemsRecycler.setVisibility(GONE);
                        if (response.optString("message") == " No Data Found") { // user has made no orders
                            mErrorLayout.setVisibility(VISIBLE);
                            mTxtErrorMessage.setText(response.optString("message"));

                        } else {

                            mErrorLayout.setVisibility(VISIBLE);
                            mTxtErrorMessage.setText(response.optString("message"));

                            /*MyDialogSheet dialogSheet = new MyDialogSheet(PackingActivity.this);
                            dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                    .setMessage(response.optString("message"))
                                    .setPositiveButton(getString(R.string.dialog_button_retry),
                                            new DialogSheet.OnPositiveClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    addData();
                                                }
                                            })
                                    .setBackgroundColor(ContextCompat.getColor(PackingActivity.this, R.color.white))
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

                Utils.checkVolleyError(PackingActivity.this, error);
                MyDialogSheet dialogSheet = new MyDialogSheet(PackingActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(PackingActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addData();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(PackingActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(PackingActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };
        RequestController.getInstance().addToRequestQueue(clearNotifRequest);
    }

    void setSubstitutePopup(String itemName) {
        final P07FancyAlert alert = new P07FancyAlert(PackingActivity.this);
        //   alert.setMessage("Order has been accepted");
        alert.setMessage(itemName + "is substituted");
        alert.setButton("Continue", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
//                               /* startActivity(new Intent(getApplicationContext(), PackingOrdersActivity.class));
//                                PackingActivity.this.finish();*/
//                                Intent dashBoardIntent = new Intent(getApplicationContext(), DashboardFragment.class);
//                                startActivity(dashBoardIntent);
//                                finish();
                // startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                //  PackingActivity.this.finish();
            }
        });
        alert.setGif(R.raw.animation_success);
        alert.show();
    }

    void deleteSubstituteProducts(ModelOrderItemsPacking dataItem) {
        mLoadingLayout.setVisibility(VISIBLE);
        JSONObject reqBody = new JSONObject();
        try {
            reqBody.put("strProductId", dataItem.getProductId());
            reqBody.put("strOrderId", dataItem.getOrderId());
            reqBody.put("strLoginUSerId", new UserSessionManager(PackingActivity.this).getUserDetails().get("userid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest deleteSubstituteRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.DELETE_SUBSTITUTE_PRODUCT, reqBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mLoadingLayout.setVisibility(View.INVISIBLE);
                try {
                    if (response.getBoolean("success")) {
                        addData();
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PackingActivity.this, "Some error", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", new UserSessionManager(PackingActivity.this).getUserDetails().get("token"));
                return params;
            }
        };
        RequestController.getInstance().addToRequestQueue(deleteSubstituteRequest);
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

        } else if (Double.parseDouble(mTxtCashAmount.getText().toString()) + Double.parseDouble(mTxtCardAmount.getText().toString()) > Double.parseDouble(mTxtPriceTop.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Collected Amount Can't be greater than Order Amount..!", Toast.LENGTH_LONG).show();

        }
    }

    private void showSubstitutionDialog(ModelOrderItemsPacking dataItem) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Add or View");
        String[] items = {"View Substituted Items", "Add Products"};
        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        SubstitutedProductsListActivity.Companion.start(PackingActivity.this, dataItem.getOrderId(), dataItem.getProductId());
                        break;
                    case 1:
                        selectRecommendedProducts(dataItem.getProductId(), dataItem.getfkSubCategoryId());
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void createPDFFile(String path) {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        if (new File(path).exists())
            new File(path).delete();
        try {
            Document document = new Document();
            //Save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //open to write
            document.open();
            //Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Matajr");
            document.addCreator("Matajar E commerce");

            //Font Settings
            BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
            float fontSizeHeader = 20.0f;
            float fontSizeBody = 16.0f;
            float valueFontSize = 26.0f;

            //Custom font
            BaseFont fontName = BaseFont.createFont("res/font/roboto.ttf", "UTF-8", BaseFont.EMBEDDED);

            //create title of document
            Font titleFont = new Font(fontName, 20.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "Order Deatils", Element.ALIGN_CENTER, titleFont);

            // Add more
            Font orderNumberFont = new Font(fontName, fontSizeHeader, Font.NORMAL, colorAccent);
            addNewItem(document, "order number", Element.ALIGN_LEFT, orderNumberFont);

            Font orderNumberValueFont = new Font(fontName, valueFontSize, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "#525263", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeperator(document);

            addNewItem(document, "Order Date", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, date, Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeperator(document);

            addNewItem(document, "Account name", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, "Matajar Ecommerce", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeperator(document);

            //Add product order detail
            addLineSpace(document);
            addNewItem(document, "Product details", Element.ALIGN_CENTER, titleFont);

            addLineSeperator(document);

            //item 1
            addNewItemWithLeftAndRight(document, "Burger", "(1.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "20", "1200.0", titleFont, orderNumberValueFont);

            addLineSeperator(document);

            //item 2
            addNewItemWithLeftAndRight(document, "Pizza", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "12", "1520.0", titleFont, orderNumberValueFont);

            addLineSeperator(document);

            //item 3
            addNewItemWithLeftAndRight(document, "Sandwich", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "10", "1000.0", titleFont, orderNumberValueFont);

            addLineSeperator(document);

            //Total
            addLineSpace(document);
            addLineSpace(document);

            addNewItemWithLeftAndRight(document, "total", "8500", titleFont, orderNumberValueFont);

            document.close();
            Toast.makeText(this, "Sucess", Toast.LENGTH_SHORT).show();

            printPDF();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);

    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);

    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(PackingActivity.this, Comman.getAppPath(PackingActivity.this) + "matajarpdf.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        } catch (Exception ex) {
            Toast.makeText(PackingActivity.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }


    private void initViews() {
        bTnPrint = findViewById(R.id.btnPrint);
        mTxtCardAmount = findViewById(R.id.txt_card_amount);
        mTxtCashAmount = findViewById(R.id.txt_cash_amount);

        //
        lay_price_variation = findViewById(R.id.lay_price_variation);

        txtRewardPoints = findViewById(R.id.txt_reward_discount);

        txtDinomination = findViewById(R.id.txt_denomination);
        dinominationLayout = findViewById(R.id.dinominationLayout);

        priceVarioationLayout = findViewById(R.id.lay_price_variation);
        txt_price_variation = findViewById(R.id.txt_price_variation);

        btnEditPrice = findViewById(R.id.btnEditPrice);
        mTxtDeliveryType = findViewById(R.id.txt_delivery_type);
        mLayoutDeliveryType = findViewById(R.id.layout_delivery_type);

        mSlotSelectedLabel = findViewById(R.id.slot_selected_label);
        mTimeOrderPlaced = findViewById(R.id.time_order_placed);
        mTimeSlotSelected = findViewById(R.id.time_slot_selected);
        mTxtSpecialNote = findViewById(R.id.txt_special_note);
        mLayoutSpecialNote = findViewById(R.id.layout_special_note);

        edtInvoice = findViewById(R.id.edtInvoice);
        mBackIcon = findViewById(R.id.back_icon);
        mTitleToolbar = findViewById(R.id.title_toolbar);
        mTxtDayOfDate = findViewById(R.id.txt_day_of_date);
        mTxtMonthOfDate = findViewById(R.id.txt_month_of_date);
        mTxtOrderIdTop = findViewById(R.id.txt_order_id_top);
        itemLeftCount = findViewById(R.id.txt_price_top);
        mHeader = findViewById(R.id.header);
        mIcOrderStar = findViewById(R.id.ic_order_star);
        mTxtOrdered = findViewById(R.id.txt_ordered);
        mOrderedStar = findViewById(R.id.orderedStar);
        mIcDeliveryStar = findViewById(R.id.ic_delivery_star);
        mTxtDelivered = findViewById(R.id.txt_delivered);
        mTextView4 = findViewById(R.id.textView4);
        mOrderItemsRecycler = findViewById(R.id.orderItemsRecycler);
        mTxtDeliveryAddress = findViewById(R.id.txt_delivery_address);
        mLayoutAddress = findViewById(R.id.layout_address);
        mImgPaymentType = findViewById(R.id.img_payment_type);
        mTxtPaymentType = findViewById(R.id.txt_payment_type);
        mLayoutPaymentType = findViewById(R.id.layout_payment_type);
        mTxtSubTotal = findViewById(R.id.txt_sub_total);
        mTxtDeliveryCharge = findViewById(R.id.txt_delivery_charge);
        mTxtTotalPrice = findViewById(R.id.txt_total_price);
        mLayoutPaymentInfo = findViewById(R.id.layout_payment_info);
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
        userInfo = findViewById(R.id.userInfo);
        txtItemsLeft = findViewById(R.id.txtItemsLeft);
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTitleToolbar.setText("Order Packing");

        mBackIcon = findViewById(R.id.back_icon);
        mTitleToolbar = findViewById(R.id.title_toolbar);
        mTxtDayOfDate = findViewById(R.id.txt_day_of_date);
        mTxtMonthOfDate = findViewById(R.id.txt_month_of_date);
        mTxtPriceTop = findViewById(R.id.txt_price_top);
        mTxtItemsLeft = findViewById(R.id.txtItemsLeft);
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
        notifyButton = findViewById(R.id.btnNotifyUser);
    }

    public void onClickPacking(View view) {
        if (userInfo.getVisibility() == VISIBLE) {
            userInfo.setVisibility(GONE);
        } else {
            userInfo.setText("Order placed by aeth Analytica on 19 Apr 20, 10:10PM");
            userInfo.setVisibility(VISIBLE);
        }
    }

    public void onClickOrderStart(View view) {
        if (userInfo.getVisibility() == VISIBLE) {
            userInfo.setVisibility(GONE);
        } else {
            userInfo.setText("Order placed by aeth Analytica on 19 Apr 20, 10:10PM");
            userInfo.setVisibility(VISIBLE);
        }
    }

    public void onClickReadyToDispatch(View view) {
        if (adapter.readyDispatch == list.size()) {

        }
        MyDialogSheet dialog = new MyDialogSheet(PackingActivity.this);
        dialog.setTitle("Change Status");
        dialog.setMessage("Are you sure to update the status");
        dialog.setPositiveButton("Yes", new DialogSheet.OnPositiveClickListener() {
            @Override
            public void onClick(View v) {
                // mMainButton.setText("Delivered");
                if (edtInvoice.getText().toString().length() > 0) {
                    mMainButton.setEnabled(true);
                    callDispatchOrder();

                } else {
                    Toast.makeText(getApplicationContext(), "Enter Invoice Number !", Toast.LENGTH_LONG).show();
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
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    private void callTheStoreApiDispatch() {

        callSaveInvoiceNumber_second();
        paidAmount();

        final ProgressDialog loginProgress = new ProgressDialog(PackingActivity.this);
        loginProgress.setMessage("Please Wait...");
        loginProgress.setCancelable(false);
        loginProgress.show();

        mLoadingLayout.setVisibility(VISIBLE);

        JSONObject requestBody = new JSONObject();

        try {

            requestBody.put("strOrderId", order_id);
            //  requestBody.put("strTripNumber", trip_number);
            requestBody.put("strLoginUserId", new UserSessionManager(PackingActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strCashAmount", mTxtCashAmount.getText().toString());
            requestBody.put("strCardAmount", mTxtCardAmount.getText().toString());
            requestBody.put("strTotalCollectedAmount", String.format("%.2f", Double.parseDouble(mTxtCashAmount.getText().toString()) + Double.parseDouble(mTxtCardAmount.getText().toString())));
            requestBody.put("strDeviationAmount", String.format("%.2f", Double.parseDouble(mTxtPriceTop.getText().toString()) - (Double.parseDouble(mTxtCashAmount.getText().toString()) + Double.parseDouble(mTxtCardAmount.getText().toString()))));
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
                        final P07FancyAlert alert = new P07FancyAlert(PackingActivity.this);
                        alert.setMessage("Pickup store order has been accepted");
                        alert.setButton("Continue", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alert.dismiss();
                                mMainButton.setText("Store Dispatched");
                                mMainButton.setEnabled(false);
                                mMainButton.setBackgroundColor(getResources().getColor(R.color.green));
                               /* startActivity(new Intent(getApplicationContext(), PackingOrdersActivity.class));
                                PackingActivity.this.finish();*/

                            }
                        });
                        alert.setGif(R.raw.animation_success);
                        alert.show();
                    } else {
                        MyDialogSheet dialogSheet = new MyDialogSheet(PackingActivity.this);
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
                Utils.checkVolleyError(PackingActivity.this, error);
                MyDialogSheet dialogSheet = new MyDialogSheet(PackingActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(PackingActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callTheStoreApiDispatch();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(PackingActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(PackingActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };
        RequestController.getInstance().addToRequestQueue(allStoreDispachRequest);
    }

    private void callSaveInvoiceNumber() {

        mLoadingLayout.setVisibility(VISIBLE);
        edtInvoice.setEnabled(false);


        RequestQueue rQueue = Volley.newRequestQueue(PackingActivity.this);
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("strLoginUserId", new UserSessionManager(PackingActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strStoreId", new UserSessionManager(PackingActivity.this).getUserDetails().get("shop"));
            requestBody.put("strOrderId", order_id);
            requestBody.put("strInvoiceNumber", edtInvoice.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d("RequestBody", requestBody.toString());

        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.SAVE_INVOICE_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                mLoadingLayout.setVisibility(GONE);
                try {
                    if (response.getBoolean("success")) {

                        //Log.d("Response Body", response.toString());

                        // invoice has been added
                        mAddInvoiceButton.setBackgroundColor(Color.TRANSPARENT);
                        mAddInvoiceButton.setImageDrawable(ContextCompat.getDrawable(PackingActivity.this, R.drawable.ic_check));
                        edtInvoice.setText(edtInvoice.getText());
                        mAddInvoiceButton.setEnabled(false);

//                        final P07FancyAlert alert = new P07FancyAlert(PackingActivity.this);
//                        alert.setMessage("Invoice number has been noted");
//                        alert.setButton("Continue", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                alert.dismiss();
//                                  mAddInvoiceButton.setBackgroundColor(Color.TRANSPARENT);
//                                mAddInvoiceButton.setImageDrawable(ContextCompat.getDrawable(PackingActivity.this, R.drawable.ic_check));
//                                edtInvoice.setText(edtInvoice.getText());
//                                mAddInvoiceButton.setEnabled(false);
//                            }
//                        });
//                        alert.setGif(R.raw.animation_success);
//                        alert.show();


                    } else {
                        mErrorLayout.setVisibility(GONE);

                        if (response.optString("message") == " No Data Found") { // user has made no orders

                            //errorLayout.setVisibility(VISIBLE);

                        } else {

                            // errorLayout.setVisibility(View.VISIBLE);
                            MyDialogSheet dialogSheet = new MyDialogSheet(PackingActivity.this);
                            dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                    .setMessage(response.optString("message"))
                                    .setPositiveButton(getString(R.string.dialog_button_retry),
                                            new DialogSheet.OnPositiveClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    callSaveInvoiceNumber();
                                                }
                                            })
                                    .setBackgroundColor(ContextCompat.getColor(PackingActivity.this, R.color.white))
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

                mLoadingLayout.setVisibility(GONE);
                Utils.checkVolleyError(PackingActivity.this, error);
                MyDialogSheet dialogSheet = new MyDialogSheet(PackingActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(PackingActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callSaveInvoiceNumber();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(PackingActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(PackingActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(clearNotifRequest);

    }


    private void callDispatchOrder() {

        // mLoadingLayout.setVisibility(VISIBLE);

        callSaveInvoiceNumber_second();

        final ProgressDialog loginProgress = new ProgressDialog(PackingActivity.this);
        loginProgress.setMessage("Please Wait...");
        loginProgress.setCancelable(false);
        loginProgress.show();

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("strLoginUserId", new UserSessionManager(PackingActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strOrderId", order_id/* new UserSessionManager(requireActivity()).getUserId()*/);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d("Request", requestBody.toString());
        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.MY_ORDERS_DISPATCH_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                // mLoadingLayout.setVisibility(GONE);
                loginProgress.dismiss();
                try {
                    if (response.getBoolean("success")) {
                        //Log.d("Response", response.toString());

                        // JSONArray jsonArray = response.getJSONArray("data");

                        final P07FancyAlert alert = new P07FancyAlert(PackingActivity.this);
                        //   alert.setMessage("Order has been accepted");
                        alert.setMessage("Order has been dispatched");
                        alert.setButton("Continue", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alert.dismiss();
                                mMainButton.setText("Dispatched");
                                mMainButton.setEnabled(false);
                                mMainButton.setBackgroundColor(getResources().getColor(R.color.green));
//                               /* startActivity(new Intent(getApplicationContext(), PackingOrdersActivity.class));
//                                PackingActivity.this.finish();*/
//                                Intent dashBoardIntent = new Intent(getApplicationContext(), DashboardFragment.class);
//                                startActivity(dashBoardIntent);
//                                finish();
                                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                PackingActivity.this.finish();
                            }
                        });
                        alert.setGif(R.raw.animation_success);
                        alert.show();


                    } else {
                        MyDialogSheet dialogSheet = new MyDialogSheet(PackingActivity.this);
                        dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                .setMessage(response.optString("message"))
                                .setPositiveButton(getString(R.string.dialog_button_retry),
                                        new DialogSheet.OnPositiveClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                callDispatchOrder();
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
                Utils.checkVolleyError(PackingActivity.this, error);
                MyDialogSheet dialogSheet = new MyDialogSheet(PackingActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(PackingActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callDispatchOrder();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(PackingActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(PackingActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(clearNotifRequest);
    }

    public void onClickAddInvoiceNumber(final View view) {
        if (edtInvoice.getText().toString().equals("") && edtInvoice.getText().length() > 5) {
            Toast.makeText(getApplicationContext(), "Enter a valid Invoice number", Toast.LENGTH_LONG).show();
        } else {

            callSaveInvoiceNumber();

           /* final P07FancyAlert alert = new P07FancyAlert(PackingActivity.this);
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

    private void initInvoiceText() {
        mAddInvoiceButton.setImageDrawable(ContextCompat.getDrawable(PackingActivity.this, R.drawable.ic_check));
        edtInvoice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //mAddInvoiceButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check));
                mAddInvoiceButton.setEnabled(true);

            }
        });
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

    private void callSaveInvoiceNumber_second() {

        mLoadingLayout.setVisibility(VISIBLE);

        RequestQueue rQueue = Volley.newRequestQueue(PackingActivity.this);
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("strLoginUserId", new UserSessionManager(PackingActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strStoreId", new UserSessionManager(PackingActivity.this).getUserDetails().get("shop"));
            requestBody.put("strOrderId", order_id);
            requestBody.put("strInvoiceNumber", edtInvoice.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("RequestBody", requestBody.toString());

        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.SAVE_INVOICE_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                mLoadingLayout.setVisibility(GONE);
                try {
                    if (response.getBoolean("success")) {

                        Log.d("Response Body", response.toString());

                        /*final P07FancyAlert alert = new P07FancyAlert(BillMyOrderActivity.this);
                        alert.setMessage("Invoice number has been noted");
                        alert.setButton("Continue", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();



                            }
                        });
                        alert.setGif(R.raw.animation_success);
                        alert.show();*/

                        mAddInvoiceButton.setBackgroundColor(Color.TRANSPARENT);
                        mAddInvoiceButton.setImageDrawable(ContextCompat.getDrawable(PackingActivity.this, R.drawable.ic_check));
                        edtInvoice.setText(edtInvoice.getText());
                        edtInvoice.setEnabled(false);
                        mAddInvoiceButton.setEnabled(false);


                    } else {
                        mErrorLayout.setVisibility(GONE);

                        if (response.optString("message") == " No Data Found") { // user has made no orders

                            //errorLayout.setVisibility(VISIBLE);

                        } else {

                            // errorLayout.setVisibility(View.VISIBLE);
                            MyDialogSheet dialogSheet = new MyDialogSheet(PackingActivity.this);
                            dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                    .setMessage(response.optString("message"))
                                    .setPositiveButton(getString(R.string.dialog_button_retry),
                                            new DialogSheet.OnPositiveClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    callSaveInvoiceNumber_second();
                                                }
                                            })
                                    .setBackgroundColor(ContextCompat.getColor(PackingActivity.this, R.color.white))
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

                mLoadingLayout.setVisibility(GONE);
                Utils.checkVolleyError(PackingActivity.this, error);
                MyDialogSheet dialogSheet = new MyDialogSheet(PackingActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(PackingActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callSaveInvoiceNumber_second();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(PackingActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(PackingActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };

        rQueue.add(clearNotifRequest);

    }

    public void onClickEditBill(View view) {
        new EditPriceAlert(this).showEditOrderAlert(priceDetails, new EditPriceAlert.onPriceEdited() {
            @Override
            public void onPriceEdited(final String amount, final String grandTotal) {
                JSONObject reqObj = new JSONObject();
                try {
                    reqObj.put("strOrderId", priceDetails.getData().get(0).getStrOrderID());
                    reqObj.put("strAmount", amount);
                    reqObj.put("strSubTotal", priceDetails.getData().get(0).getIntSubTotal());
                    reqObj.put("strGrandTotal", grandTotal);
                    reqObj.put("strLoginUserId", priceDetails.getData().get(0).getStrCreateUserId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new OrderManagementAPI(PackingActivity.this).editOrderPrice(reqObj, new ResponseCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Toast.makeText(PackingActivity.this, "Price has been updated..!", Toast.LENGTH_SHORT).show();
                        txt_price_variation.setText("AED " + Utils.toFixed(Float.parseFloat(amount)));
                        lay_price_variation.setVisibility(VISIBLE);
                        mTxtTotalPrice.setText("AED " + Utils.toFixed(Float.parseFloat(grandTotal)));
                        btnEditPrice.setVisibility(GONE);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(PackingActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this, orderId);
    }
}
