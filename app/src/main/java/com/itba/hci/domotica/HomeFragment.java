package com.itba.hci.domotica;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends MainActivity.GeneralFragment {
    private FavRoutineListAdapter routineListAdapter;
    private ExpandableListView routineExpListView;
    private ArrayList<Routine> routineList;

    private FavDeviceExpandableListAdapter deviceListAdapter;
    private ExpandableListView deviceExpListView;
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
        routineList = new ArrayList<>();

        // set textviews
        ((TextView)rootView.findViewById(R.id.device_title)).setText(R.string.tab_text_2);
        ((TextView)rootView.findViewById(R.id.routine_title)).setText(R.string.tab_text_3);

        // get the listviews
        ExpandableListView deviceExpandableListView = (ExpandableListView) rootView.findViewById(R.id.deviceExpList);
        ListView routineListView = (ListView) rootView.findViewById(R.id.routineList);

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

        model.getRoutineList().observe(getActivity(), new Observer<ArrayList<Routine>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Routine> arrayList) {
                routineList = arrayList;
                if(routineListAdapter != null) routineListAdapter.notifyDataSetChanged();
            }
        });

        deviceListAdapter = new FavDeviceExpandableListAdapter(getContext(), deviceListDataChild);
        routineListAdapter = new FavRoutineListAdapter(getContext(), routineList);

        // setting list adapters
        deviceExpandableListView.setAdapter(deviceListAdapter);
        routineListView.setAdapter(routineListAdapter);

        return rootView;
    }
}
