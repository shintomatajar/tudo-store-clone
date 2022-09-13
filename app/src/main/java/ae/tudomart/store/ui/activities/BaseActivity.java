package ae.tudomart.store.ui.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import ae.tudomart.store.helpers.network.RequestController;
import ae.tudomart.store.helpers.sharedPref.UserSessionManager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public abstract class BaseActivity extends AppCompatActivity {
    private Socket mSocket;
    RequestController app;
    String orderId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSocket();
    }

    private void initSocket() {
        app = (RequestController) getApplication();
        mSocket = app.getSocket();
        mSocket.on("newOrder", args -> {
            if (args.length > 0) {
                processSocketResponse(String.valueOf(args[0]));
                return;
            }
            Log.d("Socket data ", "empty");
        });
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("Socket", "Connected");
            }
        });
        mSocket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("SOCKET ", "EROR " + args[0]);
                mSocket.disconnect();
            }
        });
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("SOCKET ", "TIMEOUT " + args[0]);
                mSocket.disconnect();
            }
        });
    }

    private void processSocketResponse(String s) {
        try {
            JSONObject orderData = new JSONObject(s);
            String storeId = orderData.optString("store_id");
            orderId = orderData.optString("order_id");

            if ((new UserSessionManager(this).getShopIdArray().contains(storeId))) {
                app.playAudio();
                showAlert(orderId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void showAlert(String orderId);


    /*@Override
    protected void onStart() {
        super.onStart();
        mSocket.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSocket.disconnect();
    }*/
}
