package me.codalot.core.gui;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Menu {

    private Player player;

    private Inventory inventory;

    private Map<Integer, Button> buttons;

    public Menu(Player player, String title, int rows) {
        this.player = player;
        inventory = Bukkit.createInventory(null, rows * 9, title);
        buttons = new HashMap<>();

        update();

        player.openInventory(inventory);
    }

    public void update() {
        buttons.forEach((key, button) -> inventory.setItem(key, button.getItem()));
    }

}
