/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

package org.cubicdev.plugin.amazingjoin.formats;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;
import org.cubicdev.plugin.amazingjoin.actions.ActionSerializer;
import org.cubicdev.plugin.amazingjoin.managers.ConfigsManager;

import java.util.*;

public class FormatSerializer {
    private AmazingJoin main;
    private ActionSerializer actionSerializer;
    private ConfigsManager configsManager;
    private YamlConfiguration yamlConfiguration;

    public FormatSerializer(AmazingJoin main, ActionSerializer actionSerializer, ConfigsManager configsManager){
        this.main = main;
        this.configsManager = configsManager;
        this.yamlConfiguration = configsManager.findConfig("formats.yml").getYamlConfiguration();
        this.actionSerializer = actionSerializer;
    }

    public List<Format> deserializeFormats(){
        this.yamlConfiguration = configsManager.findConfig("formats.yml").getYamlConfiguration();
        List<Format> temporal = new ArrayList<>();

        ConfigurationSection formatSection = yamlConfiguration.getConfigurationSection("formats");

        if(formatSection == null){
            return List.of();
        }

        Set<String> formats = formatSection.getKeys(false);

        for(String formatName : formats){
            String permission = yamlConfiguration.
                    getString("formats."+formatName+".permission");

            LinkedHashMap<Action, String> joinActions = actionSerializer.deserializeActions(yamlConfiguration.
                    getStringList("formats."+formatName+".join-actions"));

            LinkedHashMap<Action, String> leaveActions = actionSerializer.deserializeActions(yamlConfiguration.
                    getStringList("formats."+formatName+".leave-actions"));

            Format newFormat = new Format(formatName, permission, joinActions, leaveActions);

            temporal.add(newFormat);
        }
        return temporal;
    }

    /***
     *
     * @param formatName
     * @param permission
     * @param joinActions
     * @param leaveActions
     * @return Whether it could be serialized or not
     */
    public boolean serializeFormat(String formatName, String permission){
        this.yamlConfiguration = configsManager.findConfig("formats.yml").getYamlConfiguration();

        if(yamlConfiguration.isConfigurationSection("formats."+formatName)){
            main.getLogger().warning("Could not create a new format. Reason: It already exists in the configuration.");
            return false;
        }

        yamlConfiguration.createSection("formats."+formatName);

        String rootFormatSectionPath = "formats."+formatName+".";
        ConfigurationSection permissionSection = yamlConfiguration.createSection(rootFormatSectionPath+"permission");
        ConfigurationSection joinActionsSection = yamlConfiguration.createSection(rootFormatSectionPath+"join-actions");
        ConfigurationSection leaveActionsSection = yamlConfiguration.createSection(rootFormatSectionPath+"leave-actions");

        yamlConfiguration.set(permissionSection.getCurrentPath(), permission);
        yamlConfiguration.set(joinActionsSection.getCurrentPath(), List.of("message;<red>Newly created format!"));
        yamlConfiguration.set(leaveActionsSection.getCurrentPath(), List.of("message;<red>Newly created format!"));

        configsManager.saveConfigs();

        return true;
    }
}
