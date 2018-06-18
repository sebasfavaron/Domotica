package com.itba.hci.domotica;

public class RefrigeratorState{
    private Integer freezerTemperature;
    private Integer temperature;
    private String mode;

    public RefrigeratorState(String mode, Integer freezerTemperature, Integer temperature){
        this.freezerTemperature= freezerTemperature;
        this.mode= mode;
        this.temperature=temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setFreezerTemperature(Integer freezerTemperature) {
        this.freezerTemperature = freezerTemperature;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public Integer getFreezerTemperature() {
        return freezerTemperature;
    }
}


