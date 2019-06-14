package me.codalot.core.files;

import lombok.Getter;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.utils.CollectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
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

    public String getColoredString(String key, String def) {
        String string = getString(key);
        return string == null ? def : ChatColor.translateAlternateColorCodes('&', string);
    }

    public String getColoredString(String key) {
        return getColoredString(key, null);
    }

    public List<String> getColoredStringlist(String key) {
        List<String> list = new ArrayList<>();

        if (!contains(key))
            return list;

        getStringList(key).forEach(line -> list.add(ChatColor.translateAlternateColorCodes('&', line)));

        return list;
    }

    @SuppressWarnings("all")
    public ItemStack getItem(Material material, String path) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        int amount = getInt(path + ".amount", 1);
        String name = getColoredString(path + ".name");
        List<String> lore = getColoredStringList(path + ".lore");

        item.setAmount(amount);

        if (name != null)
            meta.setDisplayName(name);

        if (lore != null && !lore.isEmpty())
            meta.setLore(lore);

        if (getBoolean(meta + ".glow", false))
            meta.addEnchant(Enchantment.DURABILITY, 1, true);

        meta.setUnbreakable(getBoolean(path + ".unbreakable", false));

        meta.addItemFlags(ItemFlag.values());

        item.setItemMeta(meta);
        return item;
    }

    private static Material getMaterial(String name) {
        try {
            return (Material) Material.class.getDeclaredField(name).get(null);
        } catch (Exception ignored) {}

        return Material.STONE;
    }

    public ItemStack getItem(String path) {
        return getItem(getMaterial(getString(path + ".material", "STONE")), path);
    }

    @SuppressWarnings("all")
    public void setItem(String path, ItemStack item) {
        set(path + ".material", item.getType().toString());

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();

            if (meta.hasDisplayName())
                set(path + ".name", meta.getDisplayName());

            if (meta.hasLore())
                set(path + ".lore", meta.getLore());

            set(path + ".unbreakable", meta.isUnbreakable());
        }

        set(path + ".amount", item.getAmount());
    }

    @SuppressWarnings("all")
    public List<ItemStack> getItemStackList(String key) {
        List<ItemStack> items = new ArrayList<>();

        if (!contains(key))
            return items;

        getMapList(key).forEach(map -> items.add(ItemStack.deserialize((Map<String, Object>) map)));

        return items;
    }

    public List<String> getColoredStringList(String key) {

        if (get(key) instanceof String) {
            List<String> list = new ArrayList<>();
            list.add(getColoredString(key));
            return list;
        } else if (get(key) instanceof List) {
            List<String> list = new ArrayList<>();
            getStringList(key).forEach(line -> list.add(ChatColor.translateAlternateColorCodes('&', line)));
            return list;
        }

        return null;
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