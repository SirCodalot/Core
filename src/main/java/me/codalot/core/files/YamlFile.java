package me.codalot.core.files;

import lombok.Getter;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.utils.CollectionUtils;
import me.codalot.core.utils.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@SuppressWarnings({"WeakerAccess", "unused"})
public class YamlFile extends YamlConfiguration {

    protected String name;

    protected File folder;
    protected File file;

    protected CodalotPlugin plugin;

    public YamlFile(CodalotPlugin plugin, String name, File folder) {
        this.name = name + ".yml";
        this.folder = folder;
        this.plugin = plugin;

        load();
    }

    @SuppressWarnings("all")
    public void load() {
        file = new File(folder, name);

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            load(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            options().indent(2);
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        save();
        load();
    }

    public String getColoredString(String key) {
        String string = getString(key);
        return string == null ? null : ChatColor.translateAlternateColorCodes('&', string);
    }

    public List<String> getColoredStringlist(String key) {
        List<String> list = new ArrayList<>();

        if (!contains(key))
            return list;

        getStringList(key).forEach(line -> list.add(ChatColor.translateAlternateColorCodes('&', line)));

        return list;
    }

    @SuppressWarnings("all")
    public ItemStack getItemStack(String key) {
        ConfigurationSection section = getConfigurationSection(key);

        XMaterial material = XMaterial.valueOf(section.getString("material"));
        String name = getColoredString(key + ".name");
        List<String> lore = getColoredStringlist(key + ".lore");

        ItemStack item = material.parseItem();
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }

    @SuppressWarnings("all")
    public List<ItemStack> getItemStackList(String key) {
        List<ItemStack> items = new ArrayList<>();

        if (!contains(key))
            return items;

        getMapList(key).forEach(map -> items.add(ItemStack.deserialize((Map<String, Object>) map)));

        return items;
    }

    @SuppressWarnings("all")
    public Map<String, Object> getMap(String key) {
        return CollectionUtils.toMap(getConfigurationSection(key), true);
    }

    @SuppressWarnings("all")
    public Map<String, Object> asMap() {
        return CollectionUtils.toMap(getConfigurationSection(""), true);
    }

    public void set(Map<String, Object> map) {
        map.forEach(this::set);
    }
}