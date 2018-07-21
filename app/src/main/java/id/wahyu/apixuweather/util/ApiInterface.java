package id.wahyu.apixuweather.util;

import id.wahyu.apixuweather.model.ForeCast;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public interface ApiInterface {
    @GET("?key={key}&q={city}&days={day}")
    Observable<ForeCast> getForecast(@Path("key") String key, @Path("city") String city, @Path("day") int day);
}
