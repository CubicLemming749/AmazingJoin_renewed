package org.cubicdev.plugin.amazingJoin.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;

import java.io.File;
import java.io.IOException;

public class Config {
    private AmazingJoin plugin;
    private String name;
    private YamlConfiguration yamlConfiguration;

    public Config(AmazingJoin plugin, String name){
        this.plugin = plugin;
        this.name = name;
        loadConfig();
    }

    public void loadConfig(){
        File file = new File(plugin.getDataFolder(), name);

        if(!file.exists()){
            plugin.saveResource(name, false);
        }

        yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        plugin.getLogger().warning("Loading configuration file: "+name);
    }

    public void saveConfig(){
        File file = new File(plugin.getDataFolder(), name);

        try {
            yamlConfiguration.save(file);
            plugin.getLogger().warning("Saving configuration file: "+name);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save configuration file into disk: "+name);
        }
    }

    public String getName() {
        return name;
    }

    public YamlConfiguration getYamlConfiguration() {
        return yamlConfiguration;
    }
}
