package com.itba.hci.domotica;

public class DoorState{
    private String status;
    private String lock;

    public DoorState(String status, String lock){
        this.lock = lock;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public String getLock() {
        return lock;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }
}
