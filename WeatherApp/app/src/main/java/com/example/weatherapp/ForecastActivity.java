package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForecastActivity extends AppCompatActivity {

    private String cityName = null;
    private String countryName = null;
    private RequestQueue queue;
    private float temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        cityName = getIntent().getStringExtra("CITY_NAME_KEY");
        countryName = getIntent().getStringExtra("COUNTRY_NAME_KEY");

        // Setting the city and country data into textview from the intent
        TextView forecastCityNameTextView = findViewById(R.id.forecastCityNameTextView);
        forecastCityNameTextView.setText(cityName);
        TextView forecastCountryNameTextView = findViewById(R.id.forecastCountryNameTextView);
        forecastCountryNameTextView.setText(countryName);

        // Loading weather forecast
        queue = Volley.newRequestQueue(this);
        getForecastForCity();
    }

    public void getForecastForCity(){
        // Getting weather data from openweathermap API(HTTP/JSON)
        // Instantiate the RequestQueue.
        String url ="https://api.openweathermap.org/data/2.5/forecast?q=";
        // Utilizing the city name from the intent in the API to avoid needless location tracking
        String city = cityName;
        url += city;
        url += "&units=metric&appid=82fedafdee8fcb2439e50b777aafaf5e";

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    parseJsonAndUpdateUI(response);
                },
                error -> {
                    //virhe verkosta hakemisessa
                }
        );

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseJsonAndUpdateUI(JSONObject response) {
        String weatherForecastItemString = null;
        String weatherForecastDateString;
        String weatherString;
        String weatherForecastWindString;
        try {
            JSONArray forecastList = response.getJSONArray("list");
            for( int i=0; i<forecastList.length(); i++){
                JSONObject weatherItem = forecastList.getJSONObject(i);
                weatherForecastDateString = weatherItem.getString("dt_txt");
                weatherString = weatherItem.getJSONArray("weather").getJSONObject(0).getString("main");
                weatherForecastWindString = weatherItem.getJSONObject("wind").getString("speed");
                switch (weatherString) {
                    case "Clear":
                        weatherForecastItemString = getString(R.string.clear);
                        break;
                    case "Clouds":
                        weatherForecastItemString = getString(R.string.clouds);
                        break;
                    case "Thunderstorm":
                        weatherForecastItemString = getString(R.string.thunder);
                        break;
                    case "Drizzle":
                        weatherForecastItemString = getString(R.string.drizzle);
                        break;
                    case "Rain":
                        weatherForecastItemString = getString(R.string.rain);
                        break;
                    case "Snow":
                        weatherForecastItemString = getString(R.string.snow);
                        break;
                    case "Mist":
                    case "Fog":
                        weatherForecastItemString = getString(R.string.mist);
                        break;
                    case "Smoke":
                        weatherForecastItemString = getString(R.string.smoke);
                        break;
                    case "Haze":
                        weatherForecastItemString = getString(R.string.haze);
                        break;
                    case "Dust":
                        weatherForecastItemString = getString(R.string.dust);
                        break;
                    case "Sand":
                        weatherForecastItemString = getString(R.string.sand);
                        break;
                    case "Ash":
                        weatherForecastItemString = getString(R.string.ash);
                        break;
                    case "Squall":
                        weatherForecastItemString = getString(R.string.squall);
                        break;
                    case "Tornado":
                        weatherForecastItemString = getString(R.string.tornado);
                        break;
                }
                temperature = (float)weatherItem.getJSONObject("main").getDouble("temp");
                weatherForecastItemString += " " + temperature + " C";
                weatherForecastWindString += " m/s";
                TextView weatherForecastListTextView = findViewById(R.id.weatherForecastListTextView);
                weatherForecastListTextView.append( getString(R.string.time) + ": " + weatherForecastDateString + "\n"+ weatherForecastItemString + "\n" + getString(R.string.wind_speed) + ": " + weatherForecastWindString + "\n\n" );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}