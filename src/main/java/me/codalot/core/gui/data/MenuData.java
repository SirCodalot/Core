package me.codalot.core.gui.data;

import lombok.Getter;
import me.codalot.core.gui.CodalotMenu;
import me.codalot.core.gui.pages.Page;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

@Getter
public class MenuData {

    private String title;
    private int rows;
    @Getter private boolean openPrevious;
    private Page page;

    private String openSound;
    private String closeSound;
    private String switchSound;

    public MenuData(String title, int rows, boolean openPrevious, Page page, String openSound, String closeSound, String switchSound) {
        this.title = title;
        this.rows = rows;
        this.openPrevious = openPrevious;
        this.page = page;

        this.openSound = openSound;
        this.closeSound = closeSound;
        this.switchSound = switchSound;
    }

    public MenuData(String title, int rows, boolean openPrevious, Page page) {
        this(title, rows, openPrevious, page, null, null, null);
    }

    public Inventory createInventory(CodalotMenu menu) {
        return Bukkit.createInventory(menu, rows * 9, title);
    }

    public Page getPage() {
        return page.clone();
    }

}
