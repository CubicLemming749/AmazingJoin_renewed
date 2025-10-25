package org.cubicdev.plugin.amazingjoin.managers;

/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.actions.Action;
import org.cubicdev.plugin.amazingjoin.actions.ActionType;
import org.cubicdev.plugin.amazingjoin.actions.types.other.ConditionAction;
import org.cubicdev.plugin.amazingjoin.actions.types.other.PreloginAction;
import org.cubicdev.plugin.amazingjoin.formats.Format;
import org.cubicdev.plugin.amazingjoin.formats.FormatSerializer;
import org.cubicdev.plugin.amazingjoin.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormatsManager {
    private FormatSerializer formatSerializer;
    private List<Format> formats;

    public FormatsManager(FormatSerializer formatSerializer){
        this.formatSerializer = formatSerializer;
        this.formats = formatSerializer.deserializeFormats();
    }

    public Format determinePlayerFormat(Player player){
        if(formats.isEmpty()){
            return null;
        }

        for(Format format : formats){
            if(player.hasPermission(format.getPermission())){
                return format;
            }

            if(format.getPermission().equals("default")){
                return format;
            }
        }

        return null;
    }

    public Format findFormat(String name){
        return formats.
                stream().
                filter(format -> format.getName().equals(name)).
                findFirst().
                orElse(null);
    }

    public void executeActions(Player player, Format format, ActionType actionType){
        HashMap<Action, String> actionsMap = new HashMap<>();

        if(actionType == ActionType.ENTER){
             actionsMap = format.getJoinActionsMap();
        }

        if(actionType == ActionType.LEAVE){
            actionsMap = format.getLeaveActionsMap();
        }

        for(Map.Entry<Action, String> entry : actionsMap.entrySet()) {
            Action action = entry.getKey();

            if(action instanceof PreloginAction) continue;

            if(action instanceof ConditionAction condition){
                boolean result = condition.check(player, entry.getValue().split(";")[0]);

                if(!result){
                    Utils.sendDebug("Condition '"+entry.getValue().split(";")[0]+"' failed to player "+player.getName());
                    continue;
                }else{
                    condition.execute(player, entry.getValue().split(";")[2]);;
                    action.log(player, entry.getValue());
                }

                continue;
            }

            action.execute(player, entry.getValue());
            action.log(player, entry.getValue());
        }
    }

    public void executePreLoginActions(Player player, Format format){
        HashMap<Action, String> actionsMap;

        actionsMap = format.getJoinActionsMap();

        for(Map.Entry<Action, String> entry : actionsMap.entrySet()) {
            Action action = entry.getKey();
            PreloginAction preloginAction;

            if(!(action instanceof PreloginAction)) continue;

            preloginAction = (PreloginAction) action;

            if(preloginAction.getAction() instanceof ConditionAction condition){
                boolean result = condition.check(player, entry.getValue().split(";")[0]);

                if(!result){
                    Utils.sendDebug("Condition '"+entry.getValue().split(";")[0]+"' failed to player "+player.getName());
                    continue;
                }else{
                    condition.execute(player, entry.getValue().split(";")[2]);;
                    action.log(player, entry.getValue());
                }

                continue;
            }

            preloginAction.execute(player, entry.getValue());
            preloginAction.log(player, entry.getValue());
        }
    }

    public void reloadFormats(){
        this.formats = formatSerializer.deserializeFormats();
    }

    public boolean createFormat(String formatName, String permission){
        return formatSerializer.serializeFormat(formatName, permission);
    }

    public List<Format> getFormats() {
        return formats;
    }
}
