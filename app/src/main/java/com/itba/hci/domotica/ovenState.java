package com.itba.hci.domotica;

public class ovenState {
    private String status;
    private String heat;
    private String grill;
    private String convection;

    public ovenState(String status,String heat,String grill, String convection){
        this.status = status;
        this.convection= convection;
        this.grill = grill;
        this.heat = heat;
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
