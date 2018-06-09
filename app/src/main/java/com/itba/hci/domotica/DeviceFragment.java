package com.itba.hci.domotica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceFragment extends MainActivity.GeneralFragment {
    public DeviceFragment() {
        super();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.expList);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        return rootView;
    }

    private void prepareListData() {
        // Crear dispositivos segun lo que haya en la api
        Device ac1 = new Device("ac1","ac");
        Device al1 = new Device("alarm1","alarm");
        Device b1 = new Device("blind1","blind");
        Device d1 = new Device("door1","door");
        Device l1 = new Device("lamp1","lamp");
        Device o1 = new Device("oven1","oven");
        Device r1 = new Device("refrigerator1","refrigerator");
        Device t1 = new Device("timer1","timer");

        // Meter los nombres en la lista de headers
        listDataHeader.add(ac1.getName());
        listDataHeader.add(al1.getName());
        listDataHeader.add(b1.getName());
        listDataHeader.add(d1.getName());
        listDataHeader.add(l1.getName());
        listDataHeader.add(o1.getName());
        listDataHeader.add(r1.getName());
        listDataHeader.add(t1.getName());

        // Agregar (nombre -> dispositivo) al hashmap
        listDataChild.put(listDataHeader.get(0), ac1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), al1);
        listDataChild.put(listDataHeader.get(2), b1);
        listDataChild.put(listDataHeader.get(3), d1);
        listDataChild.put(listDataHeader.get(4), l1);
        listDataChild.put(listDataHeader.get(5), o1);
        listDataChild.put(listDataHeader.get(6), r1);
        listDataChild.put(listDataHeader.get(7), t1);
    }
}
