package com.tudomart.store.ui.activities.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.marcoscg.dialogsheet.DialogSheet;
import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.model.UserSessionDataModel;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.utils.Utils;
import com.tudomart.store.BuildConfig;
import com.tudomart.store.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.helpers.network.RequestController;
import com.tudomart.store.helpers.network.VolleyErrorHandler;
import com.tudomart.store.helpers.sharedPref.UserSessionManager;
import com.tudomart.store.model.UserSessionDataModel;
import com.tudomart.store.ui.activities.dash.DashboardActivity;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mHeader;
    private EditText mEdtUsername;
    private EditText mEdtPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_main);

        initView();
        handleUserSession();
//        if (BuildConfig.DEBUG) {
////            mEdtPassword.setText("Niffin@123*");
////            mEdtUsername.setText("niffin@matajar.ae");
////            mEdtPassword.setText("1234567");
////            mEdtUsername.setText("aslamAiko@test.com");
//        }
    }

    private void initView() {
        mHeader = findViewById(R.id.header);
        mEdtUsername = findViewById(R.id.edtUsername);
        mEdtPassword = findViewById(R.id.edtPassword);
    }

    public void startForgotPassword(View view) {
    }

    public void startSignup(View view) {
    }

    private void handleUserSession() {

        userSessionManager = new UserSessionManager(MainActivity.this);
        userSessionManager.checkLogin();
       /* if (userSessionManager.checkLogin())
        {
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            MainActivity.this.finish();
        }*/

    }

    public void onLoginClicked(View view) {

        if (mEdtUsername.getText().toString().isEmpty()) {

            showToast(getString(R.string.valid_msg_enter_email));
            mEdtUsername.requestFocus();
        } else {

            if (mEdtUsername.getText().toString().trim().matches(emailPattern)) {

                if (mEdtPassword.getText().toString().isEmpty()) {

                    showToast(getString(R.string.valid_msg_enter_password));
                    mEdtPassword.requestFocus();
                } else {

                    if (mEdtPassword.getText().toString().length() >= 6) {
                        callLoginAPI();
                    } else {
                        showToast(getString(R.string.valid_msg_invalid_password));
                        mEdtPassword.requestFocus();
                    }
                }

            } else {
                showToast(getString(R.string.valid_msg_invalid_email));
                mEdtUsername.requestFocus();
            }
        }


       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }
        }, 1000);*/

    }

    private void callLoginAPI() {
        final ProgressDialog loginProgress = new ProgressDialog(MainActivity.this);
        loginProgress.setMessage("Please Wait...");
        loginProgress.setCancelable(false);
        loginProgress.show();

        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("strEmail", mEdtUsername.getText().toString()/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strPassword", mEdtPassword.getText().toString()/* new UserSessionManager(requireActivity()).getUserId()*/);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d("Request", requestBody.toString());
        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.STORE_LOGIN_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loginProgress.dismiss();
                try {
                    if (response.getBoolean("success")) {
                        //Log.d("Response", response.toString());
                        JSONArray jsonArray = response.getJSONArray("data");
                        JSONObject userData = jsonArray.getJSONObject(0);
                        JSONArray storeJsonArray = userData.getJSONArray("arr_shops");
                        String[] storeList = new String[storeJsonArray.length()];
                        for (int i = 0; i < storeJsonArray.length(); i++) {
                            storeList[i] = storeJsonArray.getString(i);
                        }
                        UserSessionDataModel userSessionDataModel = new UserSessionDataModel(
                                jsonArray.getJSONObject(1).getString("token"),
                                userData.getString("pkUserId"),
                                userData.getString("strUserName"),
                                userData.getString("strEmail"),
                                userData.getString("strPhone"),
                                userData.getString("fkShopId")/*"5e94832e2103274b5750a53a"*/,
                                storeList);

                        userSessionManager.createUserLoginSession(userSessionDataModel);
                        userSessionManager.checkLogin();

                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        MainActivity.this.finish();

                    } else {
                        MyDialogSheet dialogSheet = new MyDialogSheet(MainActivity.this);
                        dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                .setMessage(response.getString("message"))
                                .setPositiveButton(getString(R.string.dialog_button_retry),
                                        new DialogSheet.OnPositiveClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                callLoginAPI();
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
                Utils.checkVolleyError(MainActivity.this, error);
                MyDialogSheet dialogSheet = new MyDialogSheet(MainActivity.this);
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(MainActivity.this).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        callLoginAPI();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white))
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
                params.put("Authorization", new UserSessionManager(MainActivity.this).getUserDetails().get("token")); //*new UserSessionManager(requireActivity()).getToken()*//*);
                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(clearNotifRequest);

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
