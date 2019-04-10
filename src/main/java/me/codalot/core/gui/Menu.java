package me.codalot.core.gui;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Menu {

    protected Player player;

    protected Inventory inventory;

    protected Map<Integer, Button> buttons;

    public Menu(Player player, String title, int rows) {
        this.player = player;
        inventory = Bukkit.createInventory(null, rows * 9, title);
        buttons = new HashMap<>();

        update();

        player.openInventory(inventory);
    }

    protected void update() {
        buttons.forEach((key, button) -> inventory.setItem(key, button.getItem()));
    }

    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (!event.getInventory().equals(inventory))
            return;

        Button button = buttons.get(event.getSlot());

        if (button == null)
            return;

        button.getAction().accept((Player) event.getWhoClicked(), event.getClick());
        update();
    }

}
