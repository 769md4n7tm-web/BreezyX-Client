package com.breezyx;

public class BreezyXModules {

    public static boolean fpsCounter = false;
    public static boolean coordinatesHud = false;
    public static boolean armorDisplay = false;

    public static boolean isEnabled(String name) {
        return switch (name) {
            case "FPS Counter" -> fpsCounter;
            case "Coordinates HUD" -> coordinatesHud;
            case "Armor Display" -> armorDisplay;
            default -> false;
        };
    }

    public static void setEnabled(String name, boolean enabled) {
        switch (name) {
            case "FPS Counter" -> fpsCounter = enabled;
            case "Coordinates HUD" -> coordinatesHud = enabled;
            case "Armor Display" -> armorDisplay = enabled;
        }
    }
}
