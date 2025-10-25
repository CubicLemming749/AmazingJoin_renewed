package org.cubicdev.plugin.amazingjoin.actions.types.player;

/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;

public class ConsoleCommandAction extends Action {
    //This actions executes a command from console.
    public ConsoleCommandAction(AmazingJoin main) {
        super(main, "console command");
    }

    @Override
    public void execute(Player player, String args) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, args));
    }
}
