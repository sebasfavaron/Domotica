package com.itba.hci.domotica;

import android.app.DialogFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends DialogFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container);

        Switch deviceSwitch = (Switch) view.findViewById(R.id.enable_device_notification_switch);
        final MainViewModel model = ViewModelProviders.of((FragmentActivity) getActivity()).get(MainViewModel.class);

        deviceSwitch.setChecked(false);
        deviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.setSendNotifications(isChecked);
            }
        });

        Spinner setUpdatingTime = (Spinner) view.findViewById(R.id.set_updating_time_spinner);
        setUpdatingTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String time = parent.getItemAtPosition(position).toString();
                switch (time){
                    case "5 minutes": model.setNotificationPeriodInMinutes(5); break;
                    case "10 minutes": model.setNotificationPeriodInMinutes(10); break;
                    case "30 minutes": model.setNotificationPeriodInMinutes(30); break;
                    case "1 hour": model.setNotificationPeriodInMinutes(60); break;
                    case "2 hours": model.setNotificationPeriodInMinutes(120); break;
                }
                Toast.makeText(getContext(),"Notifications interval set for "+time,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                model.setSendNotifications(false);
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
