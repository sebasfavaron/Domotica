package com.itba.hci.domotica;

class Action {
    private String action;
    // todo: ver como decirle que accion realiza en el dispositivo
    private Device device;

    public Action(Device device, String action) {
        this.device = device;
        this.action = action;
    }

}
