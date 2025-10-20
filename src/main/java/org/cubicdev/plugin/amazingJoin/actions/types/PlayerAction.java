package org.cubicdev.plugin.amazingJoin.actions.types;

import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.actions.Action;

public class PlayerAction extends Action {
    //This actions makes the player to execute a command or message.
    public PlayerAction(AmazingJoin main) {
        super(main, "player");
    }

    @Override
    public void execute(Player player, String args) {

        if(args.startsWith("/")){
            player.performCommand(args);
            return;
        }

        player.chat(args);

    }
    //This makes the player to execute a command or chat a message.
}
