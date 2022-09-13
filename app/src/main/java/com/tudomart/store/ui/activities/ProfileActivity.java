package com.tudomart.store.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marcoscg.dialogsheet.DialogSheet;
import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.utils.Utils;

import com.tudomart.store.R;

import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.helpers.network.RequestController;
import com.tudomart.store.helpers.network.VolleyErrorHandler;
import com.tudomart.store.helpers.sharedPref.UserSessionManager;
import com.tudomart.store.ui.activities.auth.MainActivity;
import com.tudomart.store.ui.activities.settings.SettingsActivity;
import com.tudomart.store.ui.activities.staticScreens.HelpCentreActivity;
import com.tudomart.store.ui.activities.staticScreens.LegalActivity;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProfileActivity extends BaseActivity {


    UserSessionManager userSessionManager;
    private ImageView mBackIcon;
    private TextView mTitleToolbar;
    private TextView mTxtUserIcon;
    private TextView mTxtUserName;
    private TextView mTxtUserEmail;
    private TextView mTxtStoreName;
    private TextView mTxtTaxRegNum;
    private TextView mTxtAddress;
    private TextView mTxtEmirate;
    private TextView mTxtPhone;
    private ImageView mBtnEditPhone;
    private TextView mTxtVersion;
    private NestedScrollView mDataLayout;
    private ImageView mImgError;
    private TextView mTxtErrorTitle;
    private TextView mTxtErrorMessage;
    private Button mBtnRetry;
    private LinearLayout mErrorParentLayout;
    private RelativeLayout mErrorLayout;
    private ProgressBar mLoadingProgress;
    private RelativeLayout mLoadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        initToolbar();
        handleUserSession();
        showProfie();
        initClicks();

    }

    private void showProfie() {

        mLoadingLayout.setVisibility(VISIBLE);

        RequestQueue rQueue = Volley.newRequestQueue(ProfileActivity.this);
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("strLoginUserId", new UserSessionManager(ProfileActivity.this).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.STORE_PROFILE_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                mLoadingLayout.setVisibility(GONE);
                try {
                    if (response.getBoolean("success")) {


                        mDataLayout.setVisibility(VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("data");

                        String userName = jsonArray.getJSONObject(0).getString("strUserName");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String location = jsonArray.getJSONObject(0).getJSONArray("arrayShop").getJSONObject(0).getString("strLocation");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String UserEmail = jsonArray.getJSONObject(0).getString("strEmail");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String ShopName =  jsonArray.getJSONObject(0).getJSONArray("arrayShop").getJSONObject(0).getString("strShopName");
                        String Address =  jsonArray.getJSONObject(0).getJSONArray("arrayShop").getJSONObject(0).getString("strAddress");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");


                        String phoneNumber = jsonArray.getJSONObject(0).getString("strPhone");
                        String tax_number = jsonArray.getJSONObject(0).getString("fkShopId");

                        mTxtUserName.setText(userName);
                        mTxtStoreName.setText(ShopName);

                        mTxtUserEmail.setText(UserEmail);
                        mTxtEmirate.setText(location);

                        mTxtAddress.setText(Address);
                        mTxtPhone.setText("+971" + phoneNumber);
                        mTxtTaxRegNum.setText(tax_number);


                        mTxtAddress.setText(Address);
                        mTxtUserIcon.setText(userName.substring(0,1));


                    } else {
                        mErrorLayout.setVisibility(GONE);
                        mDataLayout.setVisibility(GONE);
                        if (response.getString("message") == " No Data Found") { // user has made no orders

                            mErrorLayout.setVisibility(View.VISIBLE);
                            mTxtErrorMessage.setText(response.getString("message"));

                        } else {

                            mErrorLayout.setVisibility(View.VISIBLE);
                            mTxtErrorMessage.setText(response.getString("message"));

                           /* MyDialogSheet dialogSheet = new MyDialogSheet(ProfileActivity.this);
                            dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                    .setMessage(response.getString("message"))
                                    .setPositiveButton(getString(R.string.dialog_button_retry),
                                            new DialogSheet.OnPositiveClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    showProfie();
                                                }
                                            })
                                    .setBackgroundColor(ContextCompat.getColor(ProfileActivity.this, R.color.white))
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
                mDataLayout.setVisibility(GONE);
                Utils.checkVolleyError(ProfileActivity.this,error);
                MyDialogSheet dialogSheet = new MyDialogSheet(ProfileActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(ProfileActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showProfie();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(ProfileActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(ProfileActivity.this).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };

       RequestController.getInstance().addToRequestQueue(clearNotifRequest);


    }

    private void handleUserSession() {

        userSessionManager = new UserSessionManager(ProfileActivity.this);
    }

    private void initClicks() {

    }

    private void initToolbar() {
        mTitleToolbar.setText("My Profile");
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        mBackIcon = findViewById(R.id.back_icon);
        mTitleToolbar = findViewById(R.id.title_toolbar);
        mTxtUserIcon = findViewById(R.id.txt_user_icon);
        mTxtUserName = findViewById(R.id.txt_user_name);
        mTxtUserEmail = findViewById(R.id.txt_user_email);
        mTxtStoreName = findViewById(R.id.txt_store_name);
        mTxtTaxRegNum = findViewById(R.id.txt_tax_reg_num);
        mTxtAddress = findViewById(R.id.txt_address);
        mTxtEmirate = findViewById(R.id.txt_emirate);
        mTxtPhone = findViewById(R.id.txt_phone);
        mBtnEditPhone = findViewById(R.id.btn_edit_phone);
        mTxtVersion = findViewById(R.id.txt_version);
        mDataLayout = findViewById(R.id.data_layout);
        mImgError = findViewById(R.id.img_error);
        mTxtErrorTitle = findViewById(R.id.txt_error_title);
        mTxtErrorMessage = findViewById(R.id.txt_error_message);
        mBtnRetry = findViewById(R.id.btn_retry);
        mErrorParentLayout = findViewById(R.id.error_parent_layout);
        mErrorLayout = findViewById(R.id.error_layout);
        mLoadingProgress = findViewById(R.id.loadingProgress);
        mLoadingLayout = findViewById(R.id.loadingLayout);


    }

    public void onClickSettings(View view) {
        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
    }

    public void onClickLegal(View view) {
        startActivity(new Intent(getApplicationContext(), LegalActivity.class));
    }

    public void onClickPrivacyPolicy(View view) {
        startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
    }

    public void onClickHelpCenter(View view) {
        startActivity(new Intent(getApplicationContext(), HelpCentreActivity.class));
    }

    public void onClickLogout(View view) {
        MyDialogSheet dialog = new MyDialogSheet(ProfileActivity.this);
        dialog.setTitle("Logout");
        dialog.setMessage("Are you sure logout?");
        dialog.setPositiveButton("Yes", new DialogSheet.OnPositiveClickListener() {
            @Override
            public void onClick(View v) {
                userSessionManager.logoutUser();
                finishAffinity();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    public void onClickFaq(View view) {
    }

    public void onClickFeedbacks(View view) {
    }

    public void onClickEditPhone(View view) {
    }

    public void onClickManageAddress(View view) {
    }

    public void onCLickMyRewards(View view) {
    }

    public void onClickMyCoupons(View view) {
    }
    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this,orderId);
    }
}
