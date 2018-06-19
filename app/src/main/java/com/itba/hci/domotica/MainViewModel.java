package com.itba.hci.domotica;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainViewModel extends ViewModel {
    // User Data
    private MutableLiveData<HashMap<String,Device>> deviceListDataChild;
    private List<String> deviceListDataHeader;
    private MutableLiveData<ArrayList<Routine>> routineList;

    // Notifications
    private Integer notificationPeriodInMinutes;
    private boolean sendNotifications;
    private AlarmManager alarmManager;
    private PendingIntent notificationReceiverPendingIntent;

    // Context
    @SuppressLint("StaticFieldLeak")
    private Context appContext;

    public LiveData<HashMap<String, Device>> getDeviceMap() {
        if(deviceListDataChild == null){
            deviceListDataChild = new MutableLiveData<>();
            deviceListDataHeader = new ArrayList<>();
            boolean success = updateDeviceMap();
            if(!success) return null;
        }
        return deviceListDataChild;
    }

    public void setAlarmManager(AlarmManager alarmManager) {
        this.alarmManager = alarmManager;
    }

    public void setAppContext(Context context){
        appContext = context;
    }

    public void setSendNotifications(boolean sendNotifications) {
        this.sendNotifications = sendNotifications;
        changeNotificationTime();
    }

    public boolean getSendNotifications() {
        return sendNotifications;
    }

    public void addDevice(Device device){
        deviceListDataChild.getValue().put(device.getName(),device);
        deviceListDataHeader.add(device.getName());
    }

    public LiveData<ArrayList<Routine>> getRoutineList() {
        if(routineList == null){
            routineList = new MutableLiveData<>();
            loadRoutineMap();
        }
        return routineList;
    }

    public boolean updateDeviceMap(){
        final boolean[] success = {false};

        if(deviceListDataChild.getValue() == null) deviceListDataChild.setValue(new HashMap<String, Device>());
        if(deviceListDataHeader == null) deviceListDataHeader = new ArrayList<>();

        String requestTag = Api.getInstance(appContext).getDevices(new Response.Listener<GetDevicesResponse>() {
            @Override
            public void onResponse(GetDevicesResponse response) {
                // Vaciar dispositivos
                deviceListDataChild.getValue().clear();
                deviceListDataHeader.clear();

                // Actualizar
                ArrayList<Device> listDevice = response.getDevices();
                for(Device device : listDevice){
                    deviceListDataHeader.add(device.getName());
                    deviceListDataChild.getValue().put(device.getName(), device);
                }
                success[0] = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
                success[0] = false;
            }
        });

        /*// Crear dispositivos segun lo que haya en la api
        Device ac1 = new Device("ac1","ac");
        Device al1 = new Device("alarm1","alarm");
        Device b1 = new Device("blind1","blind");
        Device d1 = new Device("door1","door");
        Device l1 = new Device("lamp1","lamp");
        Device o1 = new Device("oven1","oven");
        Device r1 = new Device("refrigerator1","refrigerator");
        Device t1 = new Device("timer1","timer");

        // Meter los nombres en la lista de headers
        deviceListDataHeader.add(ac1.getName());
        deviceListDataChild.getValue().put(deviceListDataHeader.get(deviceListDataHeader.size()-1), ac1);
        deviceListDataHeader.add(al1.getName());
        deviceListDataChild.getValue().put(deviceListDataHeader.get(deviceListDataHeader.size()-1), al1);
        deviceListDataHeader.add(b1.getName());
        deviceListDataChild.getValue().put(deviceListDataHeader.get(deviceListDataHeader.size()-1), b1);
        deviceListDataHeader.add(d1.getName());
        deviceListDataChild.getValue().put(deviceListDataHeader.get(deviceListDataHeader.size()-1), d1);
        deviceListDataHeader.add(l1.getName());
        deviceListDataChild.getValue().put(deviceListDataHeader.get(deviceListDataHeader.size()-1), l1);
        deviceListDataHeader.add(o1.getName());
        deviceListDataChild.getValue().put(deviceListDataHeader.get(deviceListDataHeader.size()-1), o1);
        deviceListDataHeader.add(r1.getName());
        deviceListDataChild.getValue().put(deviceListDataHeader.get(deviceListDataHeader.size()-1), r1);
        deviceListDataHeader.add(t1.getName());
        deviceListDataChild.getValue().put(deviceListDataHeader.get(deviceListDataHeader.size()-1), t1);*/

        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Device dTest = new Device("test","lamp");
                        deviceListDataHeader.add(dTest.getName());
                        deviceListDataChild.getValue().put(deviceListDataHeader.get(deviceListDataHeader.size()-1), dTest);
                    }
                },15000);*/

        return success[0];
    }

    private void loadRoutineMap() {
        routineList.setValue(new ArrayList<Routine>());

        String requestTag = Api.getInstance(appContext).getRoutines(new Response.Listener<GetRoutineResponse>() {
            @Override
            public void onResponse(GetRoutineResponse response) {
                // Vaciar dispositivos
                ArrayList<Routine> routines = response.getRoutines();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
            }
        });


        // Crear rutinas
        routineList.getValue().add(new Routine("rutina"+Math.random(), "1234", randomActionList()));

    }

    private ArrayList<Action> randomActionList() {
        ArrayList<Action> actions = new ArrayList<>();

        if(deviceListDataChild == null || deviceListDataHeader == null || deviceListDataHeader.isEmpty() ||
                deviceListDataChild.getValue() == null) {
            return actions;
        }

        int amount = (int) (Math.random() * 5);
        for(int i=0; i<amount; i++) {
            int deviceIndex = (int)(Math.random() * deviceListDataHeader.size());
            Device d = deviceListDataChild.getValue().get(deviceListDataHeader.get(deviceIndex));

            //actions.add(new Action());
        }
        return actions;
    }

    public void setNotificationData(AlarmManager alarmManager, PendingIntent intent) {
        if(alarmManager == null || intent == null || (this.alarmManager == alarmManager && this.notificationReceiverPendingIntent == intent))
            return;
        this.alarmManager = alarmManager;
        this.notificationReceiverPendingIntent = intent;
        changeNotificationTime();
    }

    private void changeNotificationTime() {
        cancelNotification();
        if(sendNotifications){
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 10000,
                getNotificationPeriodInMinutes()*60000,
                notificationReceiverPendingIntent);
            Log.d("tag", "llamando al alarm manager");
        }
    }

    private void cancelNotification(){
        alarmManager.cancel(notificationReceiverPendingIntent);
    }

    public Integer getNotificationPeriodInMinutes() {
        if(notificationPeriodInMinutes == null){
            notificationPeriodInMinutes = 1;
        }
        return notificationPeriodInMinutes;
    }

    public void setNotificationPeriodInMinutes(Integer notificationPeriodInMinutes) {
        this.notificationPeriodInMinutes = notificationPeriodInMinutes;
        changeNotificationTime();
    }
}
