package ae.tudomart.store.helpers;

import android.content.Context;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class CircularImageProgress {

    public CircularProgressDrawable getProgress(Context context)
    {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }


}
