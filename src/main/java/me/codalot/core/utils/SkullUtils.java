package me.codalot.core.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@SuppressWarnings({"WeakerAccess", "unused"})
public class SkullUtils {

    private static final String MINESKIN_URL = "http://api.mineskin.org/generate/url?url=";

    public static ItemStack getSkull(String image) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(MINESKIN_URL + image);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
            stream.flush();
            stream.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String value;
        String signature;

        try {
            JSONObject json = (JSONObject) new JSONParser().parse(response.toString());

            json = (JSONObject) json.get("data");
            json = (JSONObject) json.get("texture");

            value = (String) json.get("value");
            signature = (String) json.get("signature");

        } catch (ParseException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }

        return getSkull(value, signature);
    }

    public static ItemStack getSkull(String value, String signature) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", value, signature));

        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        
        try {
            Field field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        item.setItemMeta(meta);

        return item;
    }

}
