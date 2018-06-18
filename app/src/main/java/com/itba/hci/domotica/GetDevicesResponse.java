package com.itba.hci.domotica;

import java.util.ArrayList;

public class GetDevicesResponse {
    private ArrayList<Device> devices;

    public GetDevicesResponse(ArrayList<Device> devices) {
        this.devices = devices;
    }

    public void setDevices(ArrayList<Device> devices){
        this.devices = devices;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }
}
