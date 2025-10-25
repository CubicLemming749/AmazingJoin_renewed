package org.cubicdev.plugin.amazingjoin.actions.types.player;

/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;

public class ActionbarAction extends Action {

    public ActionbarAction(AmazingJoin main) {
        super(main, "actionbar");
    }

    @Override
    public void execute(Player player, String args) {
        Component component = MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, args));
        player.sendActionBar(component);
    }
}
