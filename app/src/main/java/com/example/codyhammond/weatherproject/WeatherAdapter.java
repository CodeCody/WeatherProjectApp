package com.example.codyhammond.weatherproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by codyhammond on 4/20/16.
 */
public class WeatherAdapter extends FragmentStatePagerAdapter {
    private List<String> locations;
    private List<WeatherFragment> fragmentList;
    private ArrayAdapter<String> locationAdapter;
    private ArrayAdapter<String> removalAdapter;
    private Activity activity;



    public WeatherAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.activity=activity;
        fragmentList = new ArrayList<>();
        locations = new ArrayList<>();
        locationAdapter = new LocationAdapter(R.layout.navigation_items);
        removalAdapter=new RemovalAdapter(R.layout.location_removal_items);
    }

    @Override
    public int getItemPosition(Object object)
    {
        Log.i("Size",String.valueOf(fragmentList.size()));
        WeatherFragment fragment=(WeatherFragment)object;
        int index = fragmentList.indexOf (fragment);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    public void removeCurrentLocation()
    {
        if(MainActivity.geo_flag)
        {
            fragmentList.remove(0);
            notifyDataSetChanged();
        }
    }
    public void setCurrentLocation(String city)
    {

        if(fragmentList.size()==0)
        {
            fragmentList.add(WeatherCurrentFragment.newFragment(city));
            notifyDataSetChanged();
            Log.i("fragment size 0","notifydatasetChanged");
            return;
        }

        if(fragmentList.get(0) instanceof WeatherCurrentFragment)
        {
            fragmentList.set(0,WeatherCurrentFragment.newFragment(city));
        }
        else
        {
            fragmentList.add(0,WeatherCurrentFragment.newFragment(city));
            Log.i("setCurrentLocation()","Adding current location");
        }

       // notifyDataSetChanged();
        //Log.i("Other","notifyDataSetChanged");

    }

    public void deleteItem(int position)
    {
        locations.remove(position);
        locationAdapter.notifyDataSetChanged();
        if(MainActivity.geo_flag)
        fragmentList.remove(position+1);
        else
        fragmentList.remove(position);
        removalAdapter.notifyDataSetChanged();
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(int pos) {
        return fragmentList.get(pos);
    }

    public void setLocations(List<String> savedLocations) {

        if (savedLocations.size() == 0) {
           return;
        }
        Iterator<String> iterator = savedLocations.iterator();
        while (iterator.hasNext()) {
            fragmentList.add(WeatherFragment.newFragment(iterator.next()));
        }

        locations.addAll(savedLocations);
        notifyDataSetChanged();
    }

    public List<String> getLocations() {
        return locations;
    }

    public int addLocation(String city)
    {
        if (locations.indexOf(city) != -1)
            return locations.indexOf(city);

        locations.add(city);
        locationAdapter.notifyDataSetChanged();
        removalAdapter.notifyDataSetChanged();
        fragmentList.add(WeatherFragment.newFragment(city));
        notifyDataSetChanged();
        notifyDataSetChanged();


        return locations.size() - 1;
    }

    public ArrayAdapter<String> getLocationAdapter() {
        return locationAdapter;
    }

    public ArrayAdapter<String> getRemovalAdapter()
    {
        return removalAdapter;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


      class LocationAdapter extends ArrayAdapter<String> {
        private TextView textView;
         private ImageView imageView;

          private boolean flag=false;


        public LocationAdapter( int res_id)
        {
            super(activity, res_id, locations);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup)
        {
            View view = activity.getLayoutInflater().inflate(R.layout.navigation_items,viewGroup,false);
            textView = (TextView) view.findViewById(R.id.location_text);
            imageView =(ImageView)view.findViewById(R.id.location_image);
            if(!flag)
            {
                imageView.setImageResource(R.drawable.ic_place_white);
            }
            else
            {
                imageView.setImageResource(R.drawable.ic_clear_white);
            }
            textView.setText(locations.get(position));

            return view;
        }


          public boolean isInDeleteMode()
          {
              return flag;
          }
    }

    class RemovalAdapter extends ArrayAdapter<String>
    {
        public RemovalAdapter(int resID)
        {
            super(activity,resID,locations);
        }


        @Override
        public View getView(int position,View view,ViewGroup viewGroup)
        {
            view=activity.getLayoutInflater().inflate(R.layout.location_removal_items,viewGroup,false);
            TextView textView=(TextView)view.findViewById(R.id.location_removal_text);
            textView.setText(locations.get(position));

            return view;
        }
    }
}