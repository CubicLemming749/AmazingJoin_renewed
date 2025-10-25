/*
    MIT License

    Copyright (c) 2025 CubicLemming749

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/

package org.cubicdev.plugin.amazingjoin;

import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubicdev.plugin.amazingjoin.actions.ActionSerializer;
import org.cubicdev.plugin.amazingjoin.commands.AmazingJoinCommand;
import org.cubicdev.plugin.amazingjoin.config.Config;
import org.cubicdev.plugin.amazingjoin.config.ConfigDefaults;
import org.cubicdev.plugin.amazingjoin.formats.FormatSerializer;
import org.cubicdev.plugin.amazingjoin.listener.PlayerListener;
import org.cubicdev.plugin.amazingjoin.listener.hooks.Authme;
import org.cubicdev.plugin.amazingjoin.listener.hooks.nLogin;
import org.cubicdev.plugin.amazingjoin.managers.ConfigsManager;
import org.cubicdev.plugin.amazingjoin.managers.FormatsManager;
import org.cubicdev.plugin.amazingjoin.utils.Utils;

import java.util.logging.Logger;

public final class AmazingJoin extends JavaPlugin {

    public static Logger logger;

    private FormatsManager formatsManager;
    private ConfigsManager configsManager;

    private ConfigDefaults configDefaults;

    private ActionSerializer actionSerializer;
    private FormatSerializer formatSerializer;

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
        getLogger().warning("The plugin has been disabled.");
        getLogger().warning("Thanks for using it! :D");
        configsManager.saveConfigs();
    }

    public void init(){
        configsManager = new ConfigsManager();

        Config mainConfig = new Config(this, "config.yml");
        Config formatsConfig = new Config(this, "formats.yml");

        configsManager.addConfig(mainConfig);
        configsManager.addConfig(formatsConfig);

        configDefaults = new ConfigDefaults(this);
        configDefaults.setMainConfigDefaults(mainConfig);

        actionSerializer = new ActionSerializer(this);
        formatSerializer = new FormatSerializer(this, actionSerializer, configsManager);

        formatsManager = new FormatsManager(formatSerializer);

        new Utils(configsManager);

        UpdateChecker updateChecker = new UpdateChecker();
        updateChecker.checkVersion();
    }

    public void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        YamlConfiguration configuration = configsManager.findConfig("config.yml").getYamlConfiguration();

        if(pm.isPluginEnabled("AuthMe") && configuration.getBoolean("config.hooks.authme")){
            pm.registerEvents(new Authme(formatsManager, configsManager), this);
            getLogger().warning("Loaded hook: AuthMe");
            return;
        }

        if(pm.isPluginEnabled("nLogin") && configuration.getBoolean("config.hooks.nlogin")){
            pm.registerEvents(new nLogin(formatsManager, configsManager), this);
            getLogger().warning("Loaded hook: nLogin");
            return;
        }

        pm.registerEvents(new PlayerListener(formatsManager, configsManager), this);
    }

    public void registerCommands(){
        AmazingJoinCommand amazingJoinCommand = new AmazingJoinCommand(this, formatsManager, configsManager, configDefaults);
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
