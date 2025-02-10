package com.sandmmo.classes;

public class PlayerClass {
    private final String id;
    private final String displayName;
    private final String color;

    public PlayerClass(String id, String displayName, String color) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
    }

    // Getters
    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getColor() { return color; }
}