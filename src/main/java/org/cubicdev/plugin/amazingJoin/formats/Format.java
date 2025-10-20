package org.cubicdev.plugin.amazingJoin.formats;

import org.cubicdev.plugin.amazingJoin.actions.Action;

import java.util.HashMap;

public class Format {
    private String name;
    private String permission;
    private HashMap<Action, String> joinActionsMap;
    private HashMap<Action, String> leaveActionsMap;

    public Format(String name, String permission, HashMap<Action, String> joinActionsMap, HashMap<Action, String> leaveActionsMap){
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

    public HashMap<Action, String> getJoinActionsMap() {
        return joinActionsMap;
    }

    public void setJoinActionsList(HashMap<Action, String> newJoinActionsMap) {
        this.joinActionsMap = newJoinActionsMap;
    }

    public HashMap<Action, String> getLeaveActionsMap() {
        return leaveActionsMap;
    }

    public void setLeaveActionsList(HashMap<Action, String> newLeaveActionsMap) {
        this.leaveActionsMap = newLeaveActionsMap;
    }
}
