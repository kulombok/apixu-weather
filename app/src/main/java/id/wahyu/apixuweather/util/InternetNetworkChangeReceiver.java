package id.wahyu.apixuweather.util;

import android.content.*;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class InternetNetworkChangeReceiver extends android.content.BroadcastReceiver {
    public static final String NETWORK_AVAILABLE_ACTION = "id.co.baf.receiver.NetworkAvailable";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try {
            Log.d("Broadcast", "Start Broadcast Receiver");
            Intent i = new Intent(NETWORK_AVAILABLE_ACTION);

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
            i.putExtra(IS_NETWORK_AVAILABLE, IntenetConnectivityChecker.hasActiveInternetConnetion());
            Log.d("Broadcast", String.valueOf(IntenetConnectivityChecker.hasActiveInternetConnetion()));

            broadcastManager.sendBroadcast(i);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
