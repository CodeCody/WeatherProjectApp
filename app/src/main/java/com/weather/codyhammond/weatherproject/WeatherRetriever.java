package com.weather.codyhammond.weatherproject;

import android.net.Uri;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by codyhammond on 5/18/16.
 */
public class WeatherRetriever
{
    private final String baseURL="https://query.yahooapis.com/v1/public";
    private final String ending="&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    private RestAdapter restAdapter;
    private String temperature;
    private List<Forecast>five_day_list=new ArrayList<>(),ten_day_list=new ArrayList<>();
    private UpdateUI FragmentUI=null;
    private String weather_status,sunset,sunrise,query,weather_location,weather_condition,location;
    private Integer weather_id= 0;
    private Integer background_image_id=0;
    private final char degree=(char)0x00b0;

    private WeatherInterface weatherInterface;

    public WeatherRetriever()
    {
        restAdapter=new RestAdapter.Builder().setEndpoint(baseURL).build();
        weatherInterface=restAdapter.create(WeatherInterface.class);
    }


    public void setQuery(String query)
    {
      this.query=query;
    }

    public void setLocation(String weather_location)
    {
        this.weather_location=weather_location;
    }

    public void setFragmentUIListener(UpdateUI ui)
    {
        FragmentUI=ui;
    }

    public void connect() throws NullPointerException,IllegalStateException
    {
        if(weather_location==null)
        {
            Log.i("WeatherRtrvr.connect()","weather_location==null");

        }
        if(query.length()==0)
        {
            throw new IllegalStateException("No query set,call WeatherRetriever.setQuery(String)");
        }
        if(weather_location==null || weather_location.length()==0)
        {
            throw new IllegalStateException("No location set");
        }
        if(FragmentUI==null)
        {
            throw new IllegalStateException("No listener set,call WeatherRetriever.setListener(UpdateUI)");
        }


        final String completeURL=Uri.encode(String.format(query,weather_location)).concat(ending);
        weatherInterface.getFeed(completeURL,new MyCallBack());
    }

    public String getSunset()
    {
        return sunset;
    }

    public String getTemperature()
    {
        return temperature;
    }
    public String getSunrise()
    {
        return sunrise;
    }

    public String getLocation()
    {
        return location;
    }

    public String getCondition()
    {
        return weather_condition;
    }

    public Integer getWeatherImage()
    {
       return weather_id;
    }

    public Integer getWeatherBackground()
    {
        return background_image_id;
    }

    public List<Forecast> getFiveDayForecast()
    {
        return five_day_list;
    }

    public List<Forecast> getTenDayForecast()
    {
        return ten_day_list;
    }


    public boolean daylight(Matcher Currentmatcher, Matcher SetMatcher, Matcher RiseMatcher)
    {
        if(Currentmatcher.find() && SetMatcher.find() && RiseMatcher.find())
        {
            try {
                if (isTimeBetweenTwoTime(RiseMatcher.group(), SetMatcher.group(), Currentmatcher.group())) {
                    Log.i("In Between","True");
                    return true;
                }
                else{
                    Log.i("In Between",SetMatcher.group()+" "+RiseMatcher.group()+" "+Currentmatcher.group());
                    return false;
                }
            }
            catch (IllegalStateException ISE)
            {
                Log.e("ISE",ISE.getMessage());
            }
        }
        return false;
    }

    public String timeFormatCheck(StringBuilder time)
    {
        String checkTime=time.toString();
        if(checkTime.contains("am") || checkTime.contains("pm"))
        {
            return standardFormatCheck(time);
        }
        else
        {
            return militaryFormatCheck(time);
        }

    }
    public String standardFormatCheck(StringBuilder time)
    {
        if(time.length() > 8)
            return time.toString();

        else if(time.length() == 7)
        {
            if (time.indexOf(":") == 2)
            {
                time.insert(time.indexOf(":")+1,'0');
            }
        }
        else
        {
            time.insert(time.indexOf(":")+1,'0');
        }

        return time.toString().toUpperCase();
    }

    public String militaryFormatCheck(StringBuilder time)
    {

        if(time.length() < 5)
        {
            time.insert(0,"0");
        }

        return time.toString();
    }

    public int convertToMilitaryNum(String time)
    {
        int hours;
        int index=time.indexOf(":");
        hours=Integer.parseInt(time.substring(0,index));

        if(time.contains("AM"))
        {
            hours = (hours + 12) % 12;
        }
        else
        {
            if(hours!=12)
                hours = (hours + 12);
        }

        StringBuilder MilitaryTime;
        MilitaryTime=new StringBuilder();

        Integer TimeNum=Integer.parseInt(MilitaryTime.append(String.valueOf(hours)).append(time.substring(index+1,time.length()-3)).toString());

        return TimeNum;
    }

    public boolean isTimeBetweenTwoTime(String initialTime, String finalTime, String currentTime)
    {
        int sunRisetime=convertToMilitaryNum(initialTime);
        int sunSetTime=convertToMilitaryNum(finalTime);
        int nowTime=convertToMilitaryNum(currentTime);

        return (nowTime > sunRisetime && nowTime < sunSetTime);
    }

    class MyCallBack implements Callback<WeatherJSON>
    {

        @Override
        public void success(WeatherJSON json, Response response)
        {

            try {
                Location location_json = json.query.results.channel.location;
                Condition condition = json.query.results.channel.item.condition;
                Astronomy astronomy = json.query.results.channel.astronomy;
                Item item = json.query.results.channel.item;
                StringBuilder locationBuilder=new StringBuilder(),temperatureBuilder=new StringBuilder();

                locationBuilder.append(location_json.city).append(", ").append(location_json.region);
                location=locationBuilder.toString();
                sunset = timeFormatCheck(astronomy.sunset);
                sunrise = timeFormatCheck(astronomy.sunrise);
                List<Forecast> fullForecast = item.forecast;
                five_day_list = fullForecast.subList(0, fullForecast.size() / 2);
                ten_day_list = fullForecast.subList((fullForecast.size() / 2), fullForecast.size());

                Pattern timePattern = Pattern.compile("\\d{1,2}:\\d{1,2}\\s(?:AM|PM|am|pm)");
                Matcher Currentmatcher, SetMatcher, RiseMatcher;
                Currentmatcher = timePattern.matcher(json.query.results.channel.lastBuildDate);
                SetMatcher = timePattern.matcher(sunset);
                RiseMatcher = timePattern.matcher(sunrise);
                final boolean isDay = daylight(Currentmatcher, SetMatcher, RiseMatcher);


                weather_condition = condition.text;
                temperatureBuilder.append(condition.temp).append(degree);
                temperature=temperatureBuilder.toString();
                if (isDay && WeatherImageCenter.day_weather_image.get(weather_condition) != null) {
                    weather_id = WeatherImageCenter.day_weather_image.get(weather_condition);
                    background_image_id=WeatherImageCenter.day_background_image.get(weather_id);
                } else if (!isDay && WeatherImageCenter.night_weather_image.get(condition.text) != null) {
                    weather_id = WeatherImageCenter.night_weather_image.get(weather_condition);
                    background_image_id=WeatherImageCenter.night_background_image.get(weather_id);
                    if(background_image_id==null)
                        background_image_id=0;
                } else {
                    weather_id = R.drawable.na;
                }
                FragmentUI.updateUI();
            }
            catch (NullPointerException NPE)
            {
                Log.e("MyCallBack",NPE.getMessage());
                FragmentUI.updateUIOnFailure();
            }

        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Log.i("RetroFit Failure",retrofitError.getMessage());
            FragmentUI.updateUIOnFailure();
        }
    }

}

