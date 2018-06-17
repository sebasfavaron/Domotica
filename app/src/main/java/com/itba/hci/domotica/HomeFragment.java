package com.itba.hci.domotica;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends MainActivity.GeneralFragment {
    private FavRoutineExpandableListAdapter routineListAdapter;
    private ExpandableListView deviceExpListView;
    private ArrayList<String> routineListDataHeader;
    private HashMap<String, Routine> routineListDataChild;

    private FavDeviceExpandableListAdapter deviceListAdapter;
    private ExpandableListView routineExpListView;
    private ArrayList<String> deviceListDataHeader;
    private HashMap<String,Device> deviceListDataChild;

    private boolean firstRun;

    public HomeFragment() {
        super();
        firstRun = true;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        deviceListDataHeader = new ArrayList<>();
        deviceListDataChild = new HashMap<>();
        routineListDataHeader = new ArrayList<>();
        routineListDataChild = new HashMap<>();

        // get the listviews
        ExpandableListView deviceExpandableListView = (ExpandableListView) rootView.findViewById(R.id.deviceExpList);
        ExpandableListView routineExpandableListView = (ExpandableListView) rootView.findViewById(R.id.routineExpList);

        // preparing list data
        prepareListData();
        firstRun = false;

        // listeners for when data in devices or routines changes
        MainViewModel model = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        model.getDeviceMap().observe(getActivity(), new Observer<HashMap<String, Device>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, Device> stringDeviceHashMap) {
                // Esto actualiza la lista de dispositivos cada vez que cambia la lista en el ViewModel
                deviceListDataChild = stringDeviceHashMap;
                deviceListDataHeader = new ArrayList<>();
                deviceListDataHeader.addAll(deviceListDataChild.keySet());
                Toast.makeText(getContext(), ((Integer)deviceListDataChild.size()).toString(), Toast.LENGTH_LONG).show();
                if(deviceListAdapter != null) deviceListAdapter.notifyDataSetChanged();
            }
        });

        model.getRoutineMap().observe(getActivity(), new Observer<HashMap<String, Routine>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, Routine> stringDeviceHashMap) {
                // Esto actualiza la lista de dispositivos cada vez que cambia la lista en el ViewModel
                routineListDataChild = stringDeviceHashMap;
                routineListDataHeader = new ArrayList<>();
                routineListDataHeader.addAll(routineListDataChild.keySet());
                if(routineListAdapter != null) routineListAdapter.notifyDataSetChanged();
            }
        });

        deviceListAdapter = new FavDeviceExpandableListAdapter(getContext(), deviceListDataChild);
        routineListAdapter = new FavRoutineExpandableListAdapter(getContext(), routineListDataChild);

        // setting list adapters
        deviceExpandableListView.setAdapter(deviceListAdapter);
        routineExpandableListView.setAdapter(routineListAdapter);



        return rootView;
    }

    private void prepareListData() {
        //if(!firstRun) return; //patch a un problema de ejecucion multiple de onCreateView

        // igual que en DeviceFragment pero con favoritos (filtrar con el meta)

    }


}
