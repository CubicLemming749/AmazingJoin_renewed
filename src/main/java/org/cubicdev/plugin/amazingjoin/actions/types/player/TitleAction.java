package org.cubicdev.plugin.amazingjoin.actions.types.player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;

import java.time.Duration;

public class TitleAction extends Action {
    //This action sends a title to the target player.
    public TitleAction(AmazingJoin main) {
        super(main, "title");
    }

    @Override
    public void execute(Player player, String args) {
        String[] parameters = args.split(";");

        if(parameters.length != 5){
            return;
        }

        String title = parameters[0];
        String subtitle = parameters[1];

        long fadeIn = Long.parseLong(parameters[2]) * 50;
        long stay = Long.parseLong(parameters[3]) * 50;
        long fadeOut = Long.parseLong(parameters[4]) * 50;

        Component titleComponent = (title.equals("none")) ?
                Component.empty() : MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, title));

        Component subtitleComponent = (subtitle.equals("none")) ?
                Component.empty() : MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, subtitle));

        Title.Times times = Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));

        Title finalTitleComponent = Title.title(titleComponent, subtitleComponent, times);

        player.showTitle(finalTitleComponent);
        //Example: title;subtitle;20;20;20
    }
}
