package id.wahyu.apixuweather.util;

import id.wahyu.apixuweather.model.ForeCast;
import rx.Observable;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public interface ApiInteractor {
    Observable<ForeCast> getForeCast(String key, String city, int day);
}
