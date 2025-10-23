package org.cubicdev.plugin.amazingJoin.actions.types.broadcast;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.actions.Action;
import org.cubicdev.plugin.amazingJoin.utils.Utils;

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
