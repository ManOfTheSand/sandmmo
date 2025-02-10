package com.sandmmo.managers;

import com.sandmmo.classes.PlayerClass;
import com.sandmmo.config.ClassConfig;

import java.util.Map;

public class ClassManager {
    private final ClassConfig classConfig;
    private final Map<String, PlayerClass> classes;

    public ClassManager(ClassConfig classConfig) {
        this.classConfig = classConfig;
        this.classes = classConfig.getClasses();
    }

    public PlayerClass getClass(String className) {
        return classes.get(className);
    }

    public String[] getAvailableClasses() {
        return classes.keySet().toArray(new String[0]);
    }
}