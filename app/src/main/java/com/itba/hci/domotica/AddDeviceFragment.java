package com.itba.hci.domotica;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddDeviceFragment extends DialogFragment {

    private String deviceName;
    private String deviceType;

    public AddDeviceFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.add_device, container);

        final EditText editText = view.findViewById(R.id.add_device_name_field);

        Spinner spinner = view.findViewById(R.id.add_device_type_spinner);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deviceType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
        List<String> deviceTypes = new ArrayList<>();
        deviceTypes.add(getString(R.string.ac));
        deviceTypes.add(getString(R.string.alarm));
        deviceTypes.add(getString(R.string.blind));
        deviceTypes.add(getString(R.string.door));
        deviceTypes.add(getString(R.string.lamp));
        deviceTypes.add(getString(R.string.oven));
        deviceTypes.add(getString(R.string.refrigerator));
        deviceTypes.add(getString(R.string.timer));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, deviceTypes);
        spinner.setAdapter(adapter);

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceName = editText.getText().toString();

                //todo: mandar datos (deviceName y deviceType) del dispositivo nuevo a la api


                dismiss();
            }
        });

        Button cancelButton = view.findViewById(R.id.cancel_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
    /*public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Agregar dispositivo")
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // agregar a la api y a los dispositivos
                    }
                })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // no deberiamos tener que poner nada, el dialog se va cuando tocas un boton
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }*/

}
