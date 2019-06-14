package me.codalot.core.gui.pages;

import me.codalot.core.gui.components.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public class Page extends HashMap<Integer, Button> {

    public Page() {
        super();
    }

    public void apply(Inventory inventory) {
        inventory.clear();
        forEach((slot, button) -> inventory.setItem(slot, button.getItem()));
    }

    public void click(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ClickType type = event.getClick();

        get(event.getSlot()).click(player, type);
    }

    @Override
    @SuppressWarnings("all")
    public Page clone() {
        Page other = new Page();
        forEach((slot, button) -> other.put(slot, button.clone()));
        return other;
    }

    public Button put(String position, Button button) {
        return put(positionToSlot(position), button);
    }

    public Button remove(String position) {
        return remove(positionToSlot(position));
    }

    private static int positionToSlot(String position) {
        String[] split = position.replace(" ", "").split(",");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);

        return y * 9 + x;
    }
}
