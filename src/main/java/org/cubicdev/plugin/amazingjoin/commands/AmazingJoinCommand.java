/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

package org.cubicdev.plugin.amazingjoin.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.ActionType;
import org.cubicdev.plugin.amazingjoin.config.ConfigDefaults;
import org.cubicdev.plugin.amazingjoin.formats.Format;
import org.cubicdev.plugin.amazingjoin.managers.ConfigsManager;
import org.cubicdev.plugin.amazingjoin.managers.FormatsManager;
import org.cubicdev.plugin.amazingjoin.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class AmazingJoinCommand {
    private AmazingJoin main;
    private FormatsManager formatsManager;
    private ConfigsManager configsManager;
    private ConfigDefaults defaults;

    public AmazingJoinCommand(AmazingJoin main, FormatsManager formatsManager, ConfigsManager configsManager, ConfigDefaults defaults){
        this.main = main;
        this.formatsManager = formatsManager;
        this.configsManager = configsManager;
        this.defaults = defaults;
    }

    public void setupCommand(){
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("amazingjoin")
                .requires(executor -> executor.getExecutor() == null || executor.getExecutor().hasPermission("amazingjoin.command"))
                .then(Commands.literal("create").
                        requires(executor -> executor.getExecutor() == null || executor.getExecutor().hasPermission("amazingjoin.command.create")).
                        then(Commands.argument("name", StringArgumentType.word()).
                                then(Commands.argument("permission", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            String newFormatName = ctx.getArgument("name", String.class);
                                            String newFormatPermission = ctx.getArgument("permission", String.class);

                                            boolean successful = formatsManager.createFormat(newFormatName, newFormatPermission);

                                            if(!successful){
                                                Utils.sendParsedMessage(ctx.getSource().getSender(), configsManager.findConfig("config.yml").getYamlConfiguration().getString("config.language.format_cannot_be_created"));
                                                return 0;
                                            }

                                            Utils.sendParsedMessage(ctx.getSource().getSender(), configsManager.findConfig("config.yml").getYamlConfiguration().getString("config.language.format_created").replace("<name>", newFormatName));
                                            return Command.SINGLE_SUCCESS;
                                        })))
                )

                .then(Commands.literal("test")
                        .requires(executor -> executor.getExecutor() == null || executor.getExecutor().hasPermission("amazingjoin.command.test"))
                        .then(Commands.argument("format", StringArgumentType.word())
                                .suggests(this::getSuggestions)
                                .then(Commands.literal("join").
                                        executes(ctx -> {
                                            CommandSender sender = ctx.getSource().getSender();
                                            String formatName = ctx.getArgument("format", String.class);
                                            Format formatToTest = formatsManager.findFormat(formatName);

                                            if(!(sender instanceof Player player)){
                                                return 0;
                                            }

                                            if(formatToTest == null){
                                                Utils.sendParsedMessage(player, configsManager.findConfig("config.yml").getYamlConfiguration().getString("config.language.format_not_found"));
                                                return 0;
                                            }

                                            Utils.sendParsedMessage(player, configsManager.findConfig("config.yml").getYamlConfiguration().getString("config.language.format_testing_success"));
                                            formatsManager.executeActions(player, formatToTest, ActionType.ENTER);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .then(Commands.literal("leave")
                                        .executes(ctx -> {
                                            CommandSender sender = ctx.getSource().getSender();
                                            String formatName = ctx.getArgument("format", String.class);
                                            Format formatToTest = formatsManager.findFormat(formatName);

                                            if(!(sender instanceof Player player)){
                                                return 0;
                                            }

                                            if(formatToTest == null){
                                                Utils.sendParsedMessage(player, configsManager.findConfig("config.yml").getYamlConfiguration().getString("config.language.format_not_found"));
                                                return 0;
                                            }

                                            Utils.sendParsedMessage(player, configsManager.findConfig("config.yml").getYamlConfiguration().getString("config.language.format_testing_success"));
                                            formatsManager.executeActions(player, formatToTest, ActionType.LEAVE);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                )

                .then(Commands.literal("addaction")
                        .then(Commands.argument("format", StringArgumentType.word())
                                .requires(executor -> executor.getExecutor() == null || executor.getExecutor().hasPermission("amazingjoin.command.addaction"))
                                .suggests(this::getSuggestions)
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .suggests((commandContext, suggestionsBuilder) -> {
                                            List.of("join", "leave").stream().
                                                    filter(userEntry -> userEntry.toLowerCase().startsWith(suggestionsBuilder.getRemainingLowerCase()))
                                                    .forEach(suggestionsBuilder::suggest);

                                            return suggestionsBuilder.buildFuture();
                                        })
                                        .then(Commands.argument("action", StringArgumentType.greedyString())
                                                .executes(ctx -> addAction(ctx, ctx.getArgument("type", String.class))))))
                )

                .then(Commands.literal("help")
                        .requires(executor -> executor.getExecutor() == null || executor.getExecutor().hasPermission("amazingjoin.command.help"))
                        .executes(commandContext -> {
                            YamlConfiguration config = configsManager.findConfig("config.yml").getYamlConfiguration();
                            CommandSender commandSender = commandContext.getSource().getSender();

                            List<String> helpMessage = config.getStringList("config.language.help_message");

                            helpMessage.forEach(message -> {
                                Utils.sendParsedMessage(commandSender, message);
                            });

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("reload")
                        .requires(executor -> executor.getExecutor() == null || executor.getExecutor().hasPermission("amazingjoin.command.reload"))
                        .executes(commandContext -> {
                            configsManager.reloadConfigs();
                            formatsManager.reloadFormats();
                            defaults.setMainConfigDefaults(configsManager.findConfig("config.yml"));

                            new Utils(configsManager);

                            Utils.sendParsedMessage(commandContext.getSource().getSender(), configsManager.findConfig("config.yml").getYamlConfiguration().getString("config.language.reload_success"));
                            return Command.SINGLE_SUCCESS;
                }));

        LiteralCommandNode<CommandSourceStack> command = root.build();

        this.main.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(command, List.of("aj", "ajoin"));
        });
    }

    public int addAction(CommandContext<CommandSourceStack> ctx, String type){
        YamlConfiguration yamlConfiguration = configsManager.findConfig("formats.yml").getYamlConfiguration();
        String action = ctx.getArgument("action", String.class);
        String formatName = ctx.getArgument("format", String.class);

        if(!yamlConfiguration.isConfigurationSection("formats."+formatName)){
            Utils.sendParsedMessage(ctx.getSource().getSender(), configsManager.findConfig("config.yml").getYamlConfiguration().getString("config.language.format_doesnt_exist"));
            return 0;
        }

        String formatPath = "formats."+formatName;

        if(type.equals("join")){
            List<String> joinActions = yamlConfiguration.getStringList(formatPath+".join-actions");
            joinActions.add(action);

            yamlConfiguration.set(formatPath+".join-actions", joinActions);
            configsManager.saveConfigs();

            Utils.sendParsedMessage(ctx.getSource().getSender(), configsManager.findConfig("config.yml").getYamlConfiguration().getString("config.language.format_action_added"));

            configsManager.reloadConfigs();

            return Command.SINGLE_SUCCESS;
        }

        if (type.equals("leave")) {
            List<String> leaveActions =  yamlConfiguration.getStringList(formatPath+".leave-actions");
            leaveActions.add(action);

            yamlConfiguration.set(formatPath+".leave-actions", leaveActions);

            configsManager.saveConfigs();

            configsManager.reloadConfigs();

            Utils.sendParsedMessage(ctx.getSource().getSender(), yamlConfiguration.getString("config.language.format_action_added"));
        }

        return Command.SINGLE_SUCCESS;
    }

    public CompletableFuture<Suggestions> getSuggestions(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder){
        List<Format> formats = formatsManager.getFormats();
        List<String> formatNames = new ArrayList<>();

        formats.forEach(format -> formatNames.add(format.getName()));

        formatNames.stream()
                .filter(userEntry -> userEntry.toLowerCase().startsWith(builder.getRemainingLowerCase()))
                .forEach(builder::suggest);

        return builder.buildFuture();
    }
}
