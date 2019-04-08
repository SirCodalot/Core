package me.codalot.core.utils;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class CollectionUtils {

    public static Map<String, Object> toMap(ConfigurationSection section, boolean deep) {
        Map<String, Object> map = new HashMap<>();

        for (String key : section.getKeys(false)) {
            Object value = section.get(key);

            if (deep && value instanceof ConfigurationSection)
                map.put(key, toMap((ConfigurationSection) value, true));
            else
                map.put(key, value);

        }

        return map;
    }

}
