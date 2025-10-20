package org.cubicdev.plugin.amazingJoin.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.managers.ConfigsManager;

public class Utils {

    private static Utils instance;
    private ConfigsManager configsManager;
    private YamlConfiguration yamlConfiguration;

    public Utils(ConfigsManager configsManager){
        this.configsManager = configsManager;
        this.yamlConfiguration = configsManager.findConfig("config.yml").getYamlConfiguration();

        instance = this;
    }

    public static void sendParsedMessage(CommandSender commandSender, String msg){
        if(!(commandSender instanceof Player)){
            Component message = MiniMessage.miniMessage().deserialize(msg, Placeholder.parsed("prefix", instance.yamlConfiguration.getString("config.language.prefix")));
            commandSender.sendMessage(message);
            return;
        }

        Player player;
        player = (Player) commandSender;

        Component message = MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, msg), Placeholder.parsed("prefix", instance.yamlConfiguration.getString("config.language.prefix")));
        player.sendMessage(message);
    }

    public static void sendDebug(String msg){
        boolean isDebug = instance.yamlConfiguration.getBoolean("config.debug");

        if(isDebug){
            AmazingJoin.logger.warning(msg);
        }
    }
}
