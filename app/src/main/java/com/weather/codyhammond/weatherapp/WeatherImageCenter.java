package com.weather.codyhammond.weatherapp;

import com.weather.codyhammond.weatherproject.R;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by codyhammond on 5/12/16.
 */
 public class WeatherImageCenter
{
    public static HashMap<String,Integer> day_weather_image=new HashMap<>(),night_weather_image=new HashMap<>();
    public static HashMap<Integer,Integer>day_background_image=new HashMap<>(),night_background_image=new HashMap<>();

    static
    {
        day_weather_image.put("Partly Cloudy", R.drawable.partly_cloudy);
        day_weather_image.put("Storms",R.drawable.rain_lightning);
        day_weather_image.put("Mostly Cloudy",R.drawable.mostly_cloudy);
        day_weather_image.put("Rain",R.drawable.heavy_rain);
        day_weather_image.put("Heavy Rain",R.drawable.heavy_rain);
        day_weather_image.put("Clear",R.drawable.sunny);
        day_weather_image.put("Mostly Clear",R.drawable.sunny);
        day_weather_image.put("Cloudy",R.drawable.cloudy);
        day_weather_image.put("Showers",R.drawable.showers);
        day_weather_image.put("Scattered Thunderstorms",R.drawable.scattered_thunderstorms);
        day_weather_image.put("Scattered Showers",R.drawable.scattered_rain);
        day_weather_image.put("Mostly Sunny",R.drawable.sunny);
        day_weather_image.put("Sunny",R.drawable.sunny);
        day_weather_image.put("Snow",R.drawable.snow);
        day_weather_image.put("na",R.drawable.na);
        day_weather_image.put("Thunderstorms",R.drawable.rain_lightning);
        day_weather_image.put("Breezy",R.drawable.breezy);
        day_weather_image.put("Windy",R.drawable.breezy);
        day_weather_image.put("Rain And Snow",R.drawable.rain_snow_mix);
        day_weather_image.put("Snow Showers",R.drawable.snow);
        day_weather_image.put("Scattered Snow Showers",R.drawable.snow);
        day_weather_image.put("Fog",R.drawable.fog);


        day_background_image.put(R.drawable.sunny,R.drawable.sunny_bkg_converted);
        day_background_image.put(R.drawable.fog,R.drawable.cloudy_background);
        day_background_image.put(R.drawable.partly_cloudy,R.drawable.partly_cloudy_background);
        day_background_image.put(R.drawable.mostly_cloudy,R.drawable.partly_cloudy_background);
        day_background_image.put(R.drawable.cloudy,R.drawable.cloudy_background);
        day_background_image.put(R.drawable.scattered_thunderstorms,R.drawable.storm_background);
        day_background_image.put(R.drawable.scattered_rain,R.drawable.storm_background);
        day_background_image.put(R.drawable.showers,R.drawable.rain_background);
        day_background_image.put(R.drawable.heavy_rain,R.drawable.rain_background);
        day_background_image.put(R.drawable.rain_lightning,R.drawable.rain_background);
        day_background_image.put(R.drawable.breezy,R.drawable.partly_cloudy_background);
        day_background_image.put(R.drawable.freezing_rain,R.drawable.rain_and_snow_background);
        day_background_image.put(R.drawable.rain_snow_mix,R.drawable.rain_and_snow_background);
        day_background_image.put(R.drawable.snow,R.drawable.day_snow_background);
        day_background_image.put(R.drawable.clear_night,R.drawable.clear_night_background);
        day_background_image.put(R.drawable.mostly_cloudy_night,R.drawable.partly_cloudy_night_background);
        day_background_image.put(R.drawable.partly_cloudy_night,R.drawable.partly_cloudy_night_background);
        day_background_image.put(R.drawable.mostly_clear_night,R.drawable.cloudy_night_background);
        day_background_image.put(R.drawable.mostly_cloudy_night,R.drawable.cloudy_night_background);


        night_background_image.put(R.drawable.sunny,R.drawable.sunny_bkg_converted);
        night_background_image.put(R.drawable.fog,R.drawable.cloudy_night_background);
        night_background_image.put(R.drawable.partly_cloudy,R.drawable.partly_cloudy_background);
        night_background_image.put(R.drawable.mostly_cloudy,R.drawable.partly_cloudy_background);
        night_background_image.put(R.drawable.clear_night,R.drawable.clear_night_background);
        night_background_image.put(R.drawable.cloudy,R.drawable.cloudy_night_background);
        night_background_image.put(R.drawable.mostly_cloudy_night,R.drawable.partly_cloudy_night_background);
        night_background_image.put(R.drawable.partly_cloudy_night,R.drawable.partly_cloudy_night_background);
        night_background_image.put(R.drawable.mostly_clear_night,R.drawable.cloudy_night_background);
        night_background_image.put(R.drawable.mostly_cloudy_night,R.drawable.cloudy_night_background);
        night_background_image.put(R.drawable.heavy_rain,R.drawable.rain_background);
        night_background_image.put(R.drawable.showers,R.drawable.rain_background);
        night_background_image.put(R.drawable.rain_lightning,R.drawable.rain_background);
        night_background_image.put(R.drawable.scattered_showers_night,R.drawable.scattered_storms_night_background);
        night_background_image.put(R.drawable.scattered_thunderstorms_night,R.drawable.scattered_storms_night_background);
        night_background_image.put(R.drawable.rain_snow_mix,R.drawable.rain_and_snow_background);
        night_background_image.put(R.drawable.freezing_rain,R.drawable.rain_and_snow_background);
        night_background_image.put(R.drawable.snow,R.drawable.night_snow_background);



        night_weather_image.put("Partly Cloudy",R.drawable.partly_cloudy);
        night_weather_image.put("Mostly Cloudy",R.drawable.mostly_cloudy);
        night_weather_image.put("Clear",R.drawable.sunny);
        night_weather_image.put("Mostly Clear",R.drawable.sunny);
        night_weather_image.put("Scattered Thunderstorms",R.drawable.scattered_thunderstorms);
        night_weather_image.put("Scattered Showers",R.drawable.scattered_rain);
        night_weather_image.put("Mostly Sunny",R.drawable.sunny);
        night_weather_image.put("Sunny",R.drawable.sunny);
        night_weather_image.put("Partly Cloudy",R.drawable.partly_cloudy_night);
        night_weather_image.put("Rain And Snow",R.drawable.rain_snow_mix);
        night_weather_image.put("Clear",R.drawable.clear_night);
        night_weather_image.put("Fair",R.drawable.clear_night);
        night_weather_image.put("Scattered Thunderstorms",R.drawable.scattered_thunderstorms_night);
        night_weather_image.put("Scattered Showers",R.drawable.scattered_showers_night);
        night_weather_image.put("Mostly Clear",R.drawable.mostly_clear_night);
        night_weather_image.put("Mostly Cloudy",R.drawable.mostly_cloudy_night);
        night_weather_image.put("Cloudy",R.drawable.cloudy);
        night_weather_image.put("Storms",R.drawable.rain_lightning);
        night_weather_image.put("Snow",R.drawable.snow);
        night_weather_image.put("Rain",R.drawable.heavy_rain);
        night_weather_image.put("Heavy Rain",R.drawable.heavy_rain);
        night_weather_image.put("Sunny",R.drawable.sunny);
        night_weather_image.put("Thunderstorms",R.drawable.rain_lightning);
        night_weather_image.put("na",R.drawable.na);
        night_weather_image.put("Showers",R.drawable.showers);
        night_weather_image.put("Snow Showers",R.drawable.snow);
        night_weather_image.put("Scattered Snow Showers",R.drawable.snow);
        night_weather_image.put("Breezy",R.drawable.breezy);
    }
}
