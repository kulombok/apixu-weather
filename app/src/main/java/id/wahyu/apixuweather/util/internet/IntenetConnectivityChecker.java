package id.wahyu.apixuweather.util.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import id.wahyu.apixuweather.util.ApiService;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class IntenetConnectivityChecker implements Interceptor {
    private static String site = ApiService.PROVIDER_URL;
    private static Random random = new Random();
    private String TAG = "INTERCEPTOR:";
    private Context context;

    public IntenetConnectivityChecker(Context context){
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d(TAG, "Begin");

        if (!hasActiveDevice(context)) {
            Log.e(TAG, "Interupted by no device connection");
            throw new InternetConnectionDeviceException();
        } else if (!hasActiveInternetConnetion()){
            Log.e(TAG, "Interupted by no connection");
            throw new InternetConnectionException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

    // Check Internet Connection by Device
    public static boolean hasActiveDevice(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connMgr != null;
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) {

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }

    public static boolean hasActiveInternetConnetion(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Response resp = null;
        int code = 400;

        HttpURLConnection urlc = null;
        try {
            URL url = new URL(site + "?idrandom=" + random.nextInt());
            urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("User-Agent", "test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setRequestMethod("GET");
            urlc.setDoInput(true);
            urlc.setReadTimeout(60000);
            urlc.setConnectTimeout(60000); // mTimeout is one minutes
            urlc.connect();
            Log.d("Info", "Response Code : "+urlc.getResponseCode());
            code = urlc.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("warning", "Error checking internet connection", e);
        } finally {
            if(urlc != null){
                try {
                    urlc.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return code == 200;
    }
}
