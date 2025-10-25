package org.cubicdev.plugin.amazingjoin.formats;

/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

import org.cubicdev.plugin.amazingjoin.actions.Action;

import java.util.LinkedHashMap;

public class Format {
    private String name;
    private String permission;
    private LinkedHashMap<Action, String> joinActionsMap;
    private LinkedHashMap<Action, String> leaveActionsMap;

    public Format(String name, String permission, LinkedHashMap<Action, String> joinActionsMap, LinkedHashMap<Action, String> leaveActionsMap){
        this.name = name;
        this.permission = permission;
        this.joinActionsMap = joinActionsMap;
        this.leaveActionsMap = leaveActionsMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public LinkedHashMap<Action, String> getJoinActionsMap() {
        return joinActionsMap;
    }

    public void setJoinActionsList(LinkedHashMap<Action, String> newJoinActionsMap) {
        this.joinActionsMap = newJoinActionsMap;
    }

    public LinkedHashMap<Action, String> getLeaveActionsMap() {
        return leaveActionsMap;
    }

    public void setLeaveActionsList(LinkedHashMap<Action, String> newLeaveActionsMap) {
        this.leaveActionsMap = newLeaveActionsMap;
    }
}
