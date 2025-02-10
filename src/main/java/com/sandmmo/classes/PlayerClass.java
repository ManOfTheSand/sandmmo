package com.sandmmo.classes;

public class PlayerClass {
    private final String name;
    private final String displayName;

    public PlayerClass(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
}