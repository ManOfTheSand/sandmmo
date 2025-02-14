package com.sandcore.mmo.classes;

public class SoundSettings {
    private final String name;
    private final float volume;
    private final float pitch;

    public SoundSettings(String name, float volume, float pitch) {
        this.name = name;
        this.volume = volume;
        this.pitch = pitch;
    }

    public String getName() {
        return name;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
} 