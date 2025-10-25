package org.cubicdev.plugin.amazingjoin.actions.types.player;

/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;
import org.cubicdev.plugin.amazingjoin.utils.Utils;

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
