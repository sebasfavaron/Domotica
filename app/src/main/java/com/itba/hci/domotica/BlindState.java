package com.itba.hci.domotica;

public class BlindState{
    private String status;
    private Integer level;

    public BlindState(String status, Integer level){
        this.level = level;
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public String getStatus() {
        return status;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
