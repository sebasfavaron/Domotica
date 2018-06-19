package com.itba.hci.domotica;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class NotificationReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("tag", "Corrieron notificaciones");
        //todo: ejecutar busqueda de eventos, parsearlos y notificarlos
        String requestTag = Api.getInstance(context).getDeviceEvents(new Response.Listener<GetDeviceEventsResponse>() {
            @Override
            public void onResponse(GetDeviceEventsResponse response) {
                Log.d("tag", "Notificaciones");
                Log.d("tag", response.getDeviceEvents().get(0).getEvent());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
            }
        });
    }

}

