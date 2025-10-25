/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

package org.cubicdev.plugin.amazingjoin.actions.types.broadcast;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;
import org.cubicdev.plugin.amazingjoin.utils.Utils;

public class BroadcastCenteredMessageAction extends Action {
    /*
    Credits to the SpigotMC user SirSpoodles for the centered messages code.
     */

    public BroadcastCenteredMessageAction(AmazingJoin main) {
        super(main, "broadcast_centered_message");
    }

    @Override
    public void execute(Player player, String args) {
        Component message = MiniMessage.miniMessage().deserialize(Utils.getCenteredMessage(player, PlaceholderAPI.setPlaceholders(player, args)));
        Bukkit.broadcast(message);
    }
}
