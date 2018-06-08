package com.itba.hci.domotica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends MainActivity.GeneralFragment {
    public HomeFragment() {
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
        // igual que en DeviceFragment pero con favoritos (filtrar con el meta)
    }


}
