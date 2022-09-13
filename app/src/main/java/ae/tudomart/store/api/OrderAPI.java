package ae.tudomart.store.api;

import android.app.Activity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ae.tudomart.store.helpers.network.ApiUrl;
import ae.tudomart.store.ui.activities.ordersubstitute.OrderSubstituteRequest;
import ae.tudomart.store.utils.ResponseCallback;
import ae.tudomart.store.utils.WebService;

public class OrderAPI extends WebService {
    public OrderAPI(Activity activity) {
        super(activity);
    }

    public void initSubstitute(OrderSubstituteRequest reqObj, final ResponseCallback<JSONObject> responseCallback) {
        String url = ApiUrl.SUBSTITUTE_PRODUCTS_URL;
        try {
            JSONObject req = new JSONObject(new Gson().toJson(reqObj));
            post(url, req, new OnApiCallback() {
                @Override
                public void onSuccess(JSONObject reponse) {
                    responseCallback.onSuccess(reponse);
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
