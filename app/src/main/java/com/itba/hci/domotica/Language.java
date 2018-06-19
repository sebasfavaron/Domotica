package com.itba.hci.domotica;

public final class Language {
    public static String spanishToEnglish(String word){
        switch (word)
        {
            case "Convencional":
                return "Conventional";
            case "Mínimo":
                return "Bottom";
            case "Máximo":
                return "Top";
            case "Grande":
                return "Large";
            case "Economico":
                return "Eco";
            case "Apagado":
                return "Off";
            case "Frio":
                return "Cool";
            case "Calor":
                return "Heat";
            case "Ventilador":
                return "Fan";
            case "Automatico":
                return "Auto";
            case "Predeterminado":
                return "Default";
            case "Vacaciones":
                return "Vacation";
            case "Fiesta":
                return "Party";
            default:
                return word;

        }
    }
}
