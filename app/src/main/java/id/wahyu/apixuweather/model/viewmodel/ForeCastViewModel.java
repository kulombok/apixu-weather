package id.wahyu.apixuweather.model.viewmodel;

import id.wahyu.apixuweather.model.ForeCast;
import id.wahyu.apixuweather.util.ApiInterface;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class ForeCastViewModel {

    private ApiInterface interactor;
    private Scheduler scheduler;

    public ForeCastViewModel(ApiInterface interactor, Scheduler scheduler) {
        this.interactor = interactor;
        this.scheduler = scheduler;
    }

    public Observable<ForeCast> getForeCast(String key, String city, int day){
        return interactor.getForecast(key, city, day).subscribeOn(scheduler).observeOn(scheduler);
    }
}
