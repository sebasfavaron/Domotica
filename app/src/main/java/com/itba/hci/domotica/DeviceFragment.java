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
    private List<String> listDataHeader;
    private HashMap<String, Device> listDataChild;
    public DeviceFragment() {
        super();
        this.listDataChild = new HashMap<>();
        this.listDataHeader = new ArrayList<>();
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

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        // Adding child data
        listDataHeader.add("Door");
        listDataHeader.add("Refrigerator");
        listDataHeader.add("Oven");

        // Adding child data
        //todo: cambiar el formato de type, String es muy propenso a errores. Capaz hacer macros de tipo int PUERTA=0 y hacer type un int
        listDataChild.put(listDataHeader.get(0), new Device("door1","door")); // Header, Child data
        listDataChild.put(listDataHeader.get(1), new Device("refrigerator1","refrigerator"));
        listDataChild.put(listDataHeader.get(2), new Device("oven1","oven"));
    }
}
