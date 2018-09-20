package es.apryso.aprysobarcodereader.utils;

import android.content.Context;
import android.widget.Toast;

import es.apryso.aprysobarcodereader.activity.R;

public class PopUpUtils {

    public static void showPopup(Context context, String msg) {

        showPopup(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showPopup(Context context, String msg, int duration ) {

        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

}
