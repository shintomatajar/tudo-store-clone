package com.tudomart.store.ui.activities.staticScreens;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tudomart.store.ui.activities.BaseActivity;
import com.tudomart.store.utils.Utils;

import com.tudomart.store.R;

import com.tudomart.store.ui.activities.BaseActivity;
import com.tudomart.store.utils.Utils;

public class HelpCentreActivity extends BaseActivity {
    String url = "https://tudomart.com";
    String mail = "onlinesales@tudomart.com";

    String FACEBOOK_URL = "facebook.com/TudomartUAE";
    String WHATSAPP_URL = "https://api.whatsapp.com/send?phone=+971 56 559 4679";
    String TWITTER_URL = "https://twitter.com/";
    String YOUTUBE_URL = "https://youtube.com/";
    String INSTAGRAM_URL = "https://www.instagram.com/tudo_mart";
    String PLAYSTORE_URL = "https://play.google.com/store/";
    private ImageView backIcon;
    private TextView titleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_centre);
        initViews();
        initToolbar();
    }

    private void initToolbar() {
        titleToolbar.setText("Help Centre");
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        backIcon = findViewById(R.id.back_icon);
        titleToolbar = findViewById(R.id.title_toolbar);
    }


    public void contactWeb(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void contactPhone(View view) {
        Dexter.withContext(this).withPermission(Manifest.permission.CALL_PHONE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+97142645687"));
                startActivity(callIntent);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getApplicationContext(), "Call permission denied", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();

    }

    public void contactMail(View view) {
        Intent intent = new Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mail, null));
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    public void openWhatsApp(View view) {
        String mob_num = "+971502164297";
        try {
            Uri uri = Uri.parse("whatsapp://send?phone=$mob_num");
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void startFacebook(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
        startActivity(intent);
    }

    public void startTwitter(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_URL));
        startActivity(intent);
    }

    public void startYoutube(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL));
        startActivity(intent);
    }

    public void startInstagram(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(INSTAGRAM_URL));
        startActivity(intent);
    }

    public void rateUs(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAYSTORE_URL));
        startActivity(intent);
    }

    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this, orderId);
    }
}
