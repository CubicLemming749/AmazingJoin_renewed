package org.cubicdev.plugin.amazingjoin.actions.conditions;

/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class CustomConditions {
    String[] possibleOperators;

    public CustomConditions(){
        possibleOperators = new String[]{"!=", ">=", "<=", ">", "<", "="};
    }
    /***
     *
     * @param player The player to check
     * @param condition The condition to check
     * @return If the conditions are true or false.
     */
    public boolean checkConditions(Player player, String condition){
        /*
          Example of a condition: condition;%player_name%=CubicLemming749;message:<red>You're CubicLemming749!
          The action(s) will be only triggered if the condition(s) in the statement is/are true.
        */

        if(!PlaceholderAPI.containsPlaceholders(condition)){
            return false;
        }

        String parsed = PlaceholderAPI.setPlaceholders(player, condition);

        String[] conditions = parsed.split("\\|");

        //Possible operators: =, !=, >, <, >=, <=
        for (String conditionsArray : conditions) {
            if(!evaluate(conditionsArray)){
                return false;
            }
        }

        return true;
    }

    /***
     * This method evaluates a condition statement to determine if it is true.
     * @param condition The condition to check
     * @return If the condition is true or not
     */
    private boolean evaluate(String condition){
        String operator = getOperator(condition);

        if(operator.equals("invalid")){
            return false;
        }

        String[] conditionAsArray = condition.split(operator, 2);

        String realResult = conditionAsArray[0].trim();
        String desiredResult = conditionAsArray[1].trim();

        boolean isNumber = true;
        double realNumericResult = 0;
        double desiredNumericResult = 0;

        try {
            realNumericResult = Double.parseDouble(realResult);
            desiredNumericResult = Double.parseDouble(desiredResult);
        }catch (NumberFormatException e){
            isNumber = false;
        }

        //Check every operator
        switch (operator){
            case "=" -> {
                if(!isNumber){
                    return realResult.equals(desiredResult);
                }

                return realNumericResult == desiredNumericResult;
            }

            case "!=" -> {
                if(!isNumber){
                    return !realResult.equals(desiredResult);
                }

                return realNumericResult != desiredNumericResult;
            }

            case "<" -> {
                if(!isNumber){
                    return false;
                }

                return realNumericResult < desiredNumericResult;
            }

            case ">" -> {
                if(!isNumber){
                    return false;
                }

                return realNumericResult > desiredNumericResult;
            }

            case "<=" -> {
                if(!isNumber){
                    return false;
                }

                return realNumericResult <= desiredNumericResult;
            }

            case ">=" -> {
                if(!isNumber){
                    return false;
                }

                return realNumericResult >= desiredNumericResult;
            }

            default -> {
                return false;
            }
        }
    }

    private String getOperator(String expression){
        String temporal = "invalid";

        for(String operator : possibleOperators){
            if(expression.contains(operator)){
                temporal = operator;
                return temporal;
            }
        }

        return temporal;
    }
}
