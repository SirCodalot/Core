package me.codalot.core;

import lombok.Getter;
import me.codalot.core.commands.CmdNode;
import me.codalot.core.commands.Command;
import me.codalot.core.commands.Executor;
import me.codalot.core.gui.Button;
import me.codalot.core.gui.CodalotMenu;
import me.codalot.core.gui.MenuData;
import me.codalot.core.gui.Page;
import me.codalot.core.listeners.types.MenuListener;
import me.codalot.core.managers.Manager;
import me.codalot.core.managers.types.ListenerManager;
import me.codalot.core.managers.types.MessageManager;
import me.codalot.core.player.CPlayer;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
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

        managers.add(new ListenerManager(this, new MenuListener(this)));

        Page page = new Page();
        page.put(4, new Button(new ItemStack(Material.STONE)));
        MenuData data = new MenuData(InventoryType.CHEST, "test", 3, true, page);
        CodalotMenu menu = new CodalotMenu(data);

        new Command(new CmdNode() {
            @Override
            public void execute(Executor executor, String[] args) {
                menu.open(executor.getPlayer());
            }

            @Override
            public void failure(Executor executor) {

            }
        }, "test").register(this);

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
