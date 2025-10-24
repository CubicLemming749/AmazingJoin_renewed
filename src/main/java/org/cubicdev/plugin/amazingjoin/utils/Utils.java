package org.cubicdev.plugin.amazingjoin.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.managers.ConfigsManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Utils {

    private static Utils instance;
    private ConfigsManager configsManager;
    private YamlConfiguration yamlConfiguration;

    public Utils(ConfigsManager configsManager){
        this.configsManager = configsManager;
        this.yamlConfiguration = configsManager.findConfig("config.yml").getYamlConfiguration();

        instance = this;
    }

    public static void sendParsedMessage(CommandSender commandSender, String msg){
        if(!(commandSender instanceof Player)){
            Component message = MiniMessage.miniMessage().deserialize(msg, Placeholder.parsed("prefix", instance.yamlConfiguration.getString("config.language.prefix")));
            commandSender.sendMessage(message);
            return;
        }

        Player player;
        player = (Player) commandSender;

        Component message = MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, msg), Placeholder.parsed("prefix", instance.yamlConfiguration.getString("config.language.prefix")));
        player.sendMessage(message);
    }

    public static void sendDebug(String msg){
        boolean isDebug = instance.yamlConfiguration.getBoolean("config.debug");

        if(isDebug){
            AmazingJoin.logger.warning(msg);
        }
    }

    /*
    Credits to the SpigotMC user SirSpoodles for the centered messages code.
     */

    public static String getCenteredMessage(Player player, String message){
        final int CENTER_PX = 154;

        if(message == null || message.equals("")) return "";

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb.toString() + message;
    }

    public static HttpResponse<String> callApi(String stringUrl){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(stringUrl))
                .GET()
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }
}
