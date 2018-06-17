package com.itba.hci.domotica;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceFragment extends MainActivity.GeneralFragment {
    private DeviceExpandableListAdapter deviceListAdapter;
    private ExpandableListView deviceExpListView;
    private ArrayList<String> deviceListDataHeader;
    private HashMap<String,Device> deviceListDataChild;
    private boolean firstRun;

    public DeviceFragment() {
        super();
        firstRun = true;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        deviceListDataHeader = getArguments().getStringArrayList("deviceListDataHeader");
        deviceListDataChild = (HashMap<String, Device>) getArguments().getSerializable("deviceListDataChild");
        if(deviceListDataChild == null || deviceListDataHeader == null)
            Snackbar.make(rootView,"Error initiating app", Snackbar.LENGTH_LONG);

        // get the listview
        deviceExpListView = (ExpandableListView) rootView.findViewById(R.id.expList);

        // preparing list data
        prepareListData();
        firstRun = false;

        deviceListAdapter = new DeviceExpandableListAdapter(getActivity(), deviceListDataHeader, deviceListDataChild);

        // setting list adapter
        deviceExpListView.setAdapter(deviceListAdapter);
        return rootView;
    }

    private void prepareListData() {
        if(!firstRun) return; //patch a un problema de ejecucion multiple de onCreateView

        // Crear dispositivos segun lo que haya en la api
        //todo: meter los dispositivos de la api aca
        Device ac1 = new Device("ac1","ac");
        Device al1 = new Device("alarm1","alarm");
        Device b1 = new Device("blind1","blind");
        Device d1 = new Device("door1","door");
        Device l1 = new Device("lamp1","lamp");
        Device o1 = new Device("oven1","oven");
        Device r1 = new Device("refrigerator1","refrigerator");
        Device t1 = new Device("timer1","timer");

        // Meter los nombres en la lista de headers
        deviceListDataHeader.add(ac1.getName());
        deviceListDataChild.put(deviceListDataHeader.get(deviceListDataHeader.size()-1), ac1);
        deviceListDataHeader.add(al1.getName());
        deviceListDataChild.put(deviceListDataHeader.get(deviceListDataHeader.size()-1), al1);
        deviceListDataHeader.add(b1.getName());
        deviceListDataChild.put(deviceListDataHeader.get(deviceListDataHeader.size()-1), b1);
        deviceListDataHeader.add(d1.getName());
        deviceListDataChild.put(deviceListDataHeader.get(deviceListDataHeader.size()-1), d1);
        deviceListDataHeader.add(l1.getName());
        deviceListDataChild.put(deviceListDataHeader.get(deviceListDataHeader.size()-1), l1);
        deviceListDataHeader.add(o1.getName());
        deviceListDataChild.put(deviceListDataHeader.get(deviceListDataHeader.size()-1), o1);
        deviceListDataHeader.add(r1.getName());
        deviceListDataChild.put(deviceListDataHeader.get(deviceListDataHeader.size()-1), r1);
        deviceListDataHeader.add(t1.getName());
        deviceListDataChild.put(deviceListDataHeader.get(deviceListDataHeader.size()-1), t1);

        // Agregar (nombre -> dispositivo) al hashmap

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Device dTest = new Device("test","lamp");
                        deviceListDataHeader.add(dTest.getName());
                        deviceListDataChild.put(deviceListDataHeader.get(deviceListDataHeader.size()-1), dTest);
                        deviceListAdapter.notifyDataSetChanged();
                    }
                },5000);
    }
}
