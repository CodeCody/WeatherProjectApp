package com.weather.codyhammond.weatherproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 @author codyhammond
 */
public class WeatherFragment extends Fragment implements ViewPager.OnPageChangeListener,UpdateUI
{
    public final static String baseURL="https://query.yahooapis.com/v1/public";
    public String URL="select location,item,lastBuildDate,astronomy from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")";
    public final static String ending="&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    private TextView currentStatus;
    private TextView tmp;
    private TextView location,SunriseText,SunsetText,Forecast_Label;
    private ImageView imageStatus;
    private RecyclerView recyclerView,recyclerView2;
    private ScrollView scrollView;
    private DrawerListener drawerListener;
    private Button ten_day;
    private ProgressBar progressBar;
    private Button five_day;
    private ImageButton addForecast;
    private ImageButton navForecast;
    private ImageView background_image;
    private String city;
    private ForecastAdapter adapter1 = new ForecastAdapter();
    private ForecastAdapter adapter2 = new ForecastAdapter();
    private WeatherRetriever weatherRetriever;
    private Animation background_animation;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        weatherRetriever=new WeatherRetriever();
        weatherRetriever.setFragmentUIListener(this);
        background_animation=AnimationUtils.loadAnimation(getContext(),R.anim.background_image_fade);
        ((MainActivity) getActivity()).getViewPager().addOnPageChangeListener(this);
        drawerListener=(MainActivity)getActivity();
    }

    @SuppressWarnings("NewApi")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.weather_display, parent, false);

        city = getArguments().getString("info");
        weatherRetriever.setLocation(city);
        weatherRetriever.setQuery(URL);

        SunriseText = (TextView) view.findViewById(R.id.sunrise_time);
        SunsetText = (TextView) view.findViewById(R.id.sunset_time);
        Forecast_Label=(TextView)view.findViewById(R.id.forecast_label);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        background_image=(ImageView)view.findViewById(R.id.background_image);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        currentStatus = (TextView) view.findViewById(R.id.condition_textview);
        tmp = (TextView) view.findViewById(R.id.temp_textview);
        imageStatus = (ImageView) view.findViewById(R.id.weather_imageview);
        location = (TextView) view.findViewById(R.id.city_name);
        ten_day = (Button) view.findViewById(R.id.ten_day);
        five_day = (Button) view.findViewById(R.id.five_day);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        recyclerView2.setVisibility(View.GONE);

        five_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                five_day.setTextColor(Color.parseColor("#FFFFFF"));
                ten_day.setTextColor(Color.parseColor("#AFAFAF"));
                recyclerView2.setVisibility(View.GONE);
            }
        });

        ten_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView2.setVisibility(View.VISIBLE);
                ten_day.setTextColor(Color.parseColor("#FFFFFF"));
                five_day.setTextColor(Color.parseColor("#AFAFAF"));
            }
        });

        addForecast = (ImageButton) view.findViewById(R.id.imagebutton);
        navForecast = (ImageButton) view.findViewById(R.id.nav);

        addForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().add(R.id.drawer, new SearchFragment()).addToBackStack(null).commit();
            }
        });

        navForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerListener.openDrawer();
            }
        });

        hideViews();


       return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        try
        {
            weatherRetriever.connect();
        }
        catch (NullPointerException | IllegalStateException exception)
        {
            Log.e("weatherRtrver.connect()",exception.getMessage());
        }

    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.i("Called","Pause");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }
    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        Log.i("Callback","called");
    }

    @Override
    public void onPageSelected(int pos)
    {

    }

    @Override
    public void onPageScrolled(int value,float value2,int value3)
    {

    }

    public static WeatherFragment newFragment(String location)
    {
        Bundle bundle=new Bundle();
        bundle.putString("info",location);

        WeatherFragment fragment= new WeatherFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
        closeDrawer();
        Log.i("Scroll","Scroll Callback");
    }

    @Override
    public void updateUIOnFailure()
    {
        if(getView()==null)
            return;

        location.setText(R.string.not_available);
        hideViews();

    }

    public void hideViews()
    {
        SunriseText.setVisibility(View.GONE);
        SunsetText.setVisibility(View.GONE);
        currentStatus.setVisibility(View.GONE);
        tmp.setVisibility(View.GONE);
        Forecast_Label.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        imageStatus.setImageResource(R.drawable.na);
        recyclerView.setVisibility(View.GONE);
        recyclerView2.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
       // background_image.setVisibility(View.GONE);
    }

    public void showViews()
    {
        SunriseText.setVisibility(View.VISIBLE);
        SunsetText.setVisibility(View.VISIBLE);
        currentStatus.setVisibility(View.VISIBLE);
        tmp.setVisibility(View.VISIBLE);
        Forecast_Label.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void hideUI()
    {
        hideViews();
    }

    @Override
    public void showUI()
    {
        showViews();
    }
    @Override
    public void updateUI()
    {
        if(getView()==null)
            return;

        showViews();
        location.setText(weatherRetriever.getLocation());
        SunriseText.setText(weatherRetriever.getSunrise());
        SunsetText.setText(weatherRetriever.getSunset());
        currentStatus.setText(weatherRetriever.getCondition());
        tmp.setText(weatherRetriever.getTemperature());
        imageStatus.setImageResource(weatherRetriever.getWeatherImage());
        adapter1.setForecastArrayList(weatherRetriever.getFiveDayForecast());
        recyclerView.setAdapter(adapter1);
        adapter2.setForecastArrayList(weatherRetriever.getTenDayForecast());
        recyclerView2.setAdapter(adapter2);
        progressBar.setVisibility(View.GONE);
        background_image.setImageResource(weatherRetriever.getWeatherBackground());
        background_image.startAnimation(background_animation);
    }

    public void closeDrawer()
    {
        drawerListener.closeDrawer();
    }

    private class ForecastHolder extends RecyclerView.ViewHolder
    {
        private TextView high,low,day,date;
        private ImageView condition;
        public ForecastHolder(View itemView)
        {
            super(itemView);
            high=(TextView)itemView.findViewById(R.id.high);
            low=(TextView)itemView.findViewById(R.id.low);
            day=(TextView)itemView.findViewById(R.id.day);

            condition=(ImageView) itemView.findViewById(R.id.condition);
        }

        @SuppressWarnings("NewApi")
        public void bindForecast(Forecast forecast)
        {
            char degree=(char)0x00B0;
            String highforecast=forecast.high+degree;
            String lowforecast=forecast.low+degree;
            high.setText(highforecast);
            low.setText(lowforecast);
            day.setText(forecast.day);

            String w=forecast.text;
            if(WeatherImageCenter.day_weather_image.get(w)!=null)
            condition.setImageDrawable(getResources().getDrawable(WeatherImageCenter.day_weather_image.get(w),getActivity().getTheme()));
            else
                condition.setImageDrawable(getResources().getDrawable(WeatherImageCenter.day_weather_image.get("na"),getActivity().getTheme()));
        }
    }

    private class ForecastAdapter extends RecyclerView.Adapter<ForecastHolder>
    {
        public void setForecastArrayList(List<Forecast>arrayList)
        {
           forecastArrayList=arrayList;
        }

        private List<Forecast>forecastArrayList=new ArrayList<>();

        @Override
        public ForecastHolder onCreateViewHolder(ViewGroup parent,int pos)
        {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item,parent,false);
            return new ForecastHolder(view);
        }

        @Override
        public void onBindViewHolder(ForecastHolder holder,int pos)
        {
            if(forecastArrayList.size()!=0)
            {
                Forecast forecast = forecastArrayList.get(pos);
                holder.bindForecast(forecast);
            }
        }

        @Override
        public int getItemCount()
        {
            return 5;
        }
    }

}

