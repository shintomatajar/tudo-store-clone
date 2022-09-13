package com.tudomart.store.api;

import android.app.Activity;


import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.utils.ResponseCallback;
import com.tudomart.store.utils.WebService;

import org.json.JSONObject;

public class OrderManagementAPI extends WebService {
    public OrderManagementAPI(Activity activity) {
        super(activity);
    }

    public void editOrderPrice(JSONObject reqObj, final ResponseCallback responseCallback) {
        String url = ApiUrl.EDIT_ORDER_AMOUNT;
        post(url, reqObj, new OnApiCallback() {
            @Override
            public void onSuccess(JSONObject reponse) {
                responseCallback.onSuccess(reponse);
            }

            @Override
            public void onError(String error) {
                responseCallback.onError(error);
            }
        });
    }

    public void newPushApiCall(JSONObject reqObj, final ResponseCallback<JSONObject> responseCallback) {
        String url = ApiUrl.CUSTOMER_PUSH_CALL;
        post(url, reqObj, new OnApiCallback() {
            @Override
            public void onSuccess(JSONObject reponse) {
                responseCallback.onSuccess(reponse);
            }

            @Override
            public void onError(String error) {
                responseCallback.onError(error);
            }
        });
    }
}
