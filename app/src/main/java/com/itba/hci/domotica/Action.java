package com.itba.hci.domotica;

class Action {
    private Device device;
    // todo: ver como decirle que accion realiza en el dispositivo
    private String action;

    public Action(Device device, String action) {
        this.device = device;
        this.action = action;
    }
}
