package id.wahyu.apixuweather.util;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class ApiService {
    public static final String BASE_URL = "http://api.apixu.com";
    private static Retrofit retrofit = null;
    private static OkHttpClient client = null;

    public static Retrofit getClient(Context context) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new IntenetConnectivityChecker(context));

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        return retrofit;
    }
}
