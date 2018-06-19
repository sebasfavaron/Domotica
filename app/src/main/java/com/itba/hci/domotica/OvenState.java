package com.itba.hci.domotica;

public class OvenState{
    private String status;
    private String heat;
    private String grill;
    private String convection;
    private Integer temperature;

    public OvenState(Integer temperature,String status, String heat, String grill, String convection){
        this.status = status;
        this.convection= convection;
        this.grill = grill;
        this.heat = heat;
        this.temperature = temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getTemperature() {
        return temperature;
    }


    public String getStatus() {
        return status;
    }

    public String getConvection() {
        return convection;
    }

    public String getGrill() {
        return grill;
    }

    public String getHeat() {
        return heat;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setConvection(String convection) {
        this.convection = convection;
    }

    public void setGrill(String grill) {
        this.grill = grill;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }
}
