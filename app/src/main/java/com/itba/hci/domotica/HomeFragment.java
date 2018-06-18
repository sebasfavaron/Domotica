package com.itba.hci.domotica;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
    private ListView routineListView;
    private ArrayList<Routine> routineList;

    private FavDeviceExpandableListAdapter deviceListAdapter;
    private ExpandableListView deviceExpListView;
    private ArrayList<String> deviceListDataHeader;
    private HashMap<String,Device> deviceListDataChild;

    // Live data solo para el setup
    private LiveData<HashMap<String, Device>> deviceLiveData;
    private LiveData<ArrayList<Routine>> routineLiveData;

    public HomeFragment() {
        super();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        deviceListDataHeader = new ArrayList<>();
        deviceListDataChild = new HashMap<>();
        routineList = new ArrayList<>();

        // set textviews
        ((TextView)rootView.findViewById(R.id.device_title)).setText(R.string.tab_text_2);
        ((TextView)rootView.findViewById(R.id.routine_title)).setText(R.string.tab_text_3);

        // get the listviews
        deviceExpListView = (ExpandableListView) rootView.findViewById(R.id.deviceExpList);
        routineListView = (ListView) rootView.findViewById(R.id.routineList);

        // listeners for when data in devices or routines changes
        final MainViewModel model = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        deviceLiveData = model.getDeviceMap();
        if(deviceLiveData == null) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.error_message, Snackbar.LENGTH_LONG).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deviceLiveData = model.getDeviceMap();
                    if(deviceLiveData == null) Snackbar.make(rootView, R.string.conection_error, Snackbar.LENGTH_SHORT);
                    else endSetupDevice();
                }
            });
        } else {
            endSetupDevice();
        }

        routineLiveData = model.getRoutineList();
        if(routineLiveData == null){
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.error_message, Snackbar.LENGTH_LONG).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    routineLiveData = model.getRoutineList();
                    if(routineLiveData == null) Snackbar.make(rootView, R.string.conection_error, Snackbar.LENGTH_SHORT);
                    else endSetupRoutine();
                }
            });
        } else {
            endSetupRoutine();
        }

        return rootView;
    }

    private void endSetupDevice(){
        deviceLiveData.observe(getActivity(), new Observer<HashMap<String, Device>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, Device> stringDeviceHashMap) {
                // Esto actualiza la lista de dispositivos cada vez que cambia la lista en el ViewModel
                deviceListDataChild = stringDeviceHashMap;
                deviceListDataHeader = new ArrayList<>();
                deviceListDataHeader.addAll(deviceListDataChild.keySet());
                Toast.makeText(getContext(), ((Integer) deviceListDataChild.size()).toString(), Toast.LENGTH_LONG).show();
                if (deviceListAdapter != null) deviceListAdapter.notifyDataSetChanged();
            }
        });

        deviceListAdapter = new FavDeviceExpandableListAdapter(getActivity(), deviceListDataChild);

        // setting list adapter
        deviceExpListView.setAdapter(deviceListAdapter);
    }

    private void endSetupRoutine(){
        routineLiveData.observe(getActivity(), new Observer<ArrayList<Routine>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Routine> arrayList) {
                routineList = arrayList;
                if(routineListAdapter != null) routineListAdapter.notifyDataSetChanged();
            }
        });

        routineListAdapter = new FavRoutineListAdapter(getActivity(), routineList);

        // setting list adapter
        routineListView.setAdapter(routineListAdapter);
    }
}
