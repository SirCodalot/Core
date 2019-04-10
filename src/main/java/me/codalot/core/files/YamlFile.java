package me.codalot.core.files;

import lombok.Getter;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.utils.CollectionUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
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

        create();
    }

    @SuppressWarnings("all")
    protected void create() {
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
        create();
    }

    public Map<String, Object> asMap() {
        return CollectionUtils.toMap(getConfigurationSection(""), true);
    }

    public void set(Map<String, Object> map) {
        set("", map);
    }
}
