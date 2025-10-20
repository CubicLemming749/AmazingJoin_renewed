package org.cubicdev.plugin.amazingJoin.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.actions.ActionType;
import org.cubicdev.plugin.amazingJoin.formats.Format;
import org.cubicdev.plugin.amazingJoin.managers.ConfigsManager;
import org.cubicdev.plugin.amazingJoin.managers.FormatsManager;
import org.cubicdev.plugin.amazingJoin.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class AmazingJoinCommand {
    AmazingJoin main;
    FormatsManager formatsManager;
    ConfigsManager configsManager;

    public AmazingJoinCommand(AmazingJoin main, FormatsManager formatsManager, ConfigsManager configsManager){
        this.main = main;
        this.formatsManager = formatsManager;
        this.configsManager = configsManager;
    }

    public void setupCommand(){
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("amazingjoin")
                .requires(executor -> executor.getExecutor().hasPermission("amazingjoin.command"))
                .then(Commands.literal("create").
                        requires(executor -> executor.getExecutor().hasPermission("amazingjoin.command.create")).
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
                        .requires(executor -> executor.getExecutor().hasPermission("amazingjoin.command.test"))
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
                                            formatsManager.executeFormatForPlayer(player, formatToTest, ActionType.ENTER);
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
                                            formatsManager.executeFormatForPlayer(player, formatToTest, ActionType.LEAVE);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                )

                .then(Commands.literal("addaction")
                        .then(Commands.argument("format", StringArgumentType.word())
                                .requires(executor -> executor.getExecutor().hasPermission("amazingjoin.command.addaction"))
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

                .then(Commands.literal("reload")
                        .requires(executor -> executor.getExecutor().hasPermission("amazingjoin.command.reload"))
                        .executes(commandContext -> {
                            configsManager.reloadConfigs();
                            formatsManager.reloadFormats();

                            new Utils(configsManager);

                            Utils.sendParsedMessage(commandContext.getSource().getSender(), configsManager.findConfig("config.yml").getYamlConfiguration().getString("config.language.reload_success"));
                            return Command.SINGLE_SUCCESS;
                }));

        LiteralCommandNode<CommandSourceStack> command = root.build();

        this.main.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(command);
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
