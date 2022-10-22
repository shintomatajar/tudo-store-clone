package com.tudomart.store.helpers.sharedPref;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tudomart.store.helpers.network.service.SocketService;
import com.tudomart.store.model.UserSessionDataModel;
import com.tudomart.store.model.order.TudoStoreNamesModel;
import com.tudomart.store.ui.activities.auth.MainActivity;
import com.tudomart.store.ui.activities.dash.DashboardActivity;
import com.tudomart.store.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UserSessionManager {

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    Editor editor;

    // Context
    Activity _context;
    Context appContext;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "UserDataPref";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    private static final String IS_SERVICE_STOPPED = "IS_SERVICE_STOPPED";

    private static final String KEY_TOKEN = "token";
    public static final String KEY_USER_ID = "userid";

    //make variable public to access from outside
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NO = "phone";
    public static final String KEY_SHOP_ID = "shop";
    public static final String KEY_SHOP_ID_ARR = "shop_array";
//    public static final String KEY_INDEX = "index";

    // Tudo store name
    public static final String KEY_SHOP = "tudoshop";
    public static final String KEY_SHOP1 = "tudoshop1";
    public static final String KEY_SHOP2 = "tudoshop2";
    public static final String KEY_SHOP3 = "tudoshop3";
    public static final String KEY_SHOP4 = "tudoshop4";
    public static final String KEY_SHOP5 = "tudoshop5";
    public static final String KEY_SHOP6 = "tudoshop6";
    public static final String KEY_SHOP7 = "tudoshop7";
    public static final String KEY_SHOP8 = "tudoshop8";
    public static final String KEY_SHOP9 = "tudoshop9";
    public static final String KEY_SHOP10 = "tudoshop10";
    public static final String KEY_SHOP11 = "tudoshop11";

    // Constructor
    public UserSessionManager(Activity context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public UserSessionManager(Context context) {
        this.appContext = context;
        pref = appContext.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(UserSessionDataModel userSessionDataModel) {
        Set<String> set = new HashSet<>(Arrays.asList(userSessionDataModel.getShopList()));

        // Storing
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_TOKEN, userSessionDataModel.getToken());
        editor.putString(KEY_USER_ID, userSessionDataModel.getUserId());
        editor.putString(KEY_NAME, userSessionDataModel.getFirstName());
        editor.putString(KEY_EMAIL, userSessionDataModel.getEmail());
        editor.putString(KEY_PHONE_NO, userSessionDataModel.getPhoneNo());
        editor.putString(KEY_SHOP_ID, userSessionDataModel.getShop_id());
        editor.putStringSet(KEY_SHOP_ID_ARR, set);

        // commit changes
        editor.commit();
    }

    public void createTudoStoreNamesSession(TudoStoreNamesModel tudoStoreNamesModel) {
        editor.putString(KEY_SHOP, tudoStoreNamesModel.gettStore());
        editor.putString(KEY_SHOP1, tudoStoreNamesModel.gettStore1());
        editor.putString(KEY_SHOP2, tudoStoreNamesModel.gettStore2());
        editor.putString(KEY_SHOP3, tudoStoreNamesModel.gettStore3());
        editor.putString(KEY_SHOP4, tudoStoreNamesModel.gettStore4());
        editor.putString(KEY_SHOP5, tudoStoreNamesModel.gettStore5());
        editor.putString(KEY_SHOP6, tudoStoreNamesModel.gettStore6());
        editor.putString(KEY_SHOP7, tudoStoreNamesModel.gettStore7());
        editor.putString(KEY_SHOP8, tudoStoreNamesModel.gettStore8());
        editor.putString(KEY_SHOP9, tudoStoreNamesModel.gettStore9());
        editor.putString(KEY_SHOP10, tudoStoreNamesModel.gettStore10());
        editor.putString(KEY_SHOP11, tudoStoreNamesModel.gettStore11());
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If true it will redirect user to main page
     * Else do anything
     */
    public boolean checkLogin() {
        // Check login status
        if (this.isUserLoggedIn()) {

            // user is logged in redirect him to Main Activity
            Intent i = new Intent(_context, DashboardActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
            return true;
        }
        return false;
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PHONE_NO, pref.getString(KEY_PHONE_NO, null));
        user.put(KEY_SHOP_ID, pref.getString(KEY_SHOP_ID, null));
        return user;
    }

    public HashMap<String, String> getTudoStoreNameDetails() {
        HashMap<String, String> storeNames = new HashMap<String, String>();
        storeNames.put(KEY_SHOP, pref.getString(KEY_SHOP, null));
        storeNames.put(KEY_SHOP1, pref.getString(KEY_SHOP1, null));
        storeNames.put(KEY_SHOP2, pref.getString(KEY_SHOP2, null));
        storeNames.put(KEY_SHOP3, pref.getString(KEY_SHOP3, null));
        storeNames.put(KEY_SHOP4, pref.getString(KEY_SHOP4, null));
        storeNames.put(KEY_SHOP5, pref.getString(KEY_SHOP5, null));
        storeNames.put(KEY_SHOP6, pref.getString(KEY_SHOP6, null));
        storeNames.put(KEY_SHOP7, pref.getString(KEY_SHOP7, null));
        storeNames.put(KEY_SHOP8, pref.getString(KEY_SHOP8, null));
        storeNames.put(KEY_SHOP9, pref.getString(KEY_SHOP9, null));
        storeNames.put(KEY_SHOP10, pref.getString(KEY_SHOP10, null));
        storeNames.put(KEY_SHOP11, pref.getString(KEY_SHOP11, null));
        return storeNames;
    }

    public String getShopId() {
        return pref.getString(KEY_SHOP_ID, "");
    }

    public ArrayList<String> getShopIdArray() {
        Set<String> prefList = pref.getStringSet(KEY_SHOP_ID_ARR, null);
        if (prefList != null && !prefList.isEmpty()) {
            return new ArrayList<>(prefList);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();
        _context.finishAffinity();
        if (Utils.isMyServiceRunning(_context, SocketService.class)) {
            Intent myService = new Intent(_context, SocketService.class);
            _context.stopService(myService);
        }
        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, MainActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.finishAffinity();
        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public boolean isServiceStopped() {
        return pref.getBoolean(IS_SERVICE_STOPPED, false);
    }

    public void setServiceStopped(boolean status) {
        editor.putBoolean(IS_SERVICE_STOPPED, status).apply();
    }

}