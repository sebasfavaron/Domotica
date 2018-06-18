package com.itba.hci.domotica;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RoutineFragment extends MainActivity.GeneralFragment {
    private RoutineExpandableListAdapter routineListAdapter;
    private ExpandableListView routineExpListView;
    private ArrayList<String> routineListDataHeader;
    private HashMap<String, Routine> routineListDataChild;

    public RoutineFragment() {
        super();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        routineListDataHeader = new ArrayList<>();
        routineListDataChild = new HashMap<>();

        // get the listview
        routineExpListView = (ExpandableListView) rootView.findViewById(R.id.expList);

        // listeners for when data in devices changes
        MainViewModel model = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        model.getRoutineMap().observe(getActivity(), new Observer<HashMap<String, Routine>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, Routine> stringRoutineHashMap) {
                // Esto actualiza la lista de dispositivos cada vez que cambia la lista en el ViewModel
                routineListDataChild = stringRoutineHashMap;
                routineListDataHeader = new ArrayList<>();
                routineListDataHeader.addAll(routineListDataChild.keySet());
                if(routineListAdapter != null) routineListAdapter.notifyDataSetChanged();
            }
        });
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Routine r1 = new Routine("rutina_homee"+Math.random(), "1234", new ArrayList<Action>());
                        routineListDataHeader.add(r1.getName());
                        routineListDataChild.put(routineListDataHeader.get(routineListDataHeader.size()-1), r1);
                        routineListAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), (routineListAdapter.routineSize()).toString()+"(rutina)", Toast.LENGTH_LONG).show();
                    }
                },7000);

        routineListAdapter = new RoutineExpandableListAdapter(getActivity(), routineListDataHeader, routineListDataChild);

        // setting list adapter
        routineExpListView.setAdapter(routineListAdapter);
        return rootView;
    }

    private void prepareListData() {
        // Crear rutinas
        Routine r1 = new Routine("rutina"+Math.random(), "1234", randomActionList());

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

        ArrayList<String> deviceListDataHeader = getArguments().getStringArrayList("deviceListDataHeader");
        HashMap<String,Device> deviceListDataChild = (HashMap<String, Device>) getArguments().getSerializable("deviceListDataChild");
        if(deviceListDataChild == null || deviceListDataHeader == null) {
            Snackbar.make(getView(), "Error initiating app", Snackbar.LENGTH_LONG);
            return actions;
        }

        if(deviceListDataHeader.isEmpty()) {
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
