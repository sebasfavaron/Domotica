package com.itba.hci.domotica;

public class Device {
    private String name;
    private String id;
    private String typeId;
    private String meta;

    public Device(String name, String type){
        this.name = name;
        this.typeId = Device.typeToId(type);
        this.meta = "{}";
    }

    public Device(String name, String id, String typeId, String meta){
        this.name = name;
        this.id = id;
        this.typeId = typeId;
        this.meta = meta;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getMeta() {
        return meta;
    }

    public String getType() {
        return Device.idToType(typeId);
    }

    public String getTypeId() {
        return typeId;
    }

    public void setId(String id) { this.id = id; }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
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

    public static String translateEsToEn(String deviceName)
    {
        switch (deviceName) {
            case "Lámpara":
                return "lamp";
            case "Cortina":
                return "blind";
            case "Horno":
                return "oven";
            case "Aire acondicionado":
                return "ac";
            case "Puerta":
                return "door";
            case "Alarma":
                return "alarm";
            case "Temporizador":
                return "timer";
            case "Heladera":
                return "refrigerator";
            default:
                return deviceName.toLowerCase();
        }
    }

    public static String translateEnToEs(String deviceName)
    {
        switch (deviceName) {
            case "lamp":
                return "Lámpara";
            case "blind":
                return "Cortina";
            case "oven":
                return "Horno";
            case "ac":
                return "Aire acondicionado";
            case "door":
                return "Puerta";
            case "alarm":
                return "Alarma";
            case "timer":
                return "Temporizador";
            case "refrigerator":
                return "Heladera";
            default:
                return deviceName.toLowerCase();
        }
    }

}
