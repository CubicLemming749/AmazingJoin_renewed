package org.cubicdev.plugin.amazingJoin.managers;

import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.actions.Action;
import org.cubicdev.plugin.amazingJoin.actions.ActionType;
import org.cubicdev.plugin.amazingJoin.actions.types.ConditionAction;
import org.cubicdev.plugin.amazingJoin.formats.Format;
import org.cubicdev.plugin.amazingJoin.formats.FormatSerializer;
import org.cubicdev.plugin.amazingJoin.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormatsManager {
    FormatSerializer formatSerializer;
    List<Format> formats;

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

    public void executeFormatForPlayer(Player player, Format format, ActionType actionType){
        HashMap<Action, String> actionsMap = new HashMap<>();

        if(actionType == ActionType.ENTER){
             actionsMap = format.getJoinActionsMap();
        }

        if(actionType == ActionType.LEAVE){
            actionsMap = format.getLeaveActionsMap();
        }

        for(Map.Entry<Action, String> entry : actionsMap.entrySet()) {
            Action action = entry.getKey();

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
