package com.tudomart.store.helpers.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.tudomart.store.R;


import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class VolleyErrorHandler {

    Context context;

    public VolleyErrorHandler(Context context) {
        this.context = context;
    }

    public String getVolleyError(VolleyError error){

        String errorMsg="";
        if(error instanceof NoConnectionError){
            ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork=null;
            if(cm!=null){
                activeNetwork=cm.getActiveNetworkInfo();

            }
            if(activeNetwork!=null&&activeNetwork.isConnectedOrConnecting()){
                errorMsg=context.getString(R.string.volley_server_not_connected);
            }else{
                errorMsg=context.getString(R.string.volley_internet_not_active);
            }
        }else if(error instanceof NetworkError ||error.getCause()instanceof ConnectException){
            errorMsg=context.getString(R.string.volley_device_not_connected);
        }else if(error.getCause()instanceof MalformedURLException){
            errorMsg=context.getString(R.string.volley_bad_request);
        }else if(error instanceof ParseError ||error.getCause()instanceof IllegalStateException
                ||error.getCause()instanceof JSONException
                ||error.getCause()instanceof XmlPullParserException){
            errorMsg=context.getString(R.string.volley_error_parse_data);
        }else if(error.getCause()instanceof OutOfMemoryError){
            errorMsg=context.getString(R.string.volley_device_out_of_memory);
        }else if(error instanceof AuthFailureError){
            errorMsg=context.getString(R.string.volley_server_authentication_fail);
        }else if(error instanceof ServerError ||error.getCause()instanceof ServerError){
            errorMsg=context.getString(R.string.volley_internal_server_error);
        }else if(error instanceof TimeoutError ||error.getCause()instanceof SocketTimeoutException
                ||error.getCause()instanceof ConnectTimeoutException
                ||error.getCause()instanceof SocketException
                ||(error.getCause().getMessage()!=null
                &&error.getCause().getMessage().contains("Connection timed out"))){
            errorMsg=context.getString(R.string.volley_connection_time_out);
        }else{
            errorMsg=context.getString(R.string.volley_something_bad_occurred);
        }
        return errorMsg;
    }
}
