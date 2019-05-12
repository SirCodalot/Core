package me.codalot.core;

import lombok.Getter;
import me.codalot.core.commands.CmdNode;
import me.codalot.core.commands.Command;
import me.codalot.core.commands.Executor;
import me.codalot.core.managers.Manager;
import me.codalot.core.managers.types.MessageManager;
import me.codalot.core.setup.Message;
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

    public Message getMessage(String key) {
        return getManager(MessageManager.class).getMessages().get(key);
    }
}
