package com.itba.hci.domotica;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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


import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AddDeviceFragment extends DialogFragment {
    private MainActivity mainActivity;
    private String deviceName;
    private String deviceType;
    private Context context;
    private String requestTag;
    private Device device;

    public AddDeviceFragment(){
        this.device = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getDialog().setTitle(getString(R.string.add_device));
        View view = inflater.inflate(R.layout.add_device, container);

        //final EditText editText = view.findViewById(R.id.add_device_name_field);

        final Spinner spinner = view.findViewById(R.id.add_device_type_spinner);
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

        final TextInputLayout til = (TextInputLayout) view.findViewById(R.id.add_device_layout);
        til.setHint("Device Name");

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hideKeyboard();

                deviceName = til.getEditText().getText().toString();

                //deviceName = editText.getText().toString();
                if (!validLength(deviceName)) {
                    //editText.setError("Longitud menor a 3");
                    til.setError("Logitud debe ser mayor a 3");
                    return;
                } else {
                    til.setErrorEnabled(false);
                }

                deviceType = Device.translateEsToEn(deviceType);

                Log.d("tag", Device.translateEsToEn(deviceType));
                Log.d("tag",deviceName);

                device = new Device(deviceName,deviceType);

                requestTag = Api.getInstance(context).addDevice(device, new Response.Listener<Device>() {
                    @Override
                    public void onResponse(Device response) {
                        device.setId(response.getId());
                        MainViewModel model = ViewModelProviders.of((FragmentActivity) getActivity()).get(MainViewModel.class);
                        model.addDevice(device);
                        Log.d("tag", "Api is working. Device Id = ");
                        Log.d("tag", response.getId());

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("tag", "Api is not working. Device Id = ");
                        handleError(error);
                    }
                });

                dismiss();
            }
        });

        Button cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            try {
                mainActivity = (MainActivity) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public boolean validLength(String deviceName)
    {
        return deviceName.length() > 3;
    }

    /*private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    */

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

    private void handleError(VolleyError error) {
        Error response = null;

        NetworkResponse networkResponse = error.networkResponse;
        if ((networkResponse != null) && (error.networkResponse.data != null)) {
            try {
                String json = new String(
                        error.networkResponse.data,
                        HttpHeaderParser.parseCharset(networkResponse.headers));

                JSONObject jsonObject = new JSONObject(json);
                json = jsonObject.getJSONObject("error").toString();

                Gson gson = new Gson();
                response = gson.fromJson(json, Error.class);
            } catch (JSONException e) {
            } catch (UnsupportedEncodingException e) {
            }
        }

        Log.e("tag", error.toString());
        String text = getResources().getString(R.string.error_message);
        if (response != null)
            text += " " + response.getDescription().get(0);

        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

}
