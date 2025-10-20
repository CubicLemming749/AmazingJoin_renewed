package org.cubicdev.plugin.amazingJoin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubicdev.plugin.amazingJoin.actions.ActionSerializer;
import org.cubicdev.plugin.amazingJoin.commands.AmazingJoinCommand;
import org.cubicdev.plugin.amazingJoin.config.Config;
import org.cubicdev.plugin.amazingJoin.formats.FormatSerializer;
import org.cubicdev.plugin.amazingJoin.listener.PlayerListener;
import org.cubicdev.plugin.amazingJoin.listener.hooks.Authme;
import org.cubicdev.plugin.amazingJoin.listener.hooks.nLogin;
import org.cubicdev.plugin.amazingJoin.managers.ConfigsManager;
import org.cubicdev.plugin.amazingJoin.managers.FormatsManager;
import org.cubicdev.plugin.amazingJoin.utils.Utils;

import java.util.logging.Logger;

public final class AmazingJoin extends JavaPlugin {

    public static Logger logger;

    FormatsManager formatsManager;
    ConfigsManager configsManager;

    ActionSerializer actionSerializer;
    FormatSerializer formatSerializer;

    public static String PLUGIN_VERSION;
    @Override
    public void onEnable() {
        logger = getLogger();
        PLUGIN_VERSION = getDescription().getVersion();

        getLogger().warning("Initializing plugin...");
        getLogger().warning("This plugin uses bStats to collect data from your server.");

        printMessage();

        init();
        registerEvents();
        registerCommands();

        int pluginId = 27646;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        configsManager.saveConfigs();
    }

    public void init(){
        configsManager = new ConfigsManager();

        Config mainConfig = new Config(this, "config.yml");
        Config formatsConfig = new Config(this, "formats.yml");

        configsManager.addConfig(mainConfig);
        configsManager.addConfig(formatsConfig);

        actionSerializer = new ActionSerializer(this);
        formatSerializer = new FormatSerializer(this, actionSerializer, configsManager);

        formatsManager = new FormatsManager(formatSerializer);

        new Utils(configsManager);
    }

    public void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        YamlConfiguration configuration = configsManager.findConfig("config.yml").getYamlConfiguration();

        if(pm.isPluginEnabled("AuthMe") && configuration.getBoolean("config.hooks.authme")){
            pm.registerEvents(new Authme(formatsManager, configsManager), this);
            return;
        }

        if(pm.isPluginEnabled("nLogin") && configuration.getBoolean("config.hooks.nlogin")){
            pm.registerEvents(new nLogin(formatsManager, configsManager), this);
            return;
        }

        pm.registerEvents(new PlayerListener(formatsManager, configsManager), this);
    }

    public void registerCommands(){
        AmazingJoinCommand amazingJoinCommand = new AmazingJoinCommand(this, formatsManager, configsManager);
        amazingJoinCommand.setupCommand();
    }

    public void printMessage(){
        getLogger().warning("----------------------[AMAZINGJOIN]----------------------");
        getLogger().warning("• Plugin version: "+PLUGIN_VERSION);
        getLogger().warning("• Server coolness: 100%");
        getLogger().warning("• Successfully initialized plugin, enjoy!");
        getLogger().warning("");
        getLogger().warning("• Created with love by CubicLemming749. <3");
        getLogger().warning("----------------------[AMAZINGJOIN]----------------------");
    }
}
