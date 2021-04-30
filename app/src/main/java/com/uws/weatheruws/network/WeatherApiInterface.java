package com.uws.weatheruws.network;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
http://api.openweathermap.org/data/2.5/weather?lat=-27.2046&lon=77.4977&appid=87908332eaa2c8c84991c142da8db13c

api.openweathermap.org/data/2.5/weather?q=Irving&appid=87908332eaa2c8c84991c142da8db13c

https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&exclude=hourly,minutely&appid=87908332eaa2c8c84991c142da8db13c

https://openweathermap.org/api/one-call-api (Daily climate data)

http://api.openweathermap.org/data/2.5/forecast?q=Irving&appid=87908332eaa2c8c84991c142da8db13c
 */
public interface WeatherApiInterface {

    @GET("data/2.5/weather")
    Call<JsonObject> getWeatherDetailsWithLocation(@Query("q") String location, @Query("appid") String appId);

    @GET("data/2.5/forecast")
    Call<JsonObject> getWeatherForecastDetailsWithLocation(@Query("q") String location, @Query("appid") String appId);
}
