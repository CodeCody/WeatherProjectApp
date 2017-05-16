package com.weather.codyhammond.weatherapp;

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
  "created": "2017-02-03T21:13:59Z",
  "lang": "en-US",
  "results": {
   "channel": {
    "units": {
     "distance": "mi",
     "pressure": "in",
     "speed": "mph",
     "temperature": "F"
    },
    "title": "Yahoo! Weather - Naperville, IL, US",
    "link": "http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-2457000/",
    "description": "Yahoo! Weather for Naperville, IL, US",
    "language": "en-us",
    "lastBuildDate": "Fri, 03 Feb 2017 03:13 PM CST",
    "ttl": "60",
    "location": {
     "city": "Naperville",
     "country": "United States",
     "region": " IL"
    },
    "wind": {
     "chill": "18",
     "direction": "300",
     "speed": "18"
    },
    "atmosphere": {
     "humidity": "36",
     "pressure": "1004.0",
     "rising": "0",
     "visibility": "16.1"
    },
    "astronomy": {
     "sunrise": "7:3 am",
     "sunset": "5:10 pm"
    },

 */