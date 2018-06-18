package com.itba.hci.domotica;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainViewModel extends ViewModel {
    private MutableLiveData<HashMap<String,Device>> deviceListDataChild;
    private MutableLiveData<ArrayList<Routine>> routineList;
    private List<String> deviceListDataHeader;
    private MutableLiveData<Integer> notificationPeriodInMinutes;
    private Context appContext;

    public LiveData<HashMap<String, Device>> getDeviceMap() {
        if(deviceListDataChild == null){
            deviceListDataChild = new MutableLiveData<>();
            deviceListDataHeader = new ArrayList<>();
            loadDeviceMap();
        }
        return deviceListDataChild;
    }

    public void setAppContext(Context context){
        appContext = context;
    }

    public void addDevice(){
        // le pasan a este metodo los datos de la api y aca se agrega a la api y a la deviceListDataChild
        // como se modifico deviceListDataChild se deberian activar los listeners de cada fragmento y actualizar los adapters
    }

    public MutableLiveData<Integer> getNotificationPeriodInMinutes() {
        if(notificationPeriodInMinutes == null){
            notificationPeriodInMinutes = new MutableLiveData<>();
            //todo: ver como settear ese periodo
        }
        return notificationPeriodInMinutes;
    }

    public LiveData<ArrayList<Routine>> getRoutineList() {
        if(routineList == null){
            routineList = new MutableLiveData<>();
            loadRoutineMap();
        }
        return routineList;
    }

    private void loadDeviceMap(){
        deviceListDataChild.setValue(new HashMap<String, Device>());

        String requestTag = Api.getInstance(appContext).getDevices(new Response.Listener<GetDevicesResponse>() {
            @Override
            public void onResponse(GetDevicesResponse response) {
                GetDevicesResponse resp = response;

                //Aca se agregaria response.getDevices();
                //Aca se agregan los dispositivos
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
                Toast.makeText(appContext, R.string.error_message, Toast.LENGTH_LONG).show();
            }
        });

        // Vaciar dispositivos


        // Crear dispositivos segun lo que haya en la api
        //todo: meter los dispositivos de la api aca
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
        deviceListDataChild.getValue().put(deviceListDataHeader.get(deviceListDataHeader.size()-1), t1);

        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Device dTest = new Device("test","lamp");
                        deviceListDataHeader.add(dTest.getName());
                        deviceListDataChild.getValue().put(deviceListDataHeader.get(deviceListDataHeader.size()-1), dTest);
                    }
                },15000);*/
    }

    private void loadRoutineMap() {
        routineList.setValue(new ArrayList<Routine>());

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
}
