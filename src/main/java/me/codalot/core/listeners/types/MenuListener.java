package me.codalot.core.listeners.types;

import me.codalot.core.CodalotPlugin;
import me.codalot.core.gui.Menu;
import me.codalot.core.listeners.CodalotListener;
import me.codalot.core.managers.types.MenuManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

@SuppressWarnings("unused")
public class MenuListener extends CodalotListener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        MenuManager manager = CodalotPlugin.getInstance().getManager(MenuManager.class);
        if (manager == null)
            return;

        Menu menu = manager.getMenus().get(event.getInventory());
        if (menu == null)
            return;

        menu.onClick(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        MenuManager manager = CodalotPlugin.getInstance().getManager(MenuManager.class);
        if (manager == null)
            return;

        if (manager.getMenus().get(event.getInventory()) == null)
            return;

        manager.getMenus().remove(event.getInventory());
    }

}
