package com.itba.hci.domotica;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends MainActivity.GeneralFragment {
    private FavRoutineExpandableListAdapter routineListAdapter;
    private ExpandableListView routineExpListView;
    private ArrayList<String> routineListDataHeader;
    private HashMap<String, Routine> routineListDataChild;

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

        // get the listviews
        ExpandableListView deviceExpandableListView = (ExpandableListView) rootView.findViewById(R.id.deviceExpList);
        ExpandableListView routineExpandableListView = (ExpandableListView) rootView.findViewById(R.id.routineExpList);

        // preparing list data
        prepareListData();
        firstRun = false;

        deviceListAdapter = new FavDeviceExpandableListAdapter(getActivity(), deviceListDataChild);
        routineListAdapter = new FavRoutineExpandableListAdapter(getActivity(), routineListDataChild);

        // listeners for when data in devices or routines changes
        // todo

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
