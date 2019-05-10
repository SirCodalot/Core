package me.codalot.core;

import lombok.Getter;
import me.codalot.core.commands.CmdNode;
import me.codalot.core.commands.Command;
import me.codalot.core.commands.Executor;
import me.codalot.core.files.ResourceFile;
import me.codalot.core.gui.YamlMenu;
import me.codalot.core.listeners.types.MenuListener;
import me.codalot.core.managers.Manager;
import me.codalot.core.managers.types.ListenerManager;
import me.codalot.core.managers.types.MenuManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

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

        managers.add(new MenuManager());
        managers.add(new ListenerManager(this, new MenuListener()));

        ResourceFile file = new ResourceFile(instance, "menu");


        load();

        new Command(new CmdNode() {

            @Override
            public void execute(Executor executor, String[] args) {
                YamlMenu menu = new YamlMenu(instance, executor.getPlayer(), file);
                menu.addAction("jump", (clicker, type) -> clicker.setVelocity(new Vector(0, 3, 0)));
                menu.load();
                menu.open();
            }

            @Override
            public void failure(Executor executor) {

            }

        }, "test").register(this);
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
