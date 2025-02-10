package com.sandmmo.data;

import java.util.List;

public class ClassData {
    private String displayName;
    private List<String> description;
    private String icon;
    // Add more fields as needed

    public ClassData(String displayName, List<String> description, String icon) {
        this.displayName = displayName;
        this.description = description;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}