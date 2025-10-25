/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

package org.cubicdev.plugin.amazingjoin.actions.types.other;

import org.bukkit.entity.Player;
import org.cubicdev.plugin.amazingjoin.AmazingJoin;
import org.cubicdev.plugin.amazingjoin.actions.Action;
import org.cubicdev.plugin.amazingjoin.actions.conditions.CustomConditions;

public class ConditionAction extends Action {
    private CustomConditions customConditions;
    private Action action;

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
