package com.itba.hci.domotica;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceFragment extends MainActivity.GeneralFragment {
    private DeviceExpandableListAdapter deviceListAdapter;
    private ExpandableListView deviceExpListView;
    private ArrayList<String> deviceListDataHeader;
    private HashMap<String,Device> deviceListDataChild;

    // Live data solo para el setup
    private LiveData<HashMap<String, Device>> deviceLiveData;
    private String requestTag;

    public DeviceFragment() {
        super();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_devices, container, false);

        deviceListDataHeader = new ArrayList<>();
        deviceListDataChild = new HashMap<>();

        // get the listview
        deviceExpListView = (ExpandableListView) rootView.findViewById(R.id.expList);

        // listeners for when data in routines changes
        final MainViewModel model = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        deviceLiveData = model.getDeviceMap();
        if(deviceLiveData == null) {
            Snackbar.make(rootView, R.string.error_message, Snackbar.LENGTH_LONG).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deviceLiveData = model.getDeviceMap();
                    if(deviceLiveData == null) Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.conection_error, Snackbar.LENGTH_SHORT);
                    else endSetup();
                }
            });
        } else {
            endSetup();
        }

        return rootView;
    }

    private void endSetup(){
        deviceListAdapter = new DeviceExpandableListAdapter(getActivity(), deviceListDataHeader, deviceListDataChild);

        deviceLiveData.observe(getActivity(), new Observer<HashMap<String, Device>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, Device> stringDeviceHashMap) {
                // Esto actualiza la lista de dispositivos cada vez que cambia la lista en el ViewModel
                deviceListDataChild = stringDeviceHashMap;
                deviceListDataHeader = new ArrayList<>();
                deviceListDataHeader.addAll(deviceListDataChild.keySet());
                Toast.makeText(getContext(),"Recargando datos devices",Toast.LENGTH_LONG).show();
                Log.d("tag", deviceListDataHeader.toString() + " " + deviceListDataChild.toString());
                if (deviceListAdapter != null) {
                    deviceListAdapter.updateList(stringDeviceHashMap);
                    deviceListAdapter.notifyDataSetChanged();
                }
            }
        });

        // setting list adapter
        deviceExpListView.setAdapter(deviceListAdapter);
    }
}
