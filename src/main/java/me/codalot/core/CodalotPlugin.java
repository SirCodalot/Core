package me.codalot.core;

import lombok.Getter;
import me.codalot.core.managers.Manager;
import me.codalot.core.utils.SkullUtils;
import org.bukkit.Bukkit;
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

        Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().addItem(SkullUtils.getSkull("https://i.imgur.com/Xf4x9fu.png")));

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
