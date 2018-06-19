package com.itba.hci.domotica;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("tag", "Corrieron notificaciones");
        //todo: ejecutar busqueda de eventos, parsearlos y notificarlos

    }
}
