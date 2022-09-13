package com.tudomart.store.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.tudomart.store.helpers.sharedPref.UserSessionManager;

public class Utils {
    static AlertDialog alert;

    public static String toFixed(float number) {
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(number);
    }

    public static String toFixed(double number) {
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(number);
    }

    public static void checkVolleyError(Activity context, VolleyError error) {
        if (error == null || error.networkResponse == null) {
            return;
        }
        if (error.networkResponse.statusCode == 401) {
            Toast.makeText(context, "Session expired, Login to continue", Toast.LENGTH_SHORT).show();
            new UserSessionManager(context).logoutUser();
        }
    }

    public static JSONArray getStoreJson(ArrayList<String> storeList) {
        JSONArray jsonArray = new JSONArray();
        for (String store:storeList){
            jsonArray.put(store);
        }
        return jsonArray;
    }

    public static void showNewOrderAlert(Activity context, String orderId) {
/*
        try {
            if (alert != null) {
                alert.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert = new AlertDialog.Builder(context)
                            .setTitle("New Order Received")
                            .setMessage("a new order has been received, do you want to continue with it?")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                context.startActivity(new Intent(context, OrderAcceptActivity.class)
                                        .putExtra("ORDER_ID", orderId));
                                dialog.cancel();
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
