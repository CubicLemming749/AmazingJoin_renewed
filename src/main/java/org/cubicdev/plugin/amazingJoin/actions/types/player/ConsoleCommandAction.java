package org.cubicdev.plugin.amazingJoin.actions.types.player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.actions.Action;

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
