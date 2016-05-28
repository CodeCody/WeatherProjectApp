package com.example.codyhammond.weatherproject;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codyhammond on 5/11/16.
 */
public class EditLocationFragment extends Fragment
{
    private ListView listView;
    private Button add_loc,remove_loc;
    private List<Boolean>selected_list;
    private WeatherAdapter adapter;
    private DataSetObserver dataSetObserver;
    private ToggleButton toggle_current_location;
    private LocationToggleListener locationToggleListener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        locationToggleListener=(MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.edit_locations,parent,false);

        selected_list=new ArrayList<>();
        add_loc=(Button)view.findViewById(R.id.add_loc);
        toggle_current_location=(ToggleButton)view.findViewById(R.id.toggle_button);
        if(MainActivity.geo_flag)
        {
            toggle_current_location.setChecked(MainActivity.geo_flag);
            toggle_current_location.setBackgroundColor(getActivity().getResources().getColor(R.color.toggleOn));
        }
        else
        {
            toggle_current_location.setChecked(MainActivity.geo_flag);
            toggle_current_location.setBackgroundColor(getActivity().getResources().getColor(R.color.toggleOff));
        }
        toggle_current_location.setChecked(MainActivity.geo_flag);
        toggle_current_location.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("Deprecation")
            @Override
            public void onClick(View v) {
               if(toggle_current_location.isChecked())
               {

                   toggle_current_location.setChecked(true);
                   toggle_current_location.setBackgroundColor(getActivity().getResources().getColor(R.color.toggleOn));
                   locationToggleListener.onToggleChange();
               }
                else
               {
                   toggle_current_location.setChecked(false);
                   toggle_current_location.setBackgroundColor(getActivity().getResources().getColor(R.color.toggleOff));
                   locationToggleListener.onToggleChange();

               }
                Log.i("ToggleButton","Clicked");
            }
        });
        remove_loc=(Button)view.findViewById(R.id.removal_button);
        add_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getActivity().getSupportFragmentManager();
                fm.beginTransaction().add(R.id.drawer,new SearchFragment()).addToBackStack(null).commit();
            }
        });

        remove_loc.setVisibility(View.GONE);
        remove_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 for(int i=0; i < selected_list.size();i++)
                 {
                     if(selected_list.get(i))
                     {
                         adapter.deleteItem(i);
                         selected_list.remove(i);
                         i--;
                     }
                 }
                adapter.notifyDataSetChanged();
                selectionCheck();

            }
        });
        listView=(ListView)view.findViewById(R.id.remove_listView);
        adapter=((MainActivity)getActivity()).getWeather();



        if(this.getView()!=null)
        {
            this.getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK)
                    {
                       if(adapter.getCount()==0)
                       {
                       //    Toast.makeText(EditLocationFragment.this, "No l", Toast.LENGTH_SHORT).show();
                       }
                    }
                    return false;
                }
            });
        }
        listView.setAdapter(adapter.getRemovalAdapter());
        for(int i=0; i < adapter.getCount();i++)
        {
            selected_list.add(false);
        }
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Callback","Click");
                if(selected_list.size()==0)
                    return;

                if(!selected_list.get(position)) {
                    view.setBackgroundColor(Color.RED);
                    selected_list.set(position,true);
                    selectionCheck();
                }
                else
                {
                    view.setBackgroundColor(Color.BLACK);
                    selected_list.set(position,false);
                    selectionCheck();
                }
            }
        });

        dataSetObserver=new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(adapter.getCount() > selected_list.size())
                {
                    Log.i("Observer",String.valueOf(adapter.getCount())+":"+String.valueOf(selected_list.size()));
                    selected_list.add(false);
                }
            }
        };

        adapter.registerDataSetObserver(dataSetObserver);
        return view;
    }


    @Override
    public void onStop()
    {
        super.onStop();
        adapter.unregisterDataSetObserver(dataSetObserver);
    }

    public void selectionCheck()
    {
        for(boolean flag : selected_list)
        {
            if(flag)
            {
                if(!remove_loc.isShown())
                remove_loc.setVisibility(View.VISIBLE);

                return;
            }
        }
        remove_loc.setVisibility(View.GONE);
    }

}
