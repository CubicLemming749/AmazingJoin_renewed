package org.cubicdev.plugin.amazingJoin.actions.types.broadcast;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.actions.Action;

public class BroadcastMessageAction extends Action {
    //This action sends a message to all the server
    public BroadcastMessageAction(AmazingJoin main) {
        super(main, "broadcast");
    }

    @Override
    public void execute(Player player, String args) {
        Component message = MiniMessage.miniMessage().deserialize(args, Placeholder.parsed("player", player.getName()));
        Bukkit.broadcast(message);
    }
}
