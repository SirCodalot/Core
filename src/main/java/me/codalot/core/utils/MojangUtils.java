package me.codalot.core.utils;

import org.apache.logging.log4j.core.util.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MojangUtils {

    private final static String API_URL = "https://api.mojang.com/users/profiles/minecraft/";

    private static Map<String, UUID> uuids = new HashMap<>();

    public static UUID fetchUuid(String name) throws IOException {
        name = name.toLowerCase();

        if (uuids.containsKey(name))
            return uuids.get(name);

        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            uuids.put(name, player.getUniqueId());
            return player.getUniqueId();
        }

        try (InputStream input = new URL(API_URL + name).openStream()) {
            String json = IOUtils.toString(new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8"))));

            if (json.isEmpty()) {
                input.close();
                return null;
            }

            JSONObject object = (JSONObject) JSONValue.parseWithException(json);

            UUID uuid = UUID.fromString(object.get("id").toString().replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
            ));

            uuids.put(name, uuid);
            return uuid;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
