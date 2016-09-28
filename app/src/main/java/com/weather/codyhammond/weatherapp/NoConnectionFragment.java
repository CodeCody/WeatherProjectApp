package com.weather.codyhammond.weatherapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weather.codyhammond.weatherproject.R;

/**
 * Created by codyhammond on 5/18/16.
 */
public class NoConnectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.no_connection_display,parent,false);

        return view;
    }
}
