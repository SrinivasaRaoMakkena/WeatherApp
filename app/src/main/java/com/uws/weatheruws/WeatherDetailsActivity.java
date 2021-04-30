package com.uws.weatheruws;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uws.weatheruws.models.current.CurrentWeatherResponse;
import com.uws.weatheruws.models.current.Main;
import com.uws.weatheruws.models.forecast.List;
import com.uws.weatheruws.utils.CommonUtils;
import com.uws.weatheruws.utils.DateUtils;

public class WeatherDetailsActivity extends AppCompatActivity {

    private CurrentWeatherResponse currentWeatherResponse;
    private List weatherForecastResponse;
    private ImageView imageView;
    private TextView city, date, tempTV, tempRangeFTV, tempRangeCTV,tempFeelsLikeTV, descriptionTV, humidityTV, pressureTV, visibilityTV, windSpeedTV, windDirectionTV;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        initializeViews();

        if (getIntent().getSerializableExtra("current") != null) {
            currentWeatherResponse = (CurrentWeatherResponse) getIntent().getSerializableExtra("current");
            setImageView(currentWeatherResponse.getWeather().get(0).getIcon());
            setDate(DateUtils.getFullDate(currentWeatherResponse.getDt()));
            setCity(currentWeatherResponse.getName()+", "+currentWeatherResponse.getSys().getCountry());
            setCurrentTemp(currentWeatherResponse.getMain());
            setWindspeed(currentWeatherResponse.getWind().getSpeed().toString(),currentWeatherResponse.getWind().getDeg().toString());
            setDescription(currentWeatherResponse.getWeather().get(0).getDescription());
            setVisibilityTV(currentWeatherResponse.getVisibility());
        }
        if (getIntent().getSerializableExtra("forecast") != null) {
            weatherForecastResponse = (List) getIntent().getSerializableExtra("forecast");
            cityName = getIntent().getStringExtra("city");
            setImageView(weatherForecastResponse.getWeather().get(0).getIcon());
            setDate(DateUtils.getFullDate(weatherForecastResponse.getDt()));
            setCity(cityName);
            setForecastTemp(weatherForecastResponse.getMain());
            setWindspeed(weatherForecastResponse.getWind().getSpeed().toString(),weatherForecastResponse.getWind().getDeg().toString());
            setDescription(weatherForecastResponse.getWeather().get(0).getDescription());
            setVisibilityTV(weatherForecastResponse.getVisibility());

        }
    }

    private void initializeViews() {
        imageView = findViewById(R.id.imageView);
        city = findViewById(R.id.cityTV);
        date = findViewById(R.id.dateTV);
        tempTV = findViewById(R.id.text2);
        tempRangeFTV = findViewById(R.id.text6);
        tempRangeCTV = findViewById(R.id.text22);
        tempFeelsLikeTV = findViewById(R.id.text4);
        descriptionTV = findViewById(R.id.text8);
        humidityTV = findViewById(R.id.text10);
        pressureTV = findViewById(R.id.text12);
        visibilityTV = findViewById(R.id.text14);
        windSpeedTV = findViewById(R.id.text18);
        windDirectionTV = findViewById(R.id.text20);
    }


    private void setImageView(String icon) {
        String urlString = "https://openweathermap.org/img/w/" + icon + ".png";
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.no_image_available);
        requestOptions.error(R.drawable.no_image_available);
        Glide.with(this)
                .load("https://openweathermap.org/img/w/" + icon + ".png")
                .apply(new RequestOptions().override(2000, 200))
                .apply(requestOptions)
                .into(imageView);
    }

    private void setDate(String time) {
        date.setText(time);
    }

    private void setCity(String time) {
        city.setText(time);
    }

    private void setCurrentTemp(Main weather) {
        setTemp(weather.getTemp(), weather.getFeelsLike(), weather.getTempMin(), weather.getTempMax(), weather.getHumidity(), weather.getPressure());
    }

    private void setTemp(Double temp, Double feelsLike, Double tempMin, Double tempMax, Integer humidity, Integer pressure) {
        tempTV.setText(CommonUtils.kelvinToFahrenheit(temp) + "/"+CommonUtils.kelvinToCelcius(temp));
        tempFeelsLikeTV.setText(CommonUtils.kelvinToFahrenheit(feelsLike) + "/"+CommonUtils.kelvinToCelcius(feelsLike));
        tempRangeFTV.setText(CommonUtils.kelvinToFahrenheit(tempMin) + " - " + CommonUtils.kelvinToFahrenheit(tempMax));
        tempRangeCTV.setText(CommonUtils.kelvinToCelcius(tempMin) + " - " + CommonUtils.kelvinToCelcius(tempMax));
        humidityTV.setText(humidity.toString() +"%");
        pressureTV.setText(pressure.toString()+ " hPa");
    }

    private void setForecastTemp(com.uws.weatheruws.models.forecast.Main weather) {
        setTemp(weather.getTemp(), weather.getFeelsLike(), weather.getTempMin(), weather.getTempMax(), weather.getHumidity(), weather.getPressure());
    }

    private void setDescription(String time) {
        descriptionTV.setText(time);
    }

    private void setWindspeed(String speed,String direction) {
        windSpeedTV.setText(speed+"km/hour");
        windDirectionTV.setText(direction +"°");
    }

    private void setVisibilityTV(int visibility){
        visibilityTV.setText((visibility/1000)+"km");
    }

}
