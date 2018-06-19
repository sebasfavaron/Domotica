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
        deviceListAdapter = new DeviceExpandableListAdapter(getActivity(), deviceListDataHeader, deviceListDataChild);

        // get the listview
        deviceExpListView = (ExpandableListView) rootView.findViewById(R.id.expList);

        // listeners for when data in routines changes
        final MainViewModel model = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        deviceLiveData = model.getDeviceMap();
        if(deviceLiveData == null) {
            // Enters if the api call failed and gives a second oportunity
            Snackbar.make(rootView, R.string.error_message, Snackbar.LENGTH_LONG).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deviceLiveData = model.getDeviceMap();
                    if(deviceLiveData == null) Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.conection_error, Snackbar.LENGTH_SHORT).show();
                    else endSetup();
                }
            }).show();
        } else {
            endSetup();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            endSetup();
                            Log.i("tag", "Segunda llamada a la api desde DeviceFragment (5 segs despues) pidiendo los dispositivos");
                        }
                    },
                    5000);
        }

        return rootView;
    }

    private void endSetup(){
        deviceLiveData.removeObservers(getActivity());
        deviceLiveData.observe(getActivity(), new Observer<HashMap<String, Device>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, Device> stringDeviceHashMap) {
                // Esto actualiza la lista de dispositivos cada vez que cambia la lista en el ViewModel
                deviceListDataChild = stringDeviceHashMap;
                deviceListDataHeader = new ArrayList<>();
                deviceListDataHeader.addAll(deviceListDataChild.keySet());
                Log.d("tag", "\n"+deviceListDataHeader.toString() + " " + deviceListDataChild.toString()+"\n\n");
                if (deviceListAdapter != null) {
                    deviceListAdapter.updateList(stringDeviceHashMap);
                }
            }
        });

        // setting list adapter
        deviceExpListView.setAdapter(deviceListAdapter);
    }
}
