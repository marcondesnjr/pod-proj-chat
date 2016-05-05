/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.util;

import com.google.common.collect.HashBiMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class StringCommand {

    public static Map<String, String> convert(String params) {
        Map<String, String> map = new HashMap<>();

        String commandSplit[] = params.split("\\?");
        map.put("command", commandSplit[0]);

        String[] paramsSpit = commandSplit[1].split("&");
        for (String param : paramsSpit) {
            String[] keyValue = param.split("=");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }
}
