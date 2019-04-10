package me.codalot.core.gui;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class Button {

    private ItemStack item;

    public Button(ItemStack item) {
        this.item = item;
    }

    public abstract void onClick(Player player, ClickType type);

    public void setItem(ItemStack item) {
        this.item.setType(item.getType());
        this.item.setData(item.getData());
        this.item.setAmount(item.getAmount());
        this.item.setItemMeta(item.getItemMeta());
    }

}
