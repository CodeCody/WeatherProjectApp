package com.example.codyhammond.weatherproject;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by codyhammond on 5/12/16.
 */
 public class WeatherImageCenter
{
    public static Hashtable<String,Integer>day_weather_image=new Hashtable<>(),night_weather_image=new Hashtable<>();

    static
    {
        day_weather_image.put("Partly Cloudy",R.drawable.partly_cloudy);
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
        night_weather_image.put("Breezy",R.drawable.breezy);
    }
}
