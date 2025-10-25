package org.cubicdev.plugin.amazingjoin.actions.types.broadcast;

/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */


import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;

public class BroadcastSoundAction extends Action {
    public BroadcastSoundAction(AmazingJoin main) {
        super(main, "broadcast_sound");
    }

    @Override
    public void execute(Player player, String args) {
        String[] parameters = args.split(";");

        String soundName = parameters[0];
        float volume = Float.parseFloat(parameters[1]);
        float pitch = Float.parseFloat(parameters[2]);

        Bukkit.getOnlinePlayers().forEach(capturedPlayer -> capturedPlayer.playSound(player.getLocation(), Sound.valueOf(soundName), volume, pitch));
    }
}
