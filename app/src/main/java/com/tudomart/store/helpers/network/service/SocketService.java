package com.tudomart.store.helpers.network.service;

import static com.tudomart.store.helpers.network.ApiUrl.SOCKET_BASE_URL;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.BuildConfig;
import com.tudomart.store.R;

import org.json.JSONObject;

import java.net.URISyntaxException;

import com.tudomart.store.helpers.network.RequestController;
import com.tudomart.store.helpers.sharedPref.UserSessionManager;
import com.tudomart.store.ui.activities.alert.NewOrderAlertActivity;
import com.tudomart.store.ui.activities.auth.MainActivity;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;

public class SocketService extends Service {

    private Socket mSocket;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initSocket();
        initNetwork();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initNetwork() {
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Log.d("Network", "Available");
                mSocket.connect();
            }

            @Override
            public void onLost(Network network) {
                Log.d("Network", "Lost");
                mSocket.disconnect();
            }
        };

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }
    }

    private void initSocket() {
        try {
            IO.Options options = new IO.Options();
            options.transports = new String[]{WebSocket.NAME};
            mSocket = IO.socket(ApiUrl.SOCKET_BASE_URL, options);
            mSocket.on("newOrder", args -> {
                if (args.length > 0) {
                    processSocketResponse(String.valueOf(args[0]));
                    return;
                }
                Log.d("Socket data ", "empty");
            });
            mSocket.on(Socket.EVENT_CONNECT, args -> Log.d("Socket", "Connected"));
            mSocket.on(Socket.EVENT_ERROR, args -> {
                Log.d("SOCKET ", "EROR " + args[0]);
                mSocket.disconnect();
            });
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, args -> {
                Log.d("SOCKET ", "TIMEOUT " + args[0]);
                mSocket.disconnect();
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
            mSocket.disconnect();
        }

    }

    private void processSocketResponse(String s) {
        try {
            JSONObject orderData = new JSONObject(s);
            String storeId = orderData.optString("store_id");
            String currentStoreId = new UserSessionManager(this).getShopId();
            String orderId = orderData.optString("order_id");
            if (new UserSessionManager(this).getShopIdArray().contains(storeId)) {
                RequestController app = (RequestController) getApplication();
                app.playAudio();
                startActivity(new Intent(this, NewOrderAlertActivity.class).putExtra("ORDER_ID", orderId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID;
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.splash_image)
                .setContentTitle("App is running in background")
                .setContentText(getString(R.string.app_name_release) + "is listening for new orders.")
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(START_NOT_STICKY, notification);
    }
}
