package id.wahyu.apixuweather.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    private Context context;
    private Activity activity;
    private Class classDest;
    private View viewLayout;
    private ProgressBar progressBar;
    private InternetNetworkChangeReceiver networkReceiver;
    private boolean oldResult;
    private String string = null;

    public BroadcastReceiver(Activity activity,
                             InternetNetworkChangeReceiver networkReceiver,
                             ProgressBar progressBar) {
        this.context = activity;
        this.activity = activity;
        this.networkReceiver = networkReceiver;
        this.progressBar = progressBar;
    }

    public void onReceive(Intent intent, View view, Class classDest){
        this.viewLayout = view;
        this.classDest = classDest;
    }

    @Override
    public void onReceive(Context c, Intent i) {
        if(i.hasExtra(InternetNetworkChangeReceiver.IS_NETWORK_AVAILABLE)) {
            boolean bol = i.getBooleanExtra(InternetNetworkChangeReceiver.IS_NETWORK_AVAILABLE, false);
//            Log.d("Broadcast String", String.valueOf(string));
//            Log.d("Broadcast oldResult", String.valueOf(oldResult));
            Log.d("Broadcast Result", String.valueOf(bol));
            if (string == null || oldResult != bol){
                oldResult = bol;
                string = String.valueOf(oldResult);

                if (bol) {
//                    Snackbar.make(viewLayout, c.getResources().getString(id.co.baf.libraryapps.R.string.message_internet_ok), Snackbar.LENGTH_SHORT).show();
//                    c.startActivity(new Intent(c, classDest));
//                    activity.finish();
                } else {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            new InternetConnectionAsyncChecker(activity, classDest, progressBar, viewLayout).execute("Username", "Password");
//                        }
//                    }, 2000);
                }
            }
        }
    }

    // For SDK API Morethan Nougat
    public void registerBroadcastInternetReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            context.registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    // For SDK API Morethan Nougat
    public void unregisterNetworkChanges() {
        try {
            if (networkReceiver != null) context.unregisterReceiver(networkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
