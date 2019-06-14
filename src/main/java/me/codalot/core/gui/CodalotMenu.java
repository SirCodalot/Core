package me.codalot.core.gui;

import lombok.Getter;
import me.codalot.core.CodalotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Getter
public class CodalotMenu implements InventoryHolder {

    private MenuData data;
    private CodalotMenu previous;

    private Inventory inventory;
    private Page page;

    public CodalotMenu(MenuData data, CodalotMenu previous) {
        this.data = data;
        this.previous = previous;

        inventory = data.createInventory(this);
        page = data.getPage();

        update();
    }

    public CodalotMenu(MenuData data) {
        this(data, null);
    }

    protected void update() {
        page.apply(inventory);
    }

    public void open(Player player) {
        player.openInventory(inventory);
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
        if (!data.isOpenPrevious() || previous == null)
            return;

        final UUID uuid = event.getPlayer().getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);

                if (player == null)
                    return;

                if (player.getOpenInventory().getTopInventory() instanceof PlayerInventory)
                    return;

                previous.open(player);
            }
        }.runTaskLater(plugin, 1);
    }

}
