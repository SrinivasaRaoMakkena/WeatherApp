package com.uws.weatheruws;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uws.weatheruws.models.forecast.List;
import com.uws.weatheruws.utils.CommonUtils;
import com.uws.weatheruws.utils.DateUtils;

public class RainForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private java.util.List<List> listOfForecast;
    private OnItemClickListener listener;

    public RainForecastAdapter(java.util.List<List> items, OnItemClickListener listener) {
        this.listOfForecast = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_forecast, parent, false);
        return new RainForecastViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        RainForecastViewHolder viewHolder = (RainForecastViewHolder) holder;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.no_image_available);
        requestOptions.error(R.drawable.no_image_available);
        viewHolder.getDate().setText(DateUtils.convertDatewithFormat(listOfForecast.get(position).getDtTxt(), "yyyy-MM-dd hh:mm:ss", "MMM dd, hh:mm a"));
        viewHolder.getTemp().setText(CommonUtils.kelvinToFahrenheit(listOfForecast.get(position).getMain().getTemp()) + "/" + CommonUtils.kelvinToCelcius(listOfForecast.get(position).getMain().getTemp()));

        String description = listOfForecast.get(position).getWeather().get(0).getDescription();
        viewHolder.getWeather_condition().setText(CommonUtils.startStringWithUpperCase(description));


        Glide.with(viewHolder.getWeatherConditionIV().getContext())
                .load("https://openweathermap.org/img/w/" + listOfForecast.get(position).getWeather().get(0).getIcon() + ".png")
                .apply(new RequestOptions().override(2000, 200))
                .apply(requestOptions)
                .into(viewHolder.getWeatherConditionIV());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(listOfForecast.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listOfForecast != null)
            return listOfForecast.size();
        return 0;
    }

    interface OnItemClickListener {
        void onItemClick(List forecastWeather);
    }


}
