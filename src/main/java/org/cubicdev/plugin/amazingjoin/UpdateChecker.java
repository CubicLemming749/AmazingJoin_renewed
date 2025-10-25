package org.cubicdev.plugin.amazingjoin;

/*
 * Copyright (c) 2025 CubicLemming749
 * Licensed under the MIT License.
 */

import org.cubicdev.plugin.amazingjoin.utils.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.http.HttpResponse;

public class UpdateChecker {
    public static String API_URL;
    public static boolean IS_NEW_VERSION;
    public static String NEW_VERSION;

    public UpdateChecker(){
        IS_NEW_VERSION = false;
        NEW_VERSION = AmazingJoin.PLUGIN_VERSION;
        API_URL = "https://api.modrinth.com/v2/project/amazingjoin/version";
    }

    public void checkVersion(){
        try {
            HttpResponse<String> response = Utils.callApi(API_URL);

            if(response.statusCode() != 200){
                return;
            }

            String responseBody = response.body();

            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(responseBody);
            JSONObject finalJson = (JSONObject) jsonArray.getFirst();

            String version = (String) finalJson.get("version_number");

            if(!version.equals(AmazingJoin.PLUGIN_VERSION)){
                IS_NEW_VERSION = true;
                NEW_VERSION = version;
            }
        } catch (ParseException e) {
            //nothing
        }
    }
}
