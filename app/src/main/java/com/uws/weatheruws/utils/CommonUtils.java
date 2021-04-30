package com.uws.weatheruws.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.uws.weatheruws.R;

import java.text.DecimalFormat;

public class CommonUtils {
    public static void showCustomToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.rounded_corners_blue);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setText(message);
        text.setTextColor(Color.parseColor("#000000"));
        text.setPadding(32, 16, 32, 16);
        /*Here you can do anything with above textview like text.setTextColor(Color.parseColor("#000000"));*/
        toast.show();
    }


    public static String startStringWithUpperCase(String desc) {
        return desc.substring(0, 1).toUpperCase() + desc.substring(1);
    }

    public static String getTwoDecimal(double i) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(i);
    }


    public static String celsiusTempAsString(double f) {
        double numerator1 = f - 32;
        double numerator = numerator1 * 5;
        double devision = numerator / 9;
        return getTwoDecimal(devision) + " °C";
    }

    public static String kelvinToFahrenheit(double kelvin) {
        double numerator1 = kelvin - 273.15;
        double numerator = numerator1 * 9;
        double devision = numerator / 5;
        double total = devision+32;
        return getTwoDecimal(total) + " °F";
    }


    public static String kelvinToCelcius(double kelvin) {
        return getTwoDecimal(kelvin - 273.15) +" °C";
    }

    public static boolean isAlpha(String s) {
        return s != null && s.matches("^[a-zA-Z]*$");
    }

}
