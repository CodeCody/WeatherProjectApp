package com.weather.codyhammond.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.weather.codyhammond.weatherproject.R;

/**
 * Created by codyhammond on 5/17/16.
 */
public class WeatherCurrentFragment extends WeatherFragment
{

    @Override @SuppressWarnings("NewApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View view=super.onCreateView(inflater,parent,savedInstanceState);
      //  ImageButton refreshButton=(ImageButton)view.findViewById(R.id.refreshButton);
        TextView city_name=(TextView)view.findViewById(R.id.city_name);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.ic_near_me_white_24dp);
        Drawable drawable=new BitmapDrawable(getResources(),bitmap.createScaledBitmap(bitmap,70,70,false));
        city_name.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CurrentWeather","Pressed");
                ((MainActivity)getActivity()).establishConnection();
            }
        });
        return view;
    }



    public static WeatherFragment newFragment(String location)
    {
        Bundle bundle=new Bundle();
        bundle.putString("info",location);
        WeatherFragment fragment= new WeatherCurrentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }



}
