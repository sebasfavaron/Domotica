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
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RoutineFragment extends MainActivity.GeneralFragment {
    private RoutineListAdapter routineListAdapter;
    private ListView routineListView;
    private ArrayList<Routine> routineList;

    public RoutineFragment() {
        super();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_routines, container, false);

        routineList = new ArrayList<>();

        // get the listview
        routineListView = (ListView) rootView.findViewById(R.id.list);

        // listeners for when data in devices changes
        MainViewModel model = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        model.getRoutineList().observe(getActivity(), new Observer<ArrayList<Routine>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Routine> arrayList) {
                routineList = arrayList;
                if(routineListAdapter != null) routineListAdapter.notifyDataSetChanged();
            }
        });

        routineListAdapter = new RoutineListAdapter(getActivity(), routineList);

        // setting list adapter
        routineListView.setAdapter(routineListAdapter);
        return rootView;
    }
}
