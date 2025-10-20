package org.cubicdev.plugin.amazingJoin.actions;

import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.utils.Utils;

public abstract class Action {
    protected AmazingJoin main;
    protected String actionName;
    protected String[] args;

    public Action(AmazingJoin main, String actionName){
        this.main = main;
        this.actionName = actionName;
    }

    public abstract void execute(Player player, String args);
    public void log(Player player, String args){
        Utils.sendDebug("Executed action of type "+actionName+" for player "+player.getName()+" with the following arguments: "+args);
    }
}
