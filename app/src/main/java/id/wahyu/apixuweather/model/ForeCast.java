package id.wahyu.apixuweather.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class ForeCast {
    @SerializedName("location")
    private CurrentForeCast location;

    @SerializedName("current")
    private CurrentForeCast current;

    @SerializedName("forecast")
    private NextForeCast forecast;

    public CurrentForeCast getLocation() {
        return location;
    }

    public void setLocation(CurrentForeCast location) {
        this.location = location;
    }

    public CurrentForeCast getCurrent() {
        return current;
    }

    public void setCurrent(CurrentForeCast current) {
        this.current = current;
    }
}
