package com.itba.hci.domotica;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends DialogFragment {
    private String time;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container);
        Switch deviceSwitch = (Switch) view.findViewById(R.id.enable_device_notification_switch);
        Switch routineSwitch = (Switch) view.findViewById(R.id.enable_routine_notification_switch);
        deviceSwitch.setChecked(true);
        routineSwitch.setChecked(true);
        deviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on

                } else {
                    // If the switch button is off

                }
            }
        });

        routineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the switch button is on

                } else {
                    // If the switch button is off

                }
            }
        });

        Spinner setUpdatingTime = (Spinner) view.findViewById(R.id.set_updating_time_spinner);
        setUpdatingTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> updatingTimes = new ArrayList<>();
        updatingTimes.add(getString(R.string.updating_time1));
        updatingTimes.add(getString(R.string.updating_time2));
        updatingTimes.add(getString(R.string.updating_time3));
        updatingTimes.add(getString(R.string.updating_time4));
        updatingTimes.add(getString(R.string.updating_time5));
        setUpdatingTime.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, updatingTimes));
        return view;
    }
}
