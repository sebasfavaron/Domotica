package com.itba.hci.domotica;

import java.util.ArrayList;

public class GetRoutineResponse {
    private ArrayList<Routine> routines;

    public GetDevicesResponse(ArrayList<Routine> routines){
        this.routines = routines;
    }

    public ArrayList<Routine> getRoutines() {
        return routines;
    }

    public void setRoutines(ArrayList<Routine> routines) {
        this.routines = routines;
    }
}
