package me.codalot.core;

import lombok.Getter;
import me.codalot.core.listeners.types.CPlayerListener;
import me.codalot.core.managers.Manager;
import me.codalot.core.managers.types.ListenerManager;
import me.codalot.core.managers.types.PlayerManager;
import me.codalot.core.player.CPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
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

        managers.add(new PlayerManager<>(this, TestPlayer.class));
        managers.add(new ListenerManager(this, new CPlayerListener(this)));

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
    public <T extends Manager> T getManager(Class<T> clazz) {
        for (Manager manager : managers) {
            if (manager.getClass().isAssignableFrom(clazz))
                return (T) manager;
        }
        return null;
    }
}
