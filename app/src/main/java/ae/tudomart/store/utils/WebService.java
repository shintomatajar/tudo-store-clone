package ae.tudomart.store.utils;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import ae.tudomart.store.helpers.network.RequestController;
import ae.tudomart.store.helpers.sharedPref.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public abstract class WebService {
    Activity activity;

    public WebService(Activity activity) {
        this.activity = activity;
    }

    public void post(String url, JSONObject reqObj, final OnApiCallback onApiCallback){
        RequestController.getInstance().addToRequestQueue(new JsonObjectRequest(POST, url, reqObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")){
                        onApiCallback.onSuccess(response);
                    } else {
                        onApiCallback.onError(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    onApiCallback.onError("An error occured, please try again");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.checkVolleyError(activity,error);
                onApiCallback.onError("The operation could not be completed, please try again");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", new UserSessionManager(activity).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        });
    }

   public interface OnApiCallback {
        void onSuccess(JSONObject reponse);
        void onError(String error);
    }


}
