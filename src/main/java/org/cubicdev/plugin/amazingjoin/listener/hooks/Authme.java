package org.cubicdev.plugin.amazingjoin.listener.hooks;

import fr.xephi.authme.events.LoginEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.cubicdev.plugin.amazingjoin.UpdateChecker;
import org.cubicdev.plugin.amazingjoin.actions.ActionType;
import org.cubicdev.plugin.amazingjoin.formats.Format;
import org.cubicdev.plugin.amazingjoin.managers.ConfigsManager;
import org.cubicdev.plugin.amazingjoin.managers.FormatsManager;
import org.cubicdev.plugin.amazingjoin.utils.Utils;

public class Authme implements Listener {
    private FormatsManager formatsManager;
    private ConfigsManager configsManager;
    private YamlConfiguration yamlConfiguration;

    public Authme(FormatsManager formatsManager, ConfigsManager configsManager){
        this.formatsManager = formatsManager;
        this.configsManager = configsManager;
        this.yamlConfiguration = configsManager.findConfig("config.yml").getYamlConfiguration();
    }

    @EventHandler
    public void onLogin(LoginEvent e){
        this.yamlConfiguration = configsManager.findConfig("formats.yml").getYamlConfiguration();
        Player player = e.getPlayer();

        if(player.hasPermission("amazingjoin.notify.updates") && UpdateChecker.IS_NEW_VERSION){
            Utils.sendParsedMessage(player, configsManager.findConfig("config.yml").getYamlConfiguration().getString("config.language.new_version_message").replace("<version>", UpdateChecker.NEW_VERSION));
        }

        Format playerFormat = formatsManager.determinePlayerFormat(player);

        if(playerFormat == null){
            return;
        }

        formatsManager.executeActions(player, playerFormat, ActionType.ENTER);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        this.yamlConfiguration = configsManager.findConfig("formats.yml").getYamlConfiguration();
        Player player = e.getPlayer();
        Format playerFormat = formatsManager.determinePlayerFormat(player);

        if(playerFormat == null){
            return;
        }

        formatsManager.executeActions(player, playerFormat, ActionType.LEAVE);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        this.yamlConfiguration = configsManager.findConfig("formats.yml").getYamlConfiguration();
        Player player = e.getPlayer();

        Format playerFormat = formatsManager.determinePlayerFormat(player);

        if(playerFormat == null){
            return;
        }

        formatsManager.executePreLoginActions(player, playerFormat);
    }
}
