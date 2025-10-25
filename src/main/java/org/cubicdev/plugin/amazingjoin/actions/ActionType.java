package org.cubicdev.plugin.amazingjoin.actions;

/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

public enum ActionType {
    JOIN("join"),
    LEAVE("leave");

    private final String name;

    ActionType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
