package com.uws.weatheruws.utils;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String getDate(long dateLong) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date(dateLong);
        return dayOfWeek(date, "EEE") + ", " + getCurrentMonth() + " " + getDateInNumber();
    }

    public static String getFullDate(long dateLong) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date(dateLong);
        return dayOfWeek(date, "EEE") + ", " + getCurrentMonth() + " " + getDateInNumber() + ", " + setTime(dateLong);
    }

    public static void setDate(TextView dateTV) {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd");
        Date date = new Date();
        dateTV.setText("" + dateFormat.format(date)+", "+setTime());
    }

    public static String setTime() {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date();
        return  (" " + dateFormat.format(date).replace("AM", "am").replace("PM","pm"));
    }


    public static String dayOfWeek(Date yourDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date dateFormat = new java.util.Date();
        return sdf.format(dateFormat);
    }

    public static String getCurrentMonth() {
        return getDateString("MMM");
    }



    public static String setTime(long dateLong) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date(dateLong);
        return dateFormat.format(date).toLowerCase();
    }

    public static String getDateString(String mmm) {
        SimpleDateFormat simpleformat = new SimpleDateFormat(mmm);
        return simpleformat.format(new Date());
    }

    public static String getDateInNumber() {
        return getDateString("dd");
    }


    public static String getcurrentDate(String dateformat) {
        return new SimpleDateFormat(dateformat).format(new Date());
    }

    public static String convertDatewithFormat(String rsedate, String originalformat, String targetformat) {
        DateFormat originalFormat = new SimpleDateFormat(originalformat);
        DateFormat targetFormat = new SimpleDateFormat(targetformat);
        Date date = null;
        try {
            date = originalFormat.parse(rsedate);
        } catch (ParseException e) {
            Log.e("Date", e.getMessage());
        }
        return targetFormat.format(date);
    }
}

