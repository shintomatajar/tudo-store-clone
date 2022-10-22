package com.tudomart.store.helpers.network;


import android.app.Application;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.tudomart.store.R;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;


public class RequestController extends Application {
    private Socket mSocket;
    public static final String TAG = RequestController.class.getSimpleName();

    private RequestQueue requestQueue;
    private MediaPlayer mMediaPlayer;
   //public static String SOCKET_IP = "http://15.185.106.17:5000";

    private static RequestController instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initSocket();
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        try {
            mMediaPlayer = new MediaPlayer();
            Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alert);
            mMediaPlayer.setDataSource(getApplicationContext(), mediaPath);
            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void playAudio(){
        mMediaPlayer.start();
    }

    private void initSocket() {
        try {
            IO.Options options = new IO.Options();
            options.transports = new String[]{WebSocket.NAME};
            mSocket = IO.socket(ApiUrl.SOCKET_BASE_URL, options);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            mSocket.disconnect();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mSocket.disconnect();
    }

    public Socket getSocket() {
        return mSocket;
    }

    public static synchronized RequestController getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        try {
            Log.d(TAG, "URL " + req.getUrl());
            Log.d(TAG, "BODY " + new String(req.getBody()));
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        req.setTag(TAG);
        /*req.setRetryPolicy(new DefaultRetryPolicy(30000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));*/
        req.setRetryPolicy(new DefaultRetryPolicy(35000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(req);
    }

    public void cancelPendingRequest(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

}

