package com.weather.codyhammond.weatherproject;

import java.util.ArrayList;

/**
 * Created by codyhammond on 4/7/16.
 */
public class WeatherJSON
{
 Query query;
}

class Query
{
 Results results;
}

class Channel
{
    Location location;
    Item item;
    String title;
    Astronomy astronomy;
    String lastBuildDate;
}
class Location
{
    String city;
    String region;
}
class Results
{
    Channel channel;
    ArrayList<Place>place;

}

class Item
{
   Condition condition;
    ArrayList<Forecast>forecast;
}

class Place
{
    Country country;
    Admin1 admin1;
    Locality locality1;
}
class Locality
{
    String town;
}
class Country
{
    String content;
}

class Admin1
{
    String content;
}
class Condition
{
    String temp="";
    String text="";
}
class Astronomy
{
    StringBuilder sunrise;
    StringBuilder sunset;
}
class Forecast
{
    String date;
    String high;
    String low;
    String text;
    String day;
}
/*
{
 "query": {
  "count": 1,
  "created": "2016-04-08T00:29:31Z",
  "lang": "en-US",
  "results": {
   "channel": {
    "item": {
     "title": "Conditions for Chicago, IL, US at 06:00 PM CDT",
     "lat": "41.884151",
     "long": "-87.632408",
     "link": "http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-2379574/",
     "pubDate": "Thu, 07 Apr 2016 06:00 PM CDT",
     "condition": {
      "code": "26",
      "date": "Thu, 07 Apr 2016 06:00 PM CDT",
      "temp": "40",
      "text": "Cloudy"
     },

 */