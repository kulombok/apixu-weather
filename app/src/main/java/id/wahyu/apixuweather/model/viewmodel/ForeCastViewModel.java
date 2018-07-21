package id.wahyu.apixuweather.model.viewmodel;

import id.wahyu.apixuweather.model.ForeCast;
import id.wahyu.apixuweather.util.ApiInteractor;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class ForeCastViewModel {

    private ApiInteractor interactor;
    private Scheduler scheduler;
    private String key, city;
    private int day;

    public ForeCastViewModel(ApiInteractor interactor, Scheduler scheduler, String key, String city, int day) {
        this.interactor = interactor;
        this.scheduler = scheduler;
        this.key = key;
        this.city = city;
        this.day = day;
    }

    public Observable<ForeCast> getForeCast(){
        return interactor.getForeCast(key, city, day).observeOn(scheduler);
    }
}
