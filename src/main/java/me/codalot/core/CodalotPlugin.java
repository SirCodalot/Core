package me.codalot.core;

import lombok.Getter;
import me.codalot.core.managers.Manager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class CodalotPlugin extends JavaPlugin implements Manager {

    @Getter
    private static CodalotPlugin instance;

    protected List<Manager> managers;

    @Override
    public void onEnable() {
        instance = this;

        if (managers == null)
            managers = new ArrayList<>();

        load();
    }

    @Override
    public void onDisable() {
        save();
    }

    /* Managers */

    @Override
    public void load() {
        managers.forEach(Manager::load);
    }

    @Override
    public void save() {
        managers.forEach(Manager::save);
    }

    public void reload() {
        save();
        load();
    }

    @SuppressWarnings("unchecked")
    public <T extends Manager> T getManager() {
        for (Manager manager : managers) {
            try {
                return (T) manager;
            } catch (ClassCastException ignored) {}
        }
        return null;
    }
}
