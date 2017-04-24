package com.example.android.sunshine.app;

/**
 * Created by Kevin_Yann on 22/04/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.Time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_location_key), context.getString(R.string.pref_location_default));
    }

    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_units_key), context.getString(R.string.pref_units_metric)).equals(context.getString(R.string.pref_units_metric));
    }

    static String formatTemperature(Context context, double temperature, boolean isMetric) {

        double temp;
        if ( !isMetric ) {
            temp = 9*temperature/5+32;
        }
        else {
            temp = temperature;
        }
        return context.getString(R.string.format_temperature, temp);
    }

    static String formatDate(long dateInMillis) {
        Date date = new Date(dateInMillis);
        return DateFormat.getDateInstance().format(date);
    }


    public static final String DATE_FORMAT = "yyyyMMdd";



    public static String getFriendlyDayString(Context context, long dateInMillis) {

        Time time = new Time();
        time.setToNow();
        long currentTime = System.currentTimeMillis();
        int julianDay = Time.getJulianDay(dateInMillis, time.gmtoff);
        int currentJulianDay = Time.getJulianDay(currentTime, time.gmtoff);

        if (julianDay == currentJulianDay) {
            String today = context.getString(R.string.today);
            int formatId = R.string.format_full_friendly_date;
            return String.format(context.getString(formatId, today, getFormattedMonthDay(context, dateInMillis)));
        }
        else if ( julianDay < currentJulianDay + 7 ) {
            return getDayName(context, dateInMillis);
        }
        else {
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(dateInMillis);
        }
    }



    public static String getDayName(Context context, long dateInMillis) {

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);

        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        }
        else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow);
        }
        else {
            Time time = new Time();
            time.setToNow();
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }




    public static String getFormattedMonthDay(Context context, long dateInMillis ) {
        Time time = new Time();
        time.setToNow();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(Utilities.DATE_FORMAT);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM dd");
        String monthDayString = monthDayFormat.format(dateInMillis);
        return monthDayString;
    }



    public static String getFormattedWind(Context context, float windSpeed, float degrees) {

        int windFormat;
        if (Utilities.isMetric(context)) {
            windFormat = R.string.format_wind_kmh;
        }
        else {
            windFormat = R.string.format_wind_mph;
            windSpeed = .621371192237334f * windSpeed;
        }


        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        }
        else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        }
        else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        }
        else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        }
        else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        }
        else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        }
        else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        }
        else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }
        return String.format(context.getString(windFormat), windSpeed, direction);
    }



    public static int getIconResourceForWeatherCondition(int weatherId) {

        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        }
        else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        }
        else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        }
        else if (weatherId == 511) {
            return R.drawable.ic_snow;
        }
        else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        }
        else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        }
        else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog;
        }
        else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm;
        }
        else if (weatherId == 800) {
            return R.drawable.ic_clear;
        }
        else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        }
        else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }



    public static int getArtResourceForWeatherCondition(int weatherId) {

        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm;
        }
        else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        }
        else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain;
        }
        else if (weatherId == 511) {
            return R.drawable.art_snow;
        }
        else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain;
        }
        else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow;
        }
        else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog;
        }
        else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.art_storm;
        }
        else if (weatherId == 800) {
            return R.drawable.art_clear;
        }
        else if (weatherId == 801) {
            return R.drawable.art_light_clouds;
        }
        else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds;
        }
        return -1;
    }
}
