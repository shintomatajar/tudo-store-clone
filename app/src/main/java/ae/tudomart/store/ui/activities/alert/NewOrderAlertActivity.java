package ae.tudomart.store.ui.activities.alert;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ae.matajar.store.R;

import ae.tudomart.store.helpers.sharedPref.UserSessionManager;
import ae.tudomart.store.ui.activities.OrderAcceptActivity;
import ae.tudomart.store.ui.activities.auth.MainActivity;

public class NewOrderAlertActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnOpen, btnClose;
    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_alert);
        initViews();
        playSound();
    }

    private void playSound() {
        try {
            mMediaPlayer = new MediaPlayer();
            Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.android_sms);
            mMediaPlayer.setDataSource(getApplicationContext(), mediaPath);
            mMediaPlayer.prepare();
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        btnOpen = findViewById(R.id.btnOpen);
        btnClose = findViewById(R.id.btnClose);
        btnOpen.setOnClickListener(this);
        btnClose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        mMediaPlayer.stop();
        if (v.getId() == R.id.btnClose) {
            finish();
        }
        if (v.getId() == R.id.btnOpen) {
            UserSessionManager session = new UserSessionManager(this);
            if (session.isUserLoggedIn()) {
                String orderId = getIntent().getStringExtra("ORDER_ID");
                if (orderId != null) {
                    startActivity(new Intent(getApplicationContext(), OrderAcceptActivity.class)
                            .putExtra("ORDER_ID", orderId));
                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
    }
}