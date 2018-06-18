package com.itba.hci.domotica;

import android.content.BroadcastReceiver;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Api {
    private final String URL = "http://10.0.2.2:8080/api/";

    private static Api instance;
    private static RequestQueue requestQueue;

    private Api(Context context) {
        this.requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
    }

    public static synchronized Api getInstance(Context context) {
        if (instance == null) {
            instance = new Api(context);
        }
        return instance;
    }

    public String addDevice(Device device, Response.Listener<Device> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Device, Device> request =
                new GsonRequest<Device, Device>(Request.Method.POST, url, device, "device", Device.class, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getDevice(String id, Response.Listener<Device> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + id;
        GsonRequest<Object, Device> request =
                new GsonRequest<Object, Device>(Request.Method.GET, url, null, "device", Device.class, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getAcState(Device device, Response.Listener<AcState> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + device.getId() + "/getState/";

        GsonRequest<Object,AcState> request =
                new GsonRequest<Object,AcState>(Request.Method.GET, url, "{}", null, AcState.class, null, listener, errorListener);

        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;

    }

    public String getBlindState(Device device, Response.Listener<BlindState> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + device.getId() + "/getState/";

        GsonRequest<Object,BlindState> request =
                new GsonRequest<Object,BlindState>(Request.Method.GET, url, "{}", null, BlindState.class, null, listener, errorListener);

        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;

    }

    public String getDoorState(Device device, Response.Listener<DoorState> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + device.getId() + "/getState/";

        GsonRequest<Object,DoorState> request =
                new GsonRequest<Object,DoorState>(Request.Method.GET, url, "{}", null, DoorState.class, null, listener, errorListener);

        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;

    }

    public String getLampState(Device device, Response.Listener<LampState> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + device.getId() + "/getState/";

        GsonRequest<Object,LampState> request =
                new GsonRequest<Object,LampState>(Request.Method.GET, url, "{}", null, LampState.class, null, listener, errorListener);

        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;

    }

    public String getOvenState(Device device, Response.Listener<OvenState> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + device.getId() + "/getState/";

        GsonRequest<Object,OvenState> request =
                new GsonRequest<Object,OvenState>(Request.Method.GET, url, "{}", null, OvenState.class, null, listener, errorListener);

        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;

    }

    public String getRefrigeratorState(Device device, Response.Listener<RefrigeratorState> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + device.getId() + "/getState/";

        GsonRequest<Object,RefrigeratorState> request =
                new GsonRequest<Object,RefrigeratorState>(Request.Method.GET, url, "{}", null, RefrigeratorState.class, null, listener, errorListener);

        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;

    }

    public String modifyDevice(Device device, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/" + device.getId();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Device, Boolean> request =
                new GsonRequest<Device, Boolean>(Request.Method.PUT, url, device, "result", Boolean.class, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String deviceAction(Device device,String actionName,String body, Response.Listener<Boolean> listener, Response.ErrorListener errorListener){
        String url = URL + "devices/" + device.getId() + "/" + actionName;
        Map<String,String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<String,Boolean> request =
                new GsonRequest<String,Boolean>
                        (Request.Method.PUT,url,body,"result", Boolean.class,headers,listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getDevices(Response.Listener<GetDevicesResponse> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/";
        GsonRequest<Object, GetDevicesResponse> request =
                new GsonRequest<Object, GetDevicesResponse>
                        (Request.Method.GET, url, null, null,
                                GetDevicesResponse.class,null, listener, errorListener);

        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getRoutines(Response.Listener<GetRoutineResponse> listener, Response.ErrorListener errorListener){
        String url = URL + "routines/";
        GsonRequest<Object, GetRoutineResponse> request =
                new GsonRequest<Object, GetRoutineResponse>
                        (Request.Method.GET, url, null, null,
                                GetRoutineResponse.class,null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;

    }

    public String executeRoutine(Routine routine, Response.Listener<Boolean> listener, Response.ErrorListener errorListener){
        String url = URL + "devices/" + routine.getId() + "/execute/";
        Map<String,String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<String,Boolean> request =
                new GsonRequest<String,Boolean>
                        (Request.Method.PUT,url,"{}","result", Boolean.class,headers,listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getURL() {
        return URL;
    }

    public static Api getInstance() {
        return instance;
    }

    /*
    public String addRoom(Room room, Response.Listener<Room> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Room, Room> request =
                new GsonRequest<Room, Room>(Request.Method.POST, url, room, "room", Room.class, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String modifyRoom(Room room, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + room.getId();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Room, Boolean> request =
                new GsonRequest<Room, Boolean>(Request.Method.PUT, url, room, "result", Boolean.class, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String deleteRoom(String id, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + id;
        GsonRequest<Object, Boolean> request =
                new GsonRequest<Object, Boolean>(Request.Method.DELETE, url, null, "result", Boolean.class, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getRoom(String id, Response.Listener<Room> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + id;
        GsonRequest<Object, Room> request =
                new GsonRequest<Object, Room>(Request.Method.GET, url, null, "room", Room.class, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }
    */


    public void cancelRequest(String uuid) {
        if ((uuid != null) && (requestQueue != null)) {
            requestQueue.cancelAll(uuid);
        }
    }
}
