package es.apryso.aprysobarcodereader.utils;

import android.util.Log;

public class Logger {

    private static  final String KEY = "aprysoBarcodeReader";

    public static void error(String msg) {

        Log.e(KEY, msg);

    }
}
