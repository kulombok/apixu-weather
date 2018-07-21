package id.wahyu.apixuweather.util;

import java.io.IOException;

import id.wahyu.apixuweather.R;

/**
 * Created by 0426591017 on 7/21/2018.
 */

public class InternetConnectionException extends IOException {
    @Override
    public String getMessage() {
        return String.valueOf(R.string.message_no_internet_connection);
    }
}
