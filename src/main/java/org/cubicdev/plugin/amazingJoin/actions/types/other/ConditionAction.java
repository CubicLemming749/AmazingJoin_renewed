package org.cubicdev.plugin.amazingJoin.actions.types.other;

import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingJoin.AmazingJoin;
import org.cubicdev.plugin.amazingJoin.actions.Action;
import org.cubicdev.plugin.amazingJoin.actions.conditions.CustomConditions;

public class ConditionAction extends Action {
    CustomConditions customConditions;
    Action action;

    //This is a special action, used to check for placeholders using the condition statements.
    public ConditionAction(AmazingJoin main, Action action) {
        super(main, "condition");
        this.action = action;
        this.customConditions = new CustomConditions();
    }

    @Override
    public void execute(Player player, String args) {
        action.execute(player, args);
    }

    public boolean check(Player player, String args){
        return customConditions.checkConditions(player, args);
    }
}
