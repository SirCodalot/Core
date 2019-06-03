package me.codalot.core;

import lombok.Getter;
import me.codalot.core.commands.Executor;
import me.codalot.core.managers.Manager;
import me.codalot.core.managers.types.MessageManager;
import me.codalot.core.player.CPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class CodalotPlugin extends JavaPlugin implements Manager {

    @Getter
    protected static CodalotPlugin instance;

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

    public void send(CommandSender sender, String key, String... placeholders) {
        getManager(MessageManager.class).send(sender, key, placeholders);
    }

    public void send(Executor executor, String key, String... placeholders) {
        getManager(MessageManager.class).send(executor, key, placeholders);
    }

    public void send(CPlayer player, String key, String... placeholders) {
        getManager(MessageManager.class).send(player, key, placeholders);
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
