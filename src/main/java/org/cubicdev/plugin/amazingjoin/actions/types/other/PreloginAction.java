package org.cubicdev.plugin.amazingjoin.actions.types.other;

import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;

public class PreloginAction extends Action {
    private Action action;

    public PreloginAction(AmazingJoin main, Action action) {
        super(main, "condition");
        this.action = action;
    }

    @Override
    public void execute(Player player, String args) {
        action.execute(player, args);
    }

    @Override
    public void log(Player player, String args){
        action.log(player, args);
    }

    public Action getAction() {
        return action;
    }

    //Example: prelogin;message;<red>You're not logged.
}
