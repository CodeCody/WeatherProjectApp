package com.example.codyhammond.weatherproject;

import android.telecom.Call;

import retrofit.Callback;
import retrofit.http.EncodedPath;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by codyhammond on 4/7/16.
 */
public interface WeatherInterface
{
    @GET("/yql")
    void getSearchFeed(@Query(value = "q",encodeValue = false)String option, Callback<searchResponse> response);

    @GET("/yql")
    void getFeed(@Query(value="q",encodeValue = false)String option, Callback<WeatherJSON>response);

}

