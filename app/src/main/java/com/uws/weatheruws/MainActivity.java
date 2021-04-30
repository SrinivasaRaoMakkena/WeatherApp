package com.uws.weatheruws;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uws.weatheruws.models.current.CurrentWeatherResponse;
import com.uws.weatheruws.models.forecast.WeatherForecastResponse;
import com.uws.weatheruws.network.RetrofitClient;
import com.uws.weatheruws.services.TrackGPS;
import com.uws.weatheruws.utils.CommonUtils;
import com.uws.weatheruws.utils.Constants;
import com.uws.weatheruws.utils.CustomProgressDialog;
import com.uws.weatheruws.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RainForecastAdapter.OnItemClickListener {


    private static final int PERMISSION_REQUEST_CODE = 200;

    private EditText location;
    private TextView temperature;
    private TextView weather_condition;
    private TextView city;
    private TextView time;
    private ConstraintLayout imageLayout;
    private CardView currentWeatherCard;
    private TrackGPS gps;

    private CurrentWeatherResponse currentWeatherResponse;
    private WeatherForecastResponse weatherForecastResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        location = findViewById(R.id.locationET);
        temperature = findViewById(R.id.tempTV);
        weather_condition = findViewById(R.id.weatherConditionTV);
        imageLayout = findViewById(R.id.imageLayout);
        city = findViewById(R.id.dateTV);
        time = findViewById(R.id.timeTV);
        currentWeatherCard = findViewById(R.id.currentWeatherCard);


        final String[] searchedLocation = {location.getText().toString()};
        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 2) {
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> {
                        if (CommonUtils.isAlpha(editable.toString())) {
                            fetchLocationWeatherDetails(location.getText().toString());
                            fetchWeatherForecastDetails(location.getText().toString());
                            searchedLocation[0] = editable.toString();
                        } else {
                            CommonUtils.showCustomToast(MainActivity.this, "Please enter valid city name");
                        }
                    }, 3000);
                }
            }
        });

        if (!checkPermission()) {
            requestPermission();
        }

        gps = new TrackGPS(MainActivity.this);

        try {
            if (gps.canGetLocation()) {
                String city = getAddressFromCoordiantes(gps.getLatitude(), gps.getLongitude());
                fetchWeatherForecastDetails(city);
                fetchLocationWeatherDetails(city);
            } else {
                gps.showAlert();
            }
        }catch (Exception e){
            CommonUtils.showCustomToast(this,"Something went wrong, Please try again later!");
        }

        currentWeatherCard.setOnClickListener(view -> startNextActivity(currentWeatherResponse));

    }


    private void fetchLocationWeatherDetails(String locationName) {
        CustomProgressDialog.showProgressBarDialog(this, getResources().getString(R.string.loader_text));
        RetrofitClient.getWeatherDetails(Constants.WEATHER_BASE_URL).getWeatherDetailsWithLocation(locationName, Constants.OPEN_WEATHER_APP_KEY)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject responseObj = response.body();
                        if (response.body() != null) {
                            setUI(responseObj);
                            CustomProgressDialog.hideProgressbarDialog();
                        } else {
                            setErrorMessage(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        String error = t.getLocalizedMessage();
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        CustomProgressDialog.hideProgressbarDialog();
                        fetchLocationWeatherDetails("Glassgow");
                    }
                });
    }

    private void fetchWeatherForecastDetails(String locationName) {
        RetrofitClient.getWeatherDetails(Constants.WEATHER_BASE_URL).getWeatherForecastDetailsWithLocation(locationName, Constants.OPEN_WEATHER_APP_KEY)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject responseObj = response.body();
                        if (response.body() != null) {
                            Gson gson = new Gson();
                            WeatherForecastResponse currentWeatherResponse = gson.fromJson(responseObj, WeatherForecastResponse.class);
                            RainForecastAdapter adapter = new RainForecastAdapter(currentWeatherResponse.getList(), MainActivity.this);
                            RecyclerView recyclerView = findViewById(R.id.list_view);
                            LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                            recyclerView.setLayoutManager(manager);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            setUIWIthFutureAPI(responseObj);
                        } else {
                            setErrorMessage(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        String error = t.getLocalizedMessage();
                        //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        fetchWeatherForecastDetails("Glassgow");
                    }
                });
    }


    private void setUIWIthFutureAPI(JsonObject responseObj) {
        try {
            Gson gson = new Gson();
            weatherForecastResponse = gson.fromJson(responseObj, WeatherForecastResponse.class);
        } catch (Exception e) {
            Log.e("Weather", e.getMessage());
        }
    }

    private void setUI(JsonObject responseObj) {
        try {
            Gson gson = new Gson();
            currentWeatherResponse = gson.fromJson(responseObj, CurrentWeatherResponse.class);
            currentWeatherResponse.getClouds();
            weather_condition.setText(CommonUtils.startStringWithUpperCase(currentWeatherResponse.getWeather().get(0).getDescription()));
            temperature.setText(CommonUtils.kelvinToFahrenheit(currentWeatherResponse.getMain().getTemp()) + "/" + CommonUtils.kelvinToCelcius(currentWeatherResponse.getMain().getTemp()));
            city.setText(currentWeatherResponse.getName() + ", " + currentWeatherResponse.getSys().getCountry());
            DateUtils.setDate(time);
        } catch (Exception e) {
            Log.e("Weather", e.getMessage());
        }
    }

    private void setErrorMessage(Response<JsonObject> response) {
        if (response.errorBody() != null) {
            CustomProgressDialog.hideProgressbarDialog();
            try {
                JSONObject object = new JSONObject(response.errorBody().string());
                CommonUtils.showCustomToast(getApplicationContext(), object.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermission();
                        }
                    }
                }
        }
    }

    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_CODE);
    }


    public String getAddressFromCoordiantes(double lat, double lng) {
        String city = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            city = addresses.get(0).getLocality();
        } catch (IOException e) {
            CommonUtils.showCustomToast(this, e.getMessage());
        }
        return city;
    }

    private void startNextActivity(CurrentWeatherResponse response) {
        Intent intent = new Intent(this, WeatherDetailsActivity.class);
        if (response != null)
            intent.putExtra("current", response);
        startActivity(intent);
    }

    @Override
    public void onItemClick(com.uws.weatheruws.models.forecast.List forecastWeather) {
        Intent intent = new Intent(this, WeatherDetailsActivity.class);
        intent.putExtra("forecast", forecastWeather);
        intent.putExtra("city", weatherForecastResponse.getCity().getName() + ", " + weatherForecastResponse.getCity().getCountry());
        startActivity(intent);
    }
}
