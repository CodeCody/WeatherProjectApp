package com.weather.codyhammond.weatherapp;

import java.util.ArrayList;

/**
 * Created by codyhammond on 4/25/16.
 */
public class searchResponse {

SearchQuery query;
}

class SearchQuery
{
    ResultsSearch results;
}
class ResultsSearch
{
    ArrayList<Channel>channel;
}
