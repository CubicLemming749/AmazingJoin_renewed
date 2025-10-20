package org.cubicdev.plugin.amazingJoin.listener.hooks;

import com.nickuc.login.api.event.bukkit.auth.AuthenticateEvent;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.cubicdev.plugin.amazingJoin.actions.ActionType;
import org.cubicdev.plugin.amazingJoin.formats.Format;
import org.cubicdev.plugin.amazingJoin.managers.ConfigsManager;
import org.cubicdev.plugin.amazingJoin.managers.FormatsManager;

public class nLogin implements Listener {
    FormatsManager formatsManager;
    ConfigsManager configsManager;
    YamlConfiguration yamlConfiguration;

    public nLogin(FormatsManager formatsManager, ConfigsManager configsManager){
        this.formatsManager = formatsManager;
        this.configsManager = configsManager;
        this.yamlConfiguration = configsManager.findConfig("config.yml").getYamlConfiguration();
    }

    @EventHandler
    public void onLogin(AuthenticateEvent e){
        this.yamlConfiguration = configsManager.findConfig("formats.yml").getYamlConfiguration();
        Player player = e.getPlayer();
        Format playerFormat = formatsManager.determinePlayerFormat(player);

        if(playerFormat == null){
            return;
        }

        formatsManager.executeFormatForPlayer(player, playerFormat, ActionType.ENTER);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        this.yamlConfiguration = configsManager.findConfig("formats.yml").getYamlConfiguration();
        Player player = e.getPlayer();
        Format playerFormat = formatsManager.determinePlayerFormat(player);

        if(playerFormat == null){
            return;
        }

        formatsManager.executeFormatForPlayer(player, playerFormat, ActionType.LEAVE);
    }
}
