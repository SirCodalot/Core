package me.codalot.core.managers.types;

import lombok.Getter;
import me.codalot.core.gui.Menu;
import me.codalot.core.managers.Manager;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MenuManager implements Manager {

    public Map<Inventory, Menu> menus;

    @Override
    public void load() {
        menus = new HashMap<>();
    }

    @Override
    public void save() {
        menus.values().forEach(menu -> menu.getPlayer().closeInventory());
    }

}
