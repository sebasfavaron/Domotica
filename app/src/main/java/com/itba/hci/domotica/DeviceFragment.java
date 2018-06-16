package com.itba.hci.domotica;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class DeviceFragment extends MainActivity.GeneralFragment {
    public DeviceFragment() {
        super();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // get the listview
        deviceExpListView = (ExpandableListView) rootView.findViewById(R.id.expList);

        // preparing list data
        prepareListData();

        deviceListAdapter = new DeviceExpandableListAdapter(getActivity(), deviceListDataHeader, deviceListDataChild);

        // setting list adapter
        deviceExpListView.setAdapter(deviceListAdapter);
        return rootView;
    }

    private void prepareListData() {
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
        deviceListDataHeader.add(al1.getName());
        deviceListDataHeader.add(b1.getName());
        deviceListDataHeader.add(d1.getName());
        deviceListDataHeader.add(l1.getName());
        deviceListDataHeader.add(o1.getName());
        deviceListDataHeader.add(r1.getName());
        deviceListDataHeader.add(t1.getName());

        // Agregar (nombre -> dispositivo) al hashmap
        listDataChild.put(listDataHeader.get(0), ac1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), al1);
        listDataChild.put(listDataHeader.get(2), b1);
        listDataChild.put(listDataHeader.get(3), d1);
        listDataChild.put(listDataHeader.get(4), l1);
        listDataChild.put(listDataHeader.get(5), o1);
        listDataChild.put(listDataHeader.get(6), r1);
        listDataChild.put(listDataHeader.get(7), t1);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Device dTest = new Device("test","lamp");
                        listDataHeader.add(dTest.getName());
                        listDataChild.put(listDataHeader.get(listDataHeader.size()-1), dTest);
                        listAdapter.notifyDataSetChanged();
                    }
                },5000);
    }
}
