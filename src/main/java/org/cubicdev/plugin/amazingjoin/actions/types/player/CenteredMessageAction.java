package org.cubicdev.plugin.amazingjoin.actions.types.player;

/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;
import org.cubicdev.plugin.amazingjoin.utils.Utils;

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

