/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

package org.cubicdev.plugin.amazingjoin.actions.types.broadcast;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;

public class BroadcastActionbarAction extends Action {
    public BroadcastActionbarAction(AmazingJoin main) {
        super(main, "broadcast_actionbar");
    }

    @Override
    public void execute(Player player, String args) {
        Component component = MiniMessage.miniMessage().deserialize(args);
        Bukkit.getOnlinePlayers().forEach(capturedPlayer -> capturedPlayer.sendActionBar(component));
    }
}
