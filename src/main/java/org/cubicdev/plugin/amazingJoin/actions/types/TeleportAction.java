package org.cubicdev.plugin.amazingJoin.actions.types;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.actions.Action;

public class TeleportAction extends Action {
    //This actions makes the target player to teleport to specific coordinates
    public TeleportAction(AmazingJoin main) {
        super(main, "teleport");
    }

    @Override
    public void execute(Player player, String args) {
        String[] parameters = args.split(";");

        if (parameters.length > 6 || parameters.length < 4) {
            return;
        }

        World world = Bukkit.getWorld(parameters[0]);
        double x = Double.parseDouble(parameters[1]);
        double y = Double.parseDouble(parameters[2]);
        double z = Double.parseDouble(parameters[3]);

        if(parameters.length == 4){
            Location location = new Location(world, x, y, z);
            player.teleport(location);
            return;
        }

        float yaw = Float.parseFloat(parameters[4]);
        float pitch = Float.parseFloat(parameters[5]);

        Location location = new Location(world, x, y, z, yaw, pitch);
        player.teleport(location);

        //Example: world;30;-10;545;-84;1
    }
}
