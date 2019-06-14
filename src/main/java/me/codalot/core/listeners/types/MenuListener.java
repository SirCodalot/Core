package me.codalot.core.listeners.types;


import me.codalot.core.CodalotPlugin;
import me.codalot.core.gui.CodalotMenu;
import me.codalot.core.listeners.CodalotListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

@SuppressWarnings("unused")
public class MenuListener extends CodalotListener {

    private CodalotPlugin plugin;

    public MenuListener(CodalotPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        CodalotMenu menu = getMenu(event.getInventory());

        if (menu != null)
            menu.onInventoryClick(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        CodalotMenu menu = getMenu(event.getInventory());

        if (menu != null)
            menu.onInventoryClose(event, plugin);
    }

    private CodalotMenu getMenu(Inventory inventory) {
        if (inventory.getHolder() instanceof CodalotMenu)
            return (CodalotMenu) inventory.getHolder();
        else
            return null;
    }

}
