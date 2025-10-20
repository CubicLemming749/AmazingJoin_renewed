package org.cubicdev.plugin.amazingJoin.actions.types.player;

import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.actions.Action;
import org.cubicdev.plugin.amazingJoin.utils.Utils;

public class MessageAction extends Action {
    //This action sends a message to the target player.
    public MessageAction(AmazingJoin main) {
        super(main,"message");
    }

    @Override
    public void execute(Player player, String args) {
        Utils.sendParsedMessage(player, args);
    }
}
