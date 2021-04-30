package com.uws.weatheruws;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RainForecastViewHolder extends RecyclerView.ViewHolder{



    TextView date,temp,weather_condition;
    ImageView weatherConditionIV;

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

    public TextView getTemp() {
        return temp;
    }

    public void setTemp(TextView temp) {
        this.temp = temp;
    }

    public TextView getWeather_condition() {
        return weather_condition;
    }

    public void setWeather_condition(TextView weather_condition) {
        this.weather_condition = weather_condition;
    }

    public ImageView getWeatherConditionIV() {
        return weatherConditionIV;
    }

    public void setWeatherConditionIV(ImageView weatherConditionIV) {
        this.weatherConditionIV = weatherConditionIV;
    }

    public RainForecastViewHolder(View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.day_forecast);
        temp = itemView.findViewById(R.id.temp_forecast);
        weather_condition = itemView.findViewById(R.id.weather_forecast);
        weatherConditionIV = itemView.findViewById(R.id.weather_condition_image);
    }

}
