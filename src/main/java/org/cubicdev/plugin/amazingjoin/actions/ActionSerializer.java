package org.cubicdev.plugin.amazingjoin.actions;

import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.types.broadcast.*;
import org.cubicdev.plugin.amazingjoin.actions.types.other.PreloginAction;
import org.cubicdev.plugin.amazingjoin.actions.types.player.*;
import org.cubicdev.plugin.amazingjoin.actions.types.other.ConditionAction;

import java.util.*;

public class ActionSerializer {
    private AmazingJoin main;
    public ActionSerializer(AmazingJoin main){
        this.main = main;
    }


    public LinkedHashMap<Action, String> deserializeActions(List<String> actions){
        LinkedHashMap<Action, String> temporal = new LinkedHashMap<>();

        if(actions.isEmpty()){
            return new LinkedHashMap<>();
        }

        for(String action : actions){
            String[] args = action.split(";", 4);

            String actionType = args[0];
            String parameter = String.join(";", Arrays.copyOfRange(args, 1, args.length));

            temporal.put(getAction(actionType, parameter), parameter);
        }

        return temporal;
    }

    private Action getAction(String actionType, String parameter){

        switch (actionType){
            case "condition" -> {
                String conditionAction = parameter.split(";")[1];
                return new ConditionAction(main, getAction(conditionAction, null));
            }

            case "prelogin" -> {
                String action = parameter.split(";")[1];
                return new PreloginAction(main, getAction(action, null));
            }

            case "message" -> {
                return new MessageAction(main);
            }

            case "centered_message" -> {
                return new CenteredMessageAction(main);
            }

            case "title" -> {
                return new TitleAction(main);
            }

            case "actionbar" -> {
                return new ActionbarAction(main);
            }

            case "sound" -> {
                return new SoundAction(main);
            }

            case "console_command" -> {
                return new ConsoleCommandAction(main);
            }

            case "player" -> {
                return new PlayerAction(main);
            }

            case "teleport" -> {
                return new TeleportAction(main);
            }

            case "broadcast" -> {
                return new BroadcastMessageAction(main);
            }

            case "broadcast_centered" -> {
                return new BroadcastCenteredMessageAction(main);
            }

            case "broadcast_sound" -> {
                return new BroadcastSoundAction(main);
            }

            case "broadcast_title" -> {
                return new BroadcastTitleAction(main);
            }

            case "broadcast_actionbar" -> {
                return new BroadcastActionbarAction(main);
            }

            default -> {
                //nothing
            }
        }
        return null;
    }
}
