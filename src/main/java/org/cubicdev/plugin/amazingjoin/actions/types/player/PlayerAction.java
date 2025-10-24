package org.cubicdev.plugin.amazingjoin.actions.types.player;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;

public class PlayerAction extends Action {
    //This actions makes the player to execute a command or message.
    public PlayerAction(AmazingJoin main) {
        super(main, "player");
    }

    @Override
    public void execute(Player player, String args) {

        if(args.startsWith("/")){
            player.performCommand(PlaceholderAPI.setPlaceholders(player, args));
            return;
        }

        player.chat(PlaceholderAPI.setPlaceholders(player, args));

    }
    //This makes the player to execute a command or chat a message.
}
