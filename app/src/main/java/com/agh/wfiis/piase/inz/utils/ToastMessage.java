package com.agh.wfiis.piase.inz.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by piase on 2018-01-11.
 */

public class ToastMessage {

    private static Context CONTEXT;

    public static void setContext(Context context) {
        CONTEXT = context;
    }

    public static void showToastMessage(String message) {
        Toast toast = Toast.makeText(CONTEXT, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

}
