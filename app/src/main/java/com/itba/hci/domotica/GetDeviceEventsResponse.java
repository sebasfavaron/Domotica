package com.itba.hci.domotica;

import java.util.ArrayList;

public class GetDeviceEventsResponse {
    ArrayList<deviceEvent> deviceEvents;

    public GetDeviceEventsResponse(ArrayList<deviceEvent> deviceEvents){
        this.deviceEvents = deviceEvents;
    }

    public void setDeviceEvents(ArrayList<deviceEvent> deviceEvents) {
        this.deviceEvents = deviceEvents;
    }


    public ArrayList<deviceEvent> getDeviceEvents() {
        return deviceEvents;
    }
}

