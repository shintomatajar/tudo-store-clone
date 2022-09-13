package ae.tudomart.store.api;

import android.app.Activity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ae.tudomart.store.helpers.network.ApiUrl;
import ae.tudomart.store.ui.activities.ordersubstitute.ModelSearchRequest;
import ae.tudomart.store.ui.activities.ordersubstitute.ModelSubCategoryProductsResponse;
import ae.tudomart.store.utils.ResponseCallback;
import ae.tudomart.store.utils.WebService;

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
