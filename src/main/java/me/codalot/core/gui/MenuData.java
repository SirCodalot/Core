package me.codalot.core.gui;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class MenuData {

    private InventoryType type;
    private String title;
    private int rows;
    @Getter private boolean openPrevious;
    private Page page;

    public MenuData(InventoryType type, String title, int rows, boolean openPrevious, Page page) {
        this.type = type;
        this.title = title;
        this.rows = rows;
        this.openPrevious = openPrevious;
        this.page = page;
    }

    public Inventory createInventory(CodalotMenu menu) {
        if (type == InventoryType.CHEST)
            return Bukkit.createInventory(menu, rows * 9, title);
        else
            return Bukkit.createInventory(menu, type, title);
    }

    public Page getPage() {
        return page.clone();
    }

}
