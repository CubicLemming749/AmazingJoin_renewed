package org.cubicdev.plugin.amazingJoin.actions.types.player;

import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.actions.Action;
import org.cubicdev.plugin.amazingJoin.utils.Utils;

public class CenteredMessageAction extends Action {
    /*
    Credits to the SpigotMC user SirSpoodles for the centered messages code.
     */

    public CenteredMessageAction(AmazingJoin main) {
        super(main, "centered_message");
    }

    @Override
    public void execute(Player player, String args) {
        Utils.sendParsedMessage(player, Utils.getCenteredMessage(player, args));
    }
}

