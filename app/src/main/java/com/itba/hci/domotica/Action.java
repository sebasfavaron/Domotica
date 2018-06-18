package com.itba.hci.domotica;

class Action {
    private String deviceId;
    private String actionName;
    private String[] params;
    private String meta;
    // todo: ver como decirle que accion realiza en el dispositivo

    public Action(String deviceId, String actionName, String[] params) {
        this.deviceId = deviceId;
        this.actionName = actionName;
        this.meta = "{}";
        this.params = params;
    }

    public Action(String deviceId, String actionName) {
        this.deviceId = deviceId;
        this.actionName = actionName;
        this.meta = "{}";
        this.params = [];
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }


    public String getActionName() {
        return actionName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String[] getParams() {
        return params;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}
