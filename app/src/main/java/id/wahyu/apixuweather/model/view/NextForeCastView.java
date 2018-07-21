package id.wahyu.apixuweather.model.view;

import android.databinding.Bindable;
import android.databinding.BaseObservable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class NextForeCastView extends BaseObservable{
    @Bindable
    public String day;

    @Bindable
    public String temperatur;

    public NextForeCastView(String day, String temperatur) {
        setDay(day);
        setTemperatur(temperatur);
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
        notifyPropertyChanged(BR.day);
    }

    public String getTemperatur() {
        return temperatur;
    }

    public void setTemperatur(String temperatur) {
        this.temperatur = temperatur;
        notifyPropertyChanged(BR.temperatur);
    }
}
