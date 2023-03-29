package edu.uncc.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WeatherForecastFragment extends Fragment {

    private OkHttpClient client = new OkHttpClient();
    String TAG = "deep";

    private ArrayList<ForecastData> weatherForecastArrayList = new ArrayList<>();
    ListView listView;
    WeatherForecastAdapter adapter;

    private static final String ARG_PARAM1 = "lat";
    private static final String ARG_PARAM2 = "lon";
    private static final String ARG_PARAM3 = "header";

    private String lat;
    private String lon;
    private String headerName;


    public WeatherForecastFragment() {
        // Required empty public constructor
    }


    public static WeatherForecastFragment newInstance(String lat, String lon,String header) {
        WeatherForecastFragment fragment = new WeatherForecastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, lat);
        args.putString(ARG_PARAM2, lon);
        args.putString(ARG_PARAM3,header);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lat = getArguments().getString(ARG_PARAM1);
            lon = getArguments().getString(ARG_PARAM2);
            headerName = getArguments().getString(ARG_PARAM3);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loadData(lat,lon);
        View view = inflater.inflate(R.layout.fragment_weather_forecast, container, false);
        TextView cityName = view.findViewById(R.id.textViewCityName);
        cityName.setText(headerName);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void loadData(String lat, String lon){
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid="+MainActivity.API_KEY)
                .build();

        Log.d(TAG, "loadData: https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid="+MainActivity.API_KEY);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String string = response.body().string();
//                    Log.d(TAG, "onResponse: "+string);
                    try {
                        JSONObject json = new JSONObject(string);

                        JSONArray jsonList = json.getJSONArray("list");
                        Log.d(TAG, "onResponse: "+jsonList);
                        for (int i = 0; i < jsonList.length(); i++) {
//
                            JSONObject jsonListObject = jsonList.getJSONObject(0);
                            JSONObject jsonMain = new JSONObject(jsonList.get(i).toString());
                            JSONObject jsonMainObject = jsonMain.getJSONObject("main");
                            JSONArray jsonWeatherArray = jsonMain.getJSONArray("weather");
                            JSONObject jsonWeatherObject = jsonWeatherArray.getJSONObject(0);

                            String date = (String) jsonListObject.get("dt_txt");
                            String temp = jsonMainObject.getString("temp");
                            String max = jsonMainObject.getString("temp_max");
                            String min = jsonMainObject.getString("temp_min");
                            String humidity = jsonMainObject.getString("humidity");
                            String desc = jsonWeatherObject.getString("description").toUpperCase();
                            String icon = jsonWeatherObject.getString("icon");
                            weatherForecastArrayList.add(new ForecastData(date,temp,max,min,humidity,desc,icon));
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView = getActivity().findViewById(R.id.listView);
                                adapter = new WeatherForecastAdapter(getActivity(),R.layout.forecast_row_item,weatherForecastArrayList);
                                listView.setAdapter(adapter);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
}