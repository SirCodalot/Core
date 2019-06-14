package me.codalot.core.gui;

import lombok.Getter;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.gui.data.MenuData;
import me.codalot.core.gui.pages.Page;
import me.codalot.core.utils.SoundUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class CodalotMenu implements InventoryHolder {

    private MenuData data;
    private CodalotMenu previous;

    private Inventory inventory;
    private Page page;

    public CodalotMenu(MenuData data) {
        this.data = data;
        this.previous = null;

        inventory = data.createInventory(this);
        page = data.getPage();

        update();
    }

    protected void update() {
        page.apply(inventory);
    }

    public void open(Player player, boolean switched) {
        if (switched)
            SoundUtils.play(player, data.getSwitchSound());
        else
            SoundUtils.play(player, data.getOpenSound());

        player.openInventory(inventory);
    }

    public void open(Player player) {
        boolean switched = false;

        if (isInCodalotMenu(player)) {
            if (previous == null)
                previous = (CodalotMenu) player.getOpenInventory().getTopInventory().getHolder();
            switched = true;
        }

        open(player, switched);
    }

    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(inventory))
            return;

        if (event.getCurrentItem() == null || event.getCurrentItem().getType().toString().contains("AIR"))
            return;

        page.click(event);
    }

    public void onInventoryClose(InventoryCloseEvent event, CodalotPlugin plugin) {
        Player player = (Player) event.getPlayer();

        boolean canGoBack = previous != null && data.isOpenPrevious();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline())
                    return;

                if (!canGoBack) {
                    if (!isInCodalotMenu(player))
                        SoundUtils.play(player, data.getCloseSound());
                    return;
                }

                if (!isInCodalotMenu(player))
                    previous.open(player, true);

            }
        }.runTaskLater(plugin, 1);

    }

    private static boolean hasOpenInventory(Player player) {
        return player.getOpenInventory().getTopInventory().getType() != InventoryType.CRAFTING;
    }

    private static boolean isInCodalotMenu(Player player) {
        return player.getOpenInventory().getTopInventory().getHolder() instanceof CodalotMenu;
    }

}
