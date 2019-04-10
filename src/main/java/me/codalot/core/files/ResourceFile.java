package me.codalot.core.files;

import me.codalot.core.CodalotPlugin;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public class ResourceFile extends YamlFile {

    public ResourceFile(CodalotPlugin plugin, String name) {
        super(plugin, name, plugin.getDataFolder());
    }

    @Override
    @SuppressWarnings("all")
    protected void create() {
        file = new File(folder, name);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(name, false);
        }

        try {
            load(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

}
