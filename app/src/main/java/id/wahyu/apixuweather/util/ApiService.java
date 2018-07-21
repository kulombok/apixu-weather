package id.wahyu.apixuweather.util;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import id.wahyu.apixuweather.model.ForeCast;
import id.wahyu.apixuweather.util.internet.IntenetConnectivityChecker;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class ApiService implements ApiInteractor {
    public static final String PROVIDER_URL = "https://www.apixu.com/";
    public static final String API_BASE_URL = "http://api.apixu.com/v1/";
    public static final String TOKEN = "b9a40bc1f66a466a87a90744182107";

    private ApiInterface apiInterface;
    private static Retrofit retrofit = null;
    private static OkHttpClient client = null;

    public ApiService(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor);
//                .addInterceptor(new IntenetConnectivityChecker(context));

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client.build())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    @Override
    public Observable<ForeCast> getForeCast(String key, String city, int day) {
        return apiInterface.getForecast(key, city, day).subscribeOn(Schedulers.io());
    }
}
