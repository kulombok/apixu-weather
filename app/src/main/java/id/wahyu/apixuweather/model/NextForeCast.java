package id.wahyu.apixuweather.model;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class NextForeCast {
    @SerializedName("forecastday")
    private List<DetailBean> detailNextForeCast;

    public List<DetailBean> getDetailNextForeCast() {
        return detailNextForeCast;
    }

    public void setDetailNextForeCast(List<DetailBean> detailNextForeCast) {
        this.detailNextForeCast = detailNextForeCast;
    }

    public static class DetailBean {
        @SerializedName("date")
        private String date;

        @SerializedName("day")
        private DayTemperatureBean dayTemperature;

        public String getDate() throws ParseException {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = format1.parse(date);
            return (String) DateFormat.format("EEEE", dt);
        }

        public void setDate(String date) {
            this.date = date;
        }

        public DayTemperatureBean getDayTemperature() {
            return dayTemperature;
        }

        public void setDayTemperature(DayTemperatureBean dayTemperature) {
            this.dayTemperature = dayTemperature;
        }

        public static class DayTemperatureBean {
            @SerializedName("avgtemp_c")
            private String averageTemperature;

            public String getAverageTemperature() {
                return averageTemperature;
            }

            public void setAverageTemperature(String averageTemperature) {
                this.averageTemperature = averageTemperature;
            }
        }
    }
}
