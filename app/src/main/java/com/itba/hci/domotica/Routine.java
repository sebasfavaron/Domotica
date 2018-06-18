package com.itba.hci.domotica;

import java.util.ArrayList;

class Routine {
    private String name;
    private String id;
    private ArrayList<Action> actions;
    private String meta;

    public Routine(String name, String id, ArrayList<Action> actions) {
        this.name = name;
        this.id = id;
        this.actions = actions;
        this.meta = "{}";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getMeta() {
        return meta;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void addAction(Action action){
        getActions().add(action);
        return;
    }

}
