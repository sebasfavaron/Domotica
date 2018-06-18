package com.itba.hci.domotica;

public class deviceEvent {
    private String timestamp;
    private String deviceId;
    private String event;
    private String args;

    public deviceEvent(String timestamp, String deviceId, String event, String args) {
        this.args = args;
        this.deviceId = deviceId;
        this.event = event;
        this.timestamp = timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getArgs() {
        return args;
    }

    public String getEvent() {
        return event;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
