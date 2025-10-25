/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

package org.cubicdev.plugin.amazingjoin.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;

import java.io.InputStreamReader;

public class ConfigDefaults {
    private AmazingJoin main;

    public ConfigDefaults(AmazingJoin main){
        this.main = main;
    }

    public void setMainConfigDefaults(Config config){
        YamlConfiguration yamlConfig = config.getYamlConfiguration();

        yamlConfig.addDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(main.getResource("config.yml"))));

        yamlConfig.options().copyDefaults(true);
        config.saveConfig();
    }
}
