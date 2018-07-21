package id.wahyu.apixuweather.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import id.wahyu.apixuweather.R;
import id.wahyu.apixuweather.databinding.ActivityMainBinding;
import id.wahyu.apixuweather.util.internet.IntenetConnectivityChecker;
import id.wahyu.apixuweather.util.internet.InternetNetworkChangeReceiver;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    private Context context;
    private ActivityMainBinding layoutBinding;
    private InternetNetworkChangeReceiver networkReceiver;
    private boolean oldResult;
    private String string = null;

    public BroadcastReceiver(Activity activity,
                             InternetNetworkChangeReceiver networkReceiver,
                             ActivityMainBinding lb) {
        this.context = activity;
        this.networkReceiver = networkReceiver;
        this.layoutBinding = lb;
    }

    @Override
    public void onReceive(Context c, Intent i) {
        if(i.hasExtra(InternetNetworkChangeReceiver.IS_NETWORK_AVAILABLE)) {
            boolean bol = i.getBooleanExtra(InternetNetworkChangeReceiver.IS_NETWORK_AVAILABLE, false);
//            Log.d("Broadcast String", String.valueOf(string));
//            Log.d("Broadcast Old", String.valueOf(oldResult));
            Log.d("Broadcast Result", String.valueOf(bol));
            if (string == null || oldResult != bol){
                oldResult = bol;
                string = String.valueOf(oldResult);

                if (bol) {
                    Snackbar.make(layoutBinding.wrapper, context.getResources().getString(R.string.message_internet_ok), Snackbar.LENGTH_SHORT).show();
                    layoutBinding.clContentWrapper.setVisibility(View.VISIBLE);
                    layoutBinding.clErrorWrapper.setVisibility(View.GONE);
                } else {
                    Log.d("Broadcast Device", String.valueOf(IntenetConnectivityChecker.hasActiveDevice(context)));
                    if (!IntenetConnectivityChecker.hasActiveDevice(context)) {
                        layoutBinding.clErrorWrapper.setVisibility(View.VISIBLE);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(layoutBinding.wrapper, context.getResources().getString(R.string.message_no_device_connection), Snackbar.LENGTH_INDEFINITE).setActionTextColor(context.getResources().getColor(R.color.white_txt_color)).setAction("Aktifkan", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Open Setting
                                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                        context.startActivity(intent);
                                    }
                                }).show();
                            }
                        }, 300);
                    }
                }
            }
        }
    }

    // For SDK API More than Marshmallow
    public void registerBroadcastInternetReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            context.registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    // For SDK API More than Marshmallow
    public void unregisterNetworkChanges() {
        try {
            if (networkReceiver != null) context.unregisterReceiver(networkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
