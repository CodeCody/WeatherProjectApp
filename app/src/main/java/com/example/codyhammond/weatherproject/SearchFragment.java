package com.example.codyhammond.weatherproject;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;

/**
 * Created by codyhammond on 4/21/16.
 */
public class SearchFragment extends Fragment
{
    private SearchView searchView;
    private List<Channel>places=new ArrayList<>();
    private RecyclerView results;
    private ProgressBar search_progress;
  //  select location,item,lastBuildDate,astronomy from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"naperville, il\")";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, final Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.search_layout,parent,false);
        search_progress=(ProgressBar)view.findViewById(R.id.search_progressBar);
        search_progress.setVisibility(View.GONE);
        searchView=(SearchView)view.findViewById(R.id.searchView);

        results=(RecyclerView)view.findViewById(R.id.results);
        results.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        results.setAdapter(new ResultAdapter());
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            private Timer timer=new Timer();
            private final int DELAY=400;
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                search_progress.setVisibility(View.VISIBLE);
                timer.cancel();
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getResults(newText);
                    }
                },DELAY);
                return false;
            }
        });

        return view;
    }

    public void getResults(final String newText)
    {

        final RestAdapter adapter=new RestAdapter.Builder().setEndpoint(WeatherFragment.baseURL).build();
        final WeatherInterface locationSuggestions=adapter.create(WeatherInterface.class);
        //public String URL="select location,item,lastBuildDate,astronomy from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"naperville, il\")";
        String query=String.format("select title from weather.forecast where woeid in (select woeid from geo.places where text=\"%s*\") | unique(field=\"title\")",newText);
        StringBuilder search_query=new StringBuilder();
        search_query.append(Uri.encode(query)).append(WeatherFragment.ending);


        locationSuggestions.getSearchFeed(search_query.toString(), new Callback<searchResponse>() {
            @Override
            public void success(searchResponse searchJSON, Response response) {
                Log.i("Response","Success");
                try {
                    if(searchJSON.query.results.channel!=null)
                        places = searchJSON.query.results.channel;

                    results.getAdapter().notifyDataSetChanged();
                    search_progress.setVisibility(View.GONE);
                }
                catch (NullPointerException NPE)
                {
                    Log.e("NullPointerException",NPE.getMessage());

                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Response","Failure");
                WifiManager wifimanager=(WifiManager)getContext().getSystemService(Context.WIFI_SERVICE);
                if(!wifimanager.isWifiEnabled())
                    Toast.makeText(getActivity(), "Network Unavailable", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class ResultsHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView city,state,country;

        public ResultsHolder(View view)
        {
            super(view);
            view.setOnClickListener(this);
            city=(TextView)view.findViewById(R.id.result_location);

        }

        @Override
        public void onClick(View view)
        {
            MainActivity activity=(MainActivity)getActivity();
            activity.addLocation(city.getText().toString());
            hideSoftKeyboard();
            getActivity().getSupportFragmentManager().popBackStack();
        }


        public void hideSoftKeyboard() {
            if(getActivity().getCurrentFocus()!=null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        }

        public void bindResults(Channel place)
        {
            StringBuilder builder=new StringBuilder();
            try {
              if(place.title!=null)
                  builder.append(place.title.substring(16,place.title.length()));
            }
            catch (NullPointerException NPE)
            {
                Log.e("Exception",NPE.getMessage());
            }

            city.setText(builder.toString());
        }
    }

    class ResultAdapter extends RecyclerView.Adapter<ResultsHolder>
    {

        @Override
        public ResultsHolder onCreateViewHolder(ViewGroup parent,int pos)
        {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.search_results,parent,false);

            return new ResultsHolder(view);
        }
        @Override
        public void onBindViewHolder(ResultsHolder holder,int pos)
        {
            Channel place=places.get(pos);
            holder.bindResults(place);
        }

        @Override
        public int getItemCount()
        {
            return places.size();
        }
    }
}

