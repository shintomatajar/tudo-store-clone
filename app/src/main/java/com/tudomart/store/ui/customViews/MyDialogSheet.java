package com.tudomart.store.ui.customViews;

import android.app.Activity;
import android.graphics.Typeface;

import com.tudomart.store.R;
import com.marcoscg.dialogsheet.DialogSheet;

public class MyDialogSheet extends DialogSheet {
    Activity activity;

    public MyDialogSheet(Activity activity) {
        super(activity,false);
        this.activity = activity;
        setTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/roboto_bold.ttf"));
        setMessageTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/roboto_regular.ttf"));
        setButtonsColorRes(R.color.colorPrimary);
        setRoundedCorners(true);
        setColoredNavigationBar(true);
    }
}
