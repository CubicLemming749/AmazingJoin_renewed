package org.cubicdev.plugin.amazingjoin.actions.types.player;

/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;

public class SoundAction extends Action {
    //This action sends a sound to the target player.
    public SoundAction(AmazingJoin main) {
        super(main, "sound");
    }

    @Override
    public void execute(Player player, String args) {
        String[] parameters = args.split(";");

        String soundName = parameters[0];
        float volume = Float.parseFloat(parameters[1]);
        float pitch = Float.parseFloat(parameters[2]);

        player.playSound(player.getLocation(), Sound.valueOf(soundName), volume, pitch);

        //example: ENTITY_ENDER_DRAGON_GROWL;1;1
    }
}
