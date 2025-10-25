/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

package org.cubicdev.plugin.amazingjoin.managers;

import org.cubicdev.plugin.amazingjoin.config.Config;

import java.util.HashSet;

public class ConfigsManager {
    private HashSet<Config> configs;

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
