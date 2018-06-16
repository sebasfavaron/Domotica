package com.itba.hci.domotica;

import java.util.ArrayList;

class Routine {
    private String name;
    private String id;
    private ArrayList<Action> actions;

    public Routine(String name, String id, ArrayList<Action> actions) {
        this.name = name;
        this.id = id;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
