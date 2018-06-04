package com.itba.hci.domotica;

public class Device {
    private String name;
    private String id;
    private String type;
    private String typeId;
    private String meta;

    public Device(String name, String type){
        this.name = name;
        this.type = type;
        this.typeId = Device.typeToId(type);
        this.meta = "";
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public static String typeToId(String type){
        switch (type) {
            case "lamp":
                return "go46xmbqeomjrsjr";
            case "blind":
                return "eu0v2xgprrhhg41g";
            case "oven":
                return "im77xxyulpegfmv8";
            case "ac":
                return "li6cbv5sdlatti0j";
            case "door":
                return "lsf78ly0eqrjbz91";
            case "alarm":
                return "mxztsyjzsrq7iaqc";
            case "timer":
                return "ofglvd9gqX8yfl3l";
            case "refrigerator":
                return "rnizejqr2di0okho";
            default:
                return "not_an_id";
        }
    }
    public static String idToType(String id){
        switch (id) {
            case "go46xmbqeomjrsjr":
                return "lamp";
            case "eu0v2xgprrhhg41g":
                return "blind";
            case "im77xxyulpegfmv8":
                return "oven";
            case "li6cbv5sdlatti0j":
                return "ac";
            case "lsf78ly0eqrjbz91":
                return "door";
            case "mxztsyjzsrq7iaqc":
                return "alarm";
            case "ofglvd9gqX8yfl3l":
                return "timer";
            case "rnizejqr2di0okho":
                return "refrigerator";
            default:
                return "not_a_type";
        }
    }
}
