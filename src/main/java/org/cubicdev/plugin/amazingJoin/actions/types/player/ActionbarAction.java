package org.cubicdev.plugin.amazingJoin.actions.types.player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.actions.Action;

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
