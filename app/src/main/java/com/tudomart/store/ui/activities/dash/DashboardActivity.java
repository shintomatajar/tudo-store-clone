package com.tudomart.store.ui.activities.dash;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.marcoscg.dialogsheet.DialogSheet;
import com.tudomart.store.helpers.network.service.SocketService;
import com.tudomart.store.ui.activities.AllOrdersActivity;
import com.tudomart.store.ui.activities.BaseActivity;
import com.tudomart.store.ui.activities.NotificationsActivity;
import com.tudomart.store.ui.activities.PrivacyPolicyActivity;
import com.tudomart.store.ui.activities.ProfileActivity;
import com.tudomart.store.ui.activities.auth.MainActivity;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.utils.Utils;

import com.tudomart.store.R;

import com.tudomart.store.helpers.network.service.SocketService;
import com.tudomart.store.helpers.sharedPref.UserSessionManager;
import com.tudomart.store.ui.activities.AllOrdersActivity;
import com.tudomart.store.ui.activities.BaseActivity;
import com.tudomart.store.ui.activities.NotificationsActivity;
import com.tudomart.store.ui.activities.PrivacyPolicyActivity;
import com.tudomart.store.ui.activities.ProfileActivity;
import com.tudomart.store.ui.activities.auth.MainActivity;
import com.tudomart.store.ui.activities.barcodeGenerator.BarcodeGeneratorActivity;
import com.tudomart.store.ui.activities.settings.SettingsActivity;
import com.tudomart.store.ui.activities.staticScreens.AboutUsActivity;
import com.tudomart.store.ui.activities.staticScreens.HelpCentreActivity;
import com.tudomart.store.ui.activities.staticScreens.LegalActivity;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.utils.Utils;

public class DashboardActivity extends BaseActivity {

    Boolean isDualPane = false;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private LinearLayout linMyNotifications;
    private TextView linSettings;
    private TextView linPrivacyPolicy;
    private TextView linLegal;
    private TextView linHelpCentre;
    private ImageView serviceIcon;
    private TextView linAboutUs;
    private LinearLayout linMyAccount;
    private LinearLayout linMyOrders;
    private static final String TAG = "DashBoardActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initLayout();
        initClicks();
        requestAlertPermission();
        initService();
    }

    private void requestAlertPermission() {
        if (isSystemAlertPermissionGranted())
            return;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Permission Required");
        alertDialogBuilder.setMessage("Permission to draw over other apps is required to show alert while new orders are placed.");
        alertDialogBuilder.setPositiveButton("Grand Permission",
                (arg0, arg1) -> {
                    final String packageName = getPackageName();
                    final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName));
                    startActivityForResult(intent, 123);
                    arg0.dismiss();
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DashboardActivity.this, "Alerts will be disabled", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!isSystemAlertPermissionGranted()) {
            showPermissionDeniedAlert();
        }
    }

    private void showPermissionDeniedAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Permission denied");
        alertDialogBuilder.setMessage("Permission to draw over other apps has been denied, alerts will not be shown while new orders are placed.");
        alertDialogBuilder.setPositiveButton("Grand Permission",
                (arg0, arg1) -> {
                    requestAlertPermission();
                    arg0.dismiss();
                });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DashboardActivity.this, "Alerts disabled", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean isSystemAlertPermissionGranted() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this);
    }

    private void initService() {
        if (!new UserSessionManager(this).isServiceStopped()) {
            startService();
        }
        updateButton();
        serviceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isMyServiceRunning(DashboardActivity.this, SocketService.class)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                    alertDialogBuilder.setTitle("Stop looking for orders in background?");
                    alertDialogBuilder.setMessage("Do you want to stop looking for new orders in background");
                    alertDialogBuilder.setPositiveButton("Yes",
                            (arg0, arg1) -> {
                                Intent myService = new Intent(DashboardActivity.this, SocketService.class);
                                stopService(myService);
                                new UserSessionManager(getApplicationContext()).setServiceStopped(true);
                                updateButton();
                            });
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setNegativeButton("Cancel", null);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    startService();
                    new UserSessionManager(getApplicationContext()).setServiceStopped(false);
                    updateButton();
                }
            }
        });
    }

    private void updateButton() {
        if (Utils.isMyServiceRunning(this, SocketService.class)) {
            serviceIcon.setColorFilter(ContextCompat.getColor(this, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            serviceIcon.setColorFilter(ContextCompat.getColor(this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    private void startService() {
        Intent intent = new Intent(this, SocketService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private void initClicks() {
        linMyNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT, true);
            }
        });
        linSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT, true);
            }
        });
        linPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT, true);
            }
        });
        linLegal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LegalActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT, true);
            }
        });
        linHelpCentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HelpCentreActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT, true);
            }
        });
        linAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT, true);
            }
        });
        linMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT, true);
            }
        });
        linMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AllOrdersActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT, true);

            }
        });

    }

    private void initLayout() {
        serviceIcon = findViewById(R.id.serviceIcon);
        //  swipeRefreshLayout = findViewById(R.id.srlDashSwipeRefresh);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        linMyNotifications = findViewById(R.id.lin_my_notifications);
        linSettings = findViewById(R.id.lin_settings);
        linPrivacyPolicy = findViewById(R.id.lin_privacy_policy);
        linLegal = findViewById(R.id.lin_legal);
        linHelpCentre = findViewById(R.id.lin_help_centre);
        linAboutUs = findViewById(R.id.lin_about_us);
        linMyAccount = findViewById(R.id.lin_my_account);
        linMyOrders = findViewById(R.id.lin_my_orders);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logoutUser();
                break;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.privacy_policy:
                startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
                break;
            case R.id.legal:
                startActivity(new Intent(getApplicationContext(), LegalActivity.class));
                break;
            case R.id.help_center:
                startActivity(new Intent(getApplicationContext(), HelpCentreActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                break;
            case R.id.startService:
                initService();
                break;
        }


        return true;
    }

    private void logoutUser() {
        MyDialogSheet dialog = new MyDialogSheet(DashboardActivity.this);
        dialog.setTitle("Logout");
        dialog.setMessage("Are you sure logout?");
        dialog.setPositiveButton("Yes", new DialogSheet.OnPositiveClickListener() {
            @Override
            public void onClick(View v) {
                new UserSessionManager(DashboardActivity.this).logoutUser();
                finishAffinity();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    public void onClickNotification(View view) {
        startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        MyDialogSheet dialog = new MyDialogSheet(DashboardActivity.this);
        dialog.setTitle("Exit");
        dialog.setMessage("Are you sure want to exit?");
        dialog.setPositiveButton("Yes", new DialogSheet.OnPositiveClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);*/
                finishAffinity();

            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();

    }


    public void generateBarcode(View view) {

        startActivity(new Intent(getApplicationContext(), BarcodeGeneratorActivity.class));

    }
    @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this,orderId);
    }
}
