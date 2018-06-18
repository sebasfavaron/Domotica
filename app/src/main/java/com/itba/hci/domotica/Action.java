package com.itba.hci.domotica;

import java.util.ArrayList;

class Action {
    private String deviceId;
    private String actionName;
    private ArrayList<String> params;
    private String meta;
    // todo: ver como decirle que accion realiza en el dispositivo

    public Action(String deviceId, String actionName, String[] params) {
        this.deviceId = deviceId;
        this.actionName = actionName;
        this.meta = "{}";
        this.params = new ArrayList<String>();
    }

    public Action(String deviceId, String actionName) {
        this.deviceId = deviceId;
        this.actionName = actionName;
        this.meta = "{}";
        this.params = new ArrayList<String>();
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public String getActionName() {
        return actionName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
