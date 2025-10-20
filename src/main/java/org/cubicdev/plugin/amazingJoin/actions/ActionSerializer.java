package org.cubicdev.plugin.amazingJoin.actions;

import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.actions.types.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ActionSerializer {
    AmazingJoin main;
    public ActionSerializer(AmazingJoin main){
        this.main = main;
    }


    public HashMap<Action, String> deserializeActions(List<String> actions){
        HashMap<Action, String> temporal = new HashMap<>();

        if(actions.isEmpty()){
            return new HashMap<>();
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

            case "message" -> {
                return new MessageAction(main);
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
                return new BroadcastAction(main);
            }

            case "title" -> {
                return new TitleAction(main);
            }

            case "actionbar" -> {
                return new ActionbarAction(main);
            }

            default -> {
                //nothing
            }
        }
        return null;
    }
}
