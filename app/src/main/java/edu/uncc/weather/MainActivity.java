package edu.uncc.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements CitiesFragment.CitiesFragmentListener,CurrentWeatherFragment.CurrentWeatherInterface {

    public static final String API_KEY = "3331af4cd819bd2fb3b94950195a8cd1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new CitiesFragment())
                .commit();
    }

    @Override
    public void gotoCurrentWeather(DataService.City city) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, CurrentWeatherFragment.newInstance(city))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openCheckForecastPage(String lan, String lon, String name) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView,new WeatherForecastFragment().newInstance(lan,lon,name))
                .addToBackStack(null)
                .commit();
    }
}