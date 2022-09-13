package ae.tudomart.store.ui.customViews;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import ae.matajar.store.R;

import java.util.Objects;


public class P07FancyAlert {
    private Activity activity;
    private Dialog dialog;
    private TextView message, btn_text;
    private TextView text_secondary;
    private final LottieAnimationView animationView;

    public P07FancyAlert(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.layout_animation_pop_up);
        animationView = dialog.findViewById(R.id.animationView);
        message = dialog.findViewById(R.id.message);
        btn_text = dialog.findViewById(R.id.text);

        animationView.setRepeatCount(0);

        text_secondary = dialog.findViewById(R.id.text_secondary);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        text_secondary.setText("");
        text_secondary.setVisibility(View.GONE);


    }

    public P07FancyAlert makeTransaparent() {
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        return this;
    }

    public P07FancyAlert setCancellable(Boolean _boolean) {
        dialog.setCancelable(_boolean);
        return this;
    }

    public P07FancyAlert setloop(Boolean aboolean) {
        animationView.setRepeatCount(0);
        return this;
    }

    public P07FancyAlert makeFullScreen() {
        Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        return this;
    }

    public P07FancyAlert setMessage(String msg) {
        message.setVisibility(View.VISIBLE);
        message.setText(msg);
        return this;
    }

    public P07FancyAlert setMessageColor(int color) {
        message.setTextColor(color);
        return this;
    }

    public P07FancyAlert setGif(int res) {
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(res);
        return this;
    }

    public P07FancyAlert setSecondButton(String text, View.OnClickListener onClickListener) {
        text_secondary.setVisibility(View.VISIBLE);
        text_secondary.setText(text);
        if (onClickListener == null) {
            text_secondary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            text_secondary.setOnClickListener(onClickListener);
        }
        return this;
    }

    public P07FancyAlert setButton(String text, View.OnClickListener onClickListener) {
        btn_text.setVisibility(View.VISIBLE);
        btn_text.setText(text);
        if (onClickListener == null) {
            btn_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            btn_text.setOnClickListener(onClickListener);
        }
        return this;
    }

    public P07FancyAlert show() {
        dialog.show();
        return this;
    }

    public P07FancyAlert dismiss() {
        dialog.dismiss();
        return this;
    }
}
