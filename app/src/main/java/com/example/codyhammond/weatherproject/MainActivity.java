package com.example.codyhammond.weatherproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.location.*;
import android.location.Location;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,DrawerListener,LocationToggleListener {

    private ViewPager viewPager;
    private WeatherAdapter adapter;
    public static final String LocationFile = "locations";
    private boolean currentFlag = false;
    private GoogleApiClient GoogleClient;
    private ListView listView;
    private android.location.Location location;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button edit_loc;
    private LinearLayout current_loc_container;
    private static String locationPref="LocationOption";
    public static boolean geo_flag=true;
    private boolean wifi_flag=false;
    private SharedPreferences sharedPreferences;
    private TextView currentLocation;
    private ImageView currentLocationImage;
    private BroadcastReceiver wifiReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_display);
        checkWifi();
        sharedPreferences=getPreferences(MODE_PRIVATE);
        geo_flag=sharedPreferences.getBoolean(locationPref,false);

        adapter = new WeatherAdapter(getSupportFragmentManager(), this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMarginDrawable(R.drawable.margin_drawable);
        viewPager.setPageMargin(10);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);


        currentLocation = (TextView) findViewById(R.id.current_location_label);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                viewPager.setCurrentItem(0, true);
            }
        });
        currentLocationImage=(ImageView)findViewById(R.id.current_location_image);

        listView = (ListView) findViewById(R.id.listview);
        edit_loc = (Button) findViewById(R.id.edit_loc);
        listView.setAdapter(adapter.getLocationAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeatherAdapter.LocationAdapter locationAdapter = (WeatherAdapter.LocationAdapter) listView.getAdapter();
                if (!locationAdapter.isInDeleteMode()) {
                    drawerLayout.closeDrawer(navigationView);
                    if(geo_flag) {
                        setLocation(position + 1);
                    }
                    else
                    {
                        setLocation(position);
                    }

                } else {
                    getWeather().deleteItem(position);
                    getWeather().notifyDataSetChanged();
                    locationAdapter.notifyDataSetChanged();
                }
            }
        });

        edit_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().add(R.id.drawer, new EditLocationFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
                closeDrawer();
            }
        });


        GoogleClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        adapter.setLocations(readLocationsFromFile());

        if(wifi_flag && geo_flag) {
            GoogleClient.connect();
        }
        else if(geo_flag)
        {
            wifiReceiver=new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (info != null && info.isConnected()) {
                        Toast.makeText(MainActivity.this, "Wifi Enabled. Connecting...", Toast.LENGTH_SHORT).show();
                        GoogleClient.connect();
                    }
                }
            };

            IntentFilter intentFilter=new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            registerReceiver(wifiReceiver,intentFilter);
        }
        else
        {
            viewPager.setAdapter(adapter);
        }

        CurrentLocLabelOnOrOff();

    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void CurrentLocLabelOnOrOff()
    {
        if(!geo_flag)
        {
            currentLocationImage.setVisibility(View.GONE);
            currentLocation.setVisibility(View.GONE);
        }
        else
        {
            currentLocationImage.setVisibility(View.VISIBLE);
            currentLocation.setVisibility(View.VISIBLE);
        }
    }
    public void checkWifi()
    {
      WifiManager wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled())
        {
            wifi_flag=true;
            return;
        }
        else
        {
            showWifiDialog();
        }
    }

    public void showWifiDialog()
    {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.turn_on_wifi);
        alertDialog.setMessage("Do you want to enable wifi?");
        AlertDialog.OnClickListener choice=new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which == DialogInterface.BUTTON_POSITIVE)
                {
                    WifiManager wifiManager=(WifiManager)getSystemService(WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    Toast.makeText(MainActivity.this, "Enabling Wifi..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    MainActivity.geo_flag=false;
                }
            }
        };
        alertDialog.setNegativeButton("NO",choice);
        alertDialog.setPositiveButton("YES",choice);



        alertDialog.create().show();
    }

    @Override
    public void openDrawer()
    {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void closeDrawer()
    {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onToggleChange()
    {
        if(geo_flag) {

            adapter.removeCurrentLocation();
            //viewPager.setAdapter(viewPager.getAdapter());
        //    current_loc_container.setVisibility(View.GONE);
            geo_flag=false;
            CurrentLocLabelOnOrOff();
        }
        else
        {
//            current_loc_container.setVisibility(View.VISIBLE);
            GoogleClient.registerConnectionCallbacks(this);
            GoogleClient.connect();
            currentFlag=false;
            geo_flag=true;
            CurrentLocLabelOnOrOff();
        }
    }

    public void writeLocationsToFile()
    {
        JsonWriter writer;
        FileOutputStream fileOutputStream;
        try
        {
            fileOutputStream=openFileOutput(LocationFile, Context.MODE_PRIVATE);
            writer=new JsonWriter(new OutputStreamWriter(fileOutputStream,"UTF-8"));
            writer.setIndent(" ");
            writer.beginArray();

            List<String>locationList=adapter.getLocations();
            for(String locations : locationList)
            {
                writer.beginObject();
                writer.name("city").value(locations);
                writer.endObject();
            }
            writer.endArray();
            writer.close();
        }
        catch (IOException io)
        {
            Log.e("Exception",io.getMessage());
        }
    }

    public List<String> readLocationsFromFile()
    {
        List<String>locations=new ArrayList<>();
        try
        {
            FileInputStream file=openFileInput(LocationFile);
            JsonReader reader=new JsonReader(new InputStreamReader(file,"UTF-8"));
            reader.beginArray();

            while(reader.hasNext())
            {
                reader.beginObject();
                String name=reader.nextName();

                if(name.equals("city"))
                locations.add(reader.nextString());
                reader.endObject();
            }
            reader.endArray();
            reader.close();
        }
        catch (IOException io)
        {
            Log.e("Reading",io.getMessage());
        }

        return locations;
    }

    public WeatherAdapter getWeather()
    {
        return adapter;
    }

    public void addLocation(String city)
    {
        if(geo_flag)
        viewPager.setCurrentItem(adapter.addLocation(city)+1);
        else
            viewPager.setCurrentItem(adapter.addLocation(city));
    }

    public void setLocation(int pos)
    {
        viewPager.setCurrentItem(pos);
    }



    @Override
    protected void onStart()
    {
        super.onStart();

        if(adapter.getCount()==0 && !geo_flag)
        {
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().add(R.id.drawer,new EditLocationFragment()).addToBackStack(null).commit();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        sharedPreferences=getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(locationPref,geo_flag).apply();
        writeLocationsToFile();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        startLocationUpdates();
    }

    private void startLocationUpdates()
    {
        LocationRequest locationRequest=LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY).setInterval(3000);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                GoogleClient, locationRequest, this);
    }
    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
      getSupportFragmentManager().beginTransaction().replace(R.id.drawer,new NoConnectionFragment()).commit();
    }

    @Override
    public void onConnectionSuspended(int code)
    {

    }

    @Override
    public void onLocationChanged(Location changedlocation) {

        StringBuilder builder=new StringBuilder();

        try {
            if(currentFlag)
                return;
            currentFlag=true;
            Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> areas = geo.getFromLocation(changedlocation.getLatitude(), changedlocation.getLongitude(), 0);
            if (!areas.isEmpty()) {
                builder.append(areas.get(0).getLocality());
                builder.append(",").append(areas.get(0).getAdminArea());

               // viewPager.addView(new WeatherCurrentFragment().getView(),0);

            }
        } catch (Exception e) {

            builder.append(R.string.current_location_not_available);
            Log.e("onLocationChanged",e.getMessage());
            e.printStackTrace();
//d            getSupportFragmentManager().beginTransaction().add(R.id.activity_main,new SearchFragment()).commit();
        }
        finally {

            viewPager.removeAllViews();
            adapter.setCurrentLocation(builder.toString());

            viewPager.setAdapter(adapter);

            if(GoogleClient.isConnected()) {
                GoogleClient.unregisterConnectionCallbacks(this);
                removeLocationUpdates();
                GoogleClient.disconnect();
            }
        }
    }

    public void removeLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                GoogleClient, this);
    }

    @Override
    public void onBackPressed()
    {
        if(adapter.getCount()==0 && !geo_flag)
        {
            Toast.makeText(MainActivity.this, "No locations selected.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            super.onBackPressed();
        }
    }

}
