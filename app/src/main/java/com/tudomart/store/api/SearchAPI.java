package com.tudomart.store.api;

import android.app.Activity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.ui.activities.ordersubstitute.ModelSearchRequest;
import com.tudomart.store.ui.activities.ordersubstitute.ModelSubCategoryProductsResponse;
import com.tudomart.store.utils.ResponseCallback;
import com.tudomart.store.utils.WebService;

public class SearchAPI extends WebService {
    public SearchAPI(Activity activity) {
        super(activity);
    }

    public void getSearchResult(ModelSearchRequest reqObj, final ResponseCallback<List<ModelSubCategoryProductsResponse.Data>> responseCallback) {
        String url = ApiUrl.SEARCH_PRODUCT_URL;
        try {
            JSONObject req = new JSONObject(new Gson().toJson(reqObj));
            post(url, req, new OnApiCallback() {
                @Override
                public void onSuccess(JSONObject reponse) {
                    ModelSubCategoryProductsResponse res = new Gson().fromJson(reponse.toString(), ModelSubCategoryProductsResponse.class);
                    responseCallback.onSuccess(res.getData());
                }

                @Override
                public void onError(String error) {
                    responseCallback.onError(error);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            responseCallback.onError(e.getMessage());
        }
    }
}
