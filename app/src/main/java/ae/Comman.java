package ae;

import android.content.Context;

import java.io.File;

import ae.matajar.store.R;

public class Comman {
    public static String getAppPath(Context context) {
        //File dir = new File(android.os.Environment.getExternalStorageDirectory()
        File dir = new File(context.getFilesDir()
                + File.separator
                + context.getResources().getString(R.string.app_name)
                + File.separator
        );
        if (!dir.exists())
            dir.mkdir();
        return dir.getPath() + File.separator;
    }
}
