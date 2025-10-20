package org.cubicdev.plugin.amazingJoin.managers;

import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.config.Config;

import java.util.HashSet;

public class ConfigsManager {
    HashSet<Config> configs;

    public ConfigsManager(){
        configs = new HashSet<>();
    }

    public void addConfig(Config config){
        configs.add(config);
    }

    public Config findConfig(String name){
        return configs.stream().
                filter(config -> config.getName().equals(name)).
                findFirst().orElse(null);
    }

    public void saveConfigs(){
        configs.forEach(Config::saveConfig);
    }

    public void reloadConfigs(){
        configs.forEach(Config::loadConfig);
    }
}
