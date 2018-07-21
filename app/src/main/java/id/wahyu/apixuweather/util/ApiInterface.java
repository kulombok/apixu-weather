package id.wahyu.apixuweather.util;

import id.wahyu.apixuweather.model.ForeCast;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public interface ApiInterface {
    @GET("forecast.json?")
    Observable<ForeCast> getForecast(@Query("key") String key, @Query("q") String city, @Query("days") int day);
}
