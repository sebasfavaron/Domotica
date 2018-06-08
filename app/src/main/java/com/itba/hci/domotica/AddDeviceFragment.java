package com.itba.hci.domotica;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddDeviceFragment extends DialogFragment {

    public AddDeviceFragment(){}

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.alarm_content, container);
        //view.findViewById()
        return view;
    }*/
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
    }

}
