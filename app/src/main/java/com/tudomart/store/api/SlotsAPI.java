package com.tudomart.store.api;

import android.app.Activity;

import org.json.JSONObject;

import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.utils.ResponseCallback;
import com.tudomart.store.utils.WebService;

public class SlotsAPI extends WebService {
    public SlotsAPI(Activity activity) {
        super(activity);
    }

    public void getSlotsWithDate(JSONObject reqObj, final ResponseCallback<JSONObject> responseCallback) {
        String url = ApiUrl.GET_SLOTS_WITH_DATE;
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
