package me.codalot.core.gui;

import lombok.Getter;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.managers.types.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

@Getter
@SuppressWarnings("WeakerAccess")
public class Menu {

    protected Player player;

    protected Inventory inventory;

    protected Map<Integer, Button> buttons;

    public Menu(CodalotPlugin plugin, Player player, String title, int rows) {
        this.player = player;
        inventory = Bukkit.createInventory(null, rows * 9, title);
        buttons = new HashMap<>();

        plugin.getManager(MenuManager.class).register(this);

        update();
        player.openInventory(inventory);
    }

    public void open() {
        player.openInventory(inventory);
    }

    protected void update() {
        inventory.clear();
        buttons.forEach((key, button) -> inventory.setItem(key, button.getItem()));
    }

    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (!event.getClickedInventory().equals(inventory))
            return;

        Button button = buttons.get(event.getSlot());
        if (button == null)
            return;

        if (button.getAction() != null)
            button.getAction().accept((Player) event.getWhoClicked(), event.getClick());

        update();
    }

    private int coordsToSlot(int x, int y) {
        return y * 9 + x;
    }

    public void setButton(int x, int y, Button button) {
        int slot = coordsToSlot(x, y);
        buttons.put(slot, button);
    }

    public void removeButton(int x, int y) {
        buttons.remove(coordsToSlot(x, y));
    }

}
