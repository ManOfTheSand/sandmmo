package com.sandmmo.managers;

import com.sandmmo.classes.PlayerClass;
import com.sandmmo.config.ClassConfig;
import java.util.Map;

public class ClassManager {
    private final ClassConfig config;

    public ClassManager(ClassConfig config) {
        this.config = config;
    }

    public Map<String, PlayerClass> getClasses() {
        return config.getClasses();
    }
}