package com.agh.wfiis.piase.inz.utils.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by piase on 2018-01-07.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private Context context;

    public NetworkChangeReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if(!checkInternet(context))
        {
            Toast toast = Toast.makeText(this.context, "internet is not available", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            return true;
        } else {
            return false;
        }
    }

}