package com.itba.hci.domotica;

public class AcState{
    private String status;
    private Integer temperature;
    private String mode;
    private String verticalSwing;
    private String horizontalSwing;
    private String fanSpeed;

    public AcState(String status, Integer temperature, String mode, String verticalSwing, String horizontalSwing, String fanSpeed){
        this.fanSpeed= fanSpeed;
        this.horizontalSwing= horizontalSwing;
        this.mode =mode;
        this.status =status;
        this.temperature= temperature;
        this.verticalSwing=verticalSwing;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setFanSpeed(String fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public void setHorizontalSwing(String horizontalSwing) {
        this.horizontalSwing = horizontalSwing;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public void setVerticalSwing(String verticalSwing) {
        this.verticalSwing = verticalSwing;
    }

    public String getStatus() {
        return status;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public String getFanSpeed() {
        return fanSpeed;
    }

    public String getHorizontalSwing() {
        return horizontalSwing;
    }

    public String getMode() {
        return mode;
    }

    public String getVerticalSwing() {
        return verticalSwing;
    }

}
