package com.example.weatherapp;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    private TextView mWeatherDescriptionTextView;
    private TextView mTemperatureTextView;
    private TextView mWindTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private TextView mCityNameTextView;
    private TextView mCountryNameTextView;
    private ImageView mWeatherIconImageView;

    private FusedLocationProviderClient client;
    private Double lat = null;
    private Double lon = null;

    private String city = "";
    private String country = "";

    private float temperature;
    private float windSpeed;
    private String weather = "";
    private float imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Requesting perms for gps
        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);

        queue = Volley.newRequestQueue(this);
        mLatitudeTextView = findViewById(R.id.latitudeTextView);
        mLongitudeTextView = findViewById(R.id.longitudeTextView);
        mCityNameTextView = findViewById(R.id.cityNameTextView);
        mCountryNameTextView = findViewById(R.id.countryNameTextView);
        mWeatherDescriptionTextView = findViewById(R.id.weatherDescriptionTextView);
        mTemperatureTextView = findViewById(R.id.temperatureTextView);
        mWindTextView = findViewById(R.id.windTextView);
        mWeatherIconImageView = findViewById(R.id.weatherIconImageView);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                // Check if location is null
                if(location!=null){
                    double latitude = location.getLatitude();
                    lat = latitude;
                    TextView latitudeView = findViewById(R.id.latitudeTextView);
                    latitudeView.setText(lat.toString());
                    double longitude = location.getLongitude();
                    lon = longitude;
                    TextView longitudeView = findViewById(R.id.longitudeTextView);
                    longitudeView.setText(lon.toString());
                }
                else {
                    TextView textView = findViewById(R.id.cityNameTextView);
                    textView.setText(R.string.location_error);
                }
            }
        });

        if (savedInstanceState != null) {
            // Bundle data
            String logLatitude = savedInstanceState.getString("LOG_LATITUDE_DATA", "No data");
            mLatitudeTextView.setText("");
            mLatitudeTextView.setText(logLatitude);
            String logLongitude = savedInstanceState.getString("LOG_LONGITUDE_DATA", "No data");
            mLongitudeTextView.setText("");
            mLongitudeTextView.setText(logLongitude);
            String logCity = savedInstanceState.getString("LOG_CITY_DATA", "No data");
            mCityNameTextView.setText("");
            mCityNameTextView.setText(logCity);
            String logCountry = savedInstanceState.getString("LOG_COUNTRY_DATA", "No data");
            mCountryNameTextView.setText("");
            mCountryNameTextView.setText(logCountry);
            String logWeather = savedInstanceState.getString("LOG_WEATHER_DATA", "No data");
            mWeatherDescriptionTextView.setText("");
            mWeatherDescriptionTextView.setText(logWeather);
            String logTemperature = savedInstanceState.getString("LOG_TEMPERATURE_DATA", "No data");
            mTemperatureTextView.setText("");
            mTemperatureTextView.setText(logTemperature);
            String logWindSpeed = savedInstanceState.getString("LOG_WIND_DATA", "No data");
            mWindTextView.setText("");
            mWindTextView.setText(logWindSpeed);
            float logImage = savedInstanceState.getFloat("LOG_IMAGE_DATA");
            //using logged image id to set the image
            switch ((int) logImage)
            {
                case 1:
                    mWeatherIconImageView.setImageResource(R.drawable.clear_icon);
                    imageId = 1;
                    break;
                case 2:
                    mWeatherIconImageView.setImageResource(R.drawable.clouds_icon);
                    imageId = 2;
                    break;
                case 3:
                    mWeatherIconImageView.setImageResource(R.drawable.thunder_icon);
                    imageId = 3;
                    break;
                case 4:
                    mWeatherIconImageView.setImageResource(R.drawable.rain_icon);
                    imageId = 4;
                    break;
                case 5:
                    mWeatherIconImageView.setImageResource(R.drawable.rain_icon);
                    imageId = 5;
                    break;
                case 6:
                    mWeatherIconImageView.setImageResource(R.drawable.snow_icon);
                    imageId = 6;
                    break;
                case 7:
                    mWeatherIconImageView.setImageResource(R.drawable.mist_icon);
                    imageId = 7;
                    break;
                case 8:
                    mWeatherIconImageView.setImageResource(R.drawable.mist_icon);
                    imageId = 8;
                    break;
                case 9:
                    mWeatherIconImageView.setImageResource(R.drawable.mist_icon);
                    imageId = 9;
                    break;
                case 10:
                    mWeatherIconImageView.setImageResource(R.drawable.mist_icon);
                    imageId = 10;
                    break;
                case 11:
                    mWeatherIconImageView.setImageResource(R.drawable.mist_icon);
                    imageId = 11;
                    break;
                case 12:
                    mWeatherIconImageView.setImageResource(R.drawable.mist_icon);
                    imageId = 12;
                    break;
                case 13:
                    mWeatherIconImageView.setImageResource(R.drawable.mist_icon);
                    imageId = 13;
                    break;
                case 14:
                    mWeatherIconImageView.setImageResource(R.drawable.mist_icon);
                    imageId = 14;
                    break;
            }
        }
    }
    // Permission request to track location
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    // Gets weather data by using location
    public void getWeatherData(View view){

        // Utilizing Geocoder
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Getting City and Country names
        String cityName = addresses.get(0).getLocality();
        mCityNameTextView.setText(cityName);
        city = cityName;
        String countryName = addresses.get(0).getCountryName();
        mCountryNameTextView.setText(countryName);
        country = countryName;

        // Updating Lat and Lon textViews on update
        mLatitudeTextView.setText(lat.toString());
        mLongitudeTextView.setText(lon.toString());

        // Getting weather data from openweathermap.org API(HTTP/JSON)
        String url ="https://api.openweathermap.org/data/2.5/weather?q=";
        String city = cityName;
        url += city;
        url += "&units=metric&appid=82fedafdee8fcb2439e50b777aafaf5e";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Showing www.google.com response as a toast
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                    parseJsonAndUpdateUI( response);
                },
                error -> {
                    //error with internet
                }
        );
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void parseJsonAndUpdateUI(String response) {
        try {
            JSONObject rootObject = new JSONObject(response);
            temperature = (float)rootObject.getJSONObject("main").getDouble("temp");
            windSpeed = (float)rootObject.getJSONObject("wind").getDouble("speed");
            weather = rootObject.getJSONArray("weather").getJSONObject(0).getString("main");

            // Data print
            TextView weatherDescriptionTextView = findViewById(R.id.weatherDescriptionTextView);
            weatherDescriptionTextView.setText( weather );
            ImageView weatherIcon = findViewById(R.id.weatherIconImageView);
            // Switch case is way better than if or else spam
            switch (weather) {
                case "Clear":
                    // clouds icon
                    weatherIcon.setImageResource(R.drawable.clear_icon);
                    weatherDescriptionTextView.setText(R.string.clear);
                    imageId = 1;
                    break;
                case "Clouds":
                    weatherIcon.setImageResource(R.drawable.clouds_icon);
                    weatherDescriptionTextView.setText(R.string.clouds);
                    imageId = 2;
                    break;
                case "Thunderstorm":
                    weatherIcon.setImageResource(R.drawable.thunder_icon);
                    weatherDescriptionTextView.setText(R.string.thunder);
                    imageId = 3;
                    break;
                case "Drizzle":
                    weatherIcon.setImageResource(R.drawable.rain_icon);
                    weatherDescriptionTextView.setText(R.string.drizzle);
                    imageId = 4;
                    break;
                case "Rain":
                    weatherIcon.setImageResource(R.drawable.rain_icon);
                    weatherDescriptionTextView.setText(R.string.rain);
                    imageId = 5;
                    break;
                case "Snow":
                    weatherIcon.setImageResource(R.drawable.snow_icon);
                    weatherDescriptionTextView.setText(R.string.snow);
                    imageId = 6;
                    break;
                case "Mist":
                case "Fog":
                    weatherIcon.setImageResource(R.drawable.mist_icon);
                    weatherDescriptionTextView.setText(R.string.mist);
                    imageId = 7;
                    break;
                case "Smoke":
                    weatherIcon.setImageResource(R.drawable.mist_icon);
                    weatherDescriptionTextView.setText(R.string.smoke);
                    imageId = 8;
                    break;
                case "Haze":
                    weatherIcon.setImageResource(R.drawable.mist_icon);
                    weatherDescriptionTextView.setText(R.string.haze);
                    imageId = 9;
                    break;
                case "Dust":
                    weatherIcon.setImageResource(R.drawable.mist_icon);
                    weatherDescriptionTextView.setText(R.string.dust);
                    imageId = 10;
                    break;
                case "Sand":
                    weatherIcon.setImageResource(R.drawable.mist_icon);
                    weatherDescriptionTextView.setText(R.string.sand);
                    imageId = 11;
                    break;
                case "Ash":
                    weatherIcon.setImageResource(R.drawable.mist_icon);
                    weatherDescriptionTextView.setText(R.string.ash);
                    imageId = 12;
                    break;
                case "Squall":
                    weatherIcon.setImageResource(R.drawable.mist_icon);
                    weatherDescriptionTextView.setText(R.string.squall);
                    imageId = 13;
                    break;
                case "Tornado":
                    weatherIcon.setImageResource(R.drawable.mist_icon);
                    weatherDescriptionTextView.setText(R.string.tornado);
                    imageId = 14;
                    break;
            }

            TextView temperatureTextView = findViewById(R.id.temperatureTextView);
            temperatureTextView.setText( temperature + " " + getString(R.string.c) );
            TextView windTextView = findViewById(R.id.windTextView);
            windTextView.setText( windSpeed + " " + getString(R.string.m_s));

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void openForecast(View view){
        // Opens the forecast activity
        getWeatherData(view);
        Intent openForecast = new Intent(this, ForecastActivity.class);
        openForecast.putExtra("CITY_NAME_KEY", city);
        openForecast.putExtra("COUNTRY_NAME_KEY", country);
        startActivity( openForecast );
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Saving the activity
        String logLatitude = mLatitudeTextView.getText().toString();
        outState.putString("LOG_LATITUDE_DATA", logLatitude);
        String logLongitude = mLongitudeTextView.getText().toString();
        outState.putString("LOG_LONGITUDE_DATA", logLongitude);
        String logCity = mCityNameTextView.getText().toString();
        outState.putString("LOG_CITY_DATA", logCity);
        String logCountry = mCountryNameTextView.getText().toString();
        outState.putString("LOG_COUNTRY_DATA", logCountry);
        String logWeather = mWeatherDescriptionTextView.getText().toString();
        outState.putString("LOG_WEATHER_DATA", logWeather);
        String logTemperature = mTemperatureTextView.getText().toString();
        outState.putString("LOG_TEMPERATURE_DATA", logTemperature);
        String logWindSpeed = mWindTextView.getText().toString();
        outState.putString("LOG_WIND_DATA", logWindSpeed);
        float logImage = imageId;
        outState.putFloat("LOG_IMAGE_DATA", logImage);
    }

}