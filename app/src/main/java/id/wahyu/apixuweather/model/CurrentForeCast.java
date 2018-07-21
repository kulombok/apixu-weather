package id.wahyu.apixuweather.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class CurrentForeCast {
    @SerializedName("name")
    private String name;

    @SerializedName("temp_c")
    private String current_temperature;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrent_temperature() {
        return current_temperature;
    }

    public void setCurrent_temperature(String current_temperature) {
        this.current_temperature = current_temperature;
    }
}
