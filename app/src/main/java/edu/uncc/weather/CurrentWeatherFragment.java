package edu.uncc.weather;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.uncc.weather.databinding.FragmentCurrentWeatherBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CurrentWeatherFragment extends Fragment {
    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";
    private DataService.City mCity;
    FragmentCurrentWeatherBinding binding;
    String TAG = "deep";

    private OkHttpClient client = new OkHttpClient();

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    public static CurrentWeatherFragment newInstance(DataService.City city) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (DataService.City) getArguments().getSerializable(ARG_PARAM_CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Current Weather");
        updateData(String.valueOf(mCity.getLat()),String.valueOf(mCity.getLon()));
        binding.buttonCheckForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWeatherListener.openCheckForecastPage(String.valueOf(mCity.getLat()),String.valueOf(mCity.getLon()),String.valueOf(mCity.getCity()+","+mCity.getCountry()));
            }
        });



    }


    public void updateData(String lat, String lon){
        HttpUrl httpUrl = HttpUrl.parse("https://api.openweathermap.org/data/2.5/weather")
                .newBuilder().build();

        FormBody formBody = new FormBody.Builder()
                .add("lat",lat)
                .add("lon",lon)
                .add("appid","3331af4cd819bd2fb3b94950195a8cd1")
                .build();

        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid="+MainActivity.API_KEY+"&units=imperial")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    try {
                        String string = response.body().string();

                        JSONObject json = new JSONObject(string);


                        JSONObject jsonObjectMain = json.getJSONObject("main");
                        JSONObject jsonObjectWind = json.getJSONObject("wind");
                        JSONObject jsonObjectCloud = json.getJSONObject("clouds");
                        JSONArray jsonObjectWeather = json.getJSONArray("weather");
                        JSONObject jsonObjectSys = json.getJSONObject("sys");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView temp = binding.textViewTemp;
                                TextView tempMax = binding.textViewTempMax;
                                TextView tempMin = binding.textViewTempMin;
                                TextView description = binding.textViewDesc;
                                TextView humidity = binding.textViewHumidity;
                                TextView wind = binding.textViewWindSpeed;
                                TextView wingDegree = binding.textViewWindDegree;
                                TextView cloud = binding.textViewCloudiness;
                                TextView cityName = binding.textViewCityName;
                                ImageView weatherIcon = binding.imageViewWeatherIcon;

                                try {
                                    temp.setText(jsonObjectMain.get("temp").toString() + " F");
                                    tempMax.setText(jsonObjectMain.get("temp_max").toString() + " F");
                                    tempMin.setText(jsonObjectMain.get("temp_min").toString() + " F");
                                    description.setText(jsonObjectWeather.getJSONObject(0).get("description").toString().toUpperCase());
                                    humidity.setText(jsonObjectMain.get("humidity").toString());
                                    wind.setText(jsonObjectWind.get("speed").toString() + " %");
                                    wingDegree.setText(jsonObjectWind.get("deg").toString() + " miles/hr");
                                    cloud.setText(jsonObjectCloud.get("all").toString() + " %");
//                                    cityName.setText(json.get("name")+","+jsonObjectSys.get("country").toString());
                                    cityName.setText(String.valueOf(mCity.getCity()+","+mCity.getCountry()));
                                    Picasso.get().load("https://openweathermap.org/img/wn/"+jsonObjectWeather.getJSONObject(0).get("icon")+".png").into(weatherIcon);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        currentWeatherListener = (CurrentWeatherInterface) context;
    }

    CurrentWeatherInterface currentWeatherListener;

    interface CurrentWeatherInterface{
        void openCheckForecastPage(String lan,String lon,String name);
    }
}