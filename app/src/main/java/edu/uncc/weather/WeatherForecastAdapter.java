package edu.uncc.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherForecastAdapter extends ArrayAdapter<ForecastData> {

    public WeatherForecastAdapter(@NonNull Context context, int resource, @NonNull List<ForecastData> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.forecast_row_item,parent,false);
        }

        ForecastData forecastData = getItem(position);

        TextView textViewDateTime = convertView.findViewById(R.id.textViewDateTime);
        TextView textViewTemp = convertView.findViewById(R.id.textViewTemp);
        TextView textViewTempMax = convertView.findViewById(R.id.textViewTempMax);
        TextView textViewTempMin = convertView.findViewById(R.id.textViewTempMin);
        TextView textViewHumidity = convertView.findViewById(R.id.textViewHumidity);
        TextView textViewDesc = convertView.findViewById(R.id.textViewDesc);
        ImageView imageViewWeatherIcon = convertView.findViewById(R.id.imageViewWeatherIcon);

        textViewDateTime.setText(forecastData.getDate());
        textViewTemp.setText(forecastData.getTemp()+"F");
        textViewTempMax.setText("Max: "+forecastData.getMax()+"F");
        textViewTempMin.setText("Min: "+forecastData.getMin()+"F");
        textViewHumidity.setText("Humidity: "+forecastData.getHumidity()+"%");
        textViewDesc.setText(forecastData.getDescription());
        Picasso.get().load("https://openweathermap.org/img/wn/"+forecastData.getIcon()+".png").into(imageViewWeatherIcon);


        return convertView;
    }
}
