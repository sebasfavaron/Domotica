package com.itba.hci.domotica;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class RoutineFragment extends MainActivity.GeneralFragment {
    public RoutineFragment() {
        super();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // get the listview
        routineExpListView = (ExpandableListView) rootView.findViewById(R.id.expList);

        prepareListData();

        routineListAdapter = new RoutineExpandableListAdapter(getActivity(), routineListDataHeader, routineListDataChild);

        // setting list adapter
        routineExpListView.setAdapter(routineListAdapter);
        return rootView;
    }

    private void prepareListData() {
        // Crear rutinas
        Routine r1 = new Routine("rutina1", "1234", randomActionList());

        // Meter los nombres en la lista de headers
        routineListDataHeader.add(r1.getName());

        // Agregar (nombre -> rutina) al hashmap
        routineListDataChild.put(routineListDataHeader.get(0), r1); // Header, Child data

        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Device dTest = new Device("test","lamp");
                        deviceListDataHeader.add(dTest.getName());
                        deviceListDataChild.put(deviceListDataHeader.get(deviceListDataHeader.size()-1), dTest);
                        deviceListAdapter.notifyDataSetChanged();
                    }
                },5000);*/
    }

    private ArrayList<Action> randomActionList() {
        ArrayList<Action> actions = new ArrayList<>();
        if(deviceListDataHeader.isEmpty()) {
            Toast.makeText(getContext(),"no hay devices todavia",Toast.LENGTH_LONG).show();
            return actions;
        }
        int amount = (int) (Math.random() * 5);
        for(int i=0; i<amount; i++) {
            int deviceIndex = (int)(Math.random() * deviceListDataHeader.size());
            Device d = deviceListDataChild.get(deviceListDataHeader.get(deviceIndex));

            //actions.add(new Action());
        }
        return actions;
    }
}
