package com.itba.hci.domotica;

public class LampState{
    private String status;
    private String color;
    private Integer brightness;

    public LampState(String status, String color, Integer brightness){
        this.brightness= brightness;
        this.color = color;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public String getColor() {
        return color;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
