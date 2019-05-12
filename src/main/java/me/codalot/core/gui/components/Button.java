package me.codalot.core.gui.components;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

@Getter
@SuppressWarnings("unused")
public class Button {

    protected ItemStack item;

    protected BiConsumer<? super Player, ? super ClickType> action;

    public Button(ItemStack item, BiConsumer<? super Player, ? super ClickType> action) {
        this.item = item;
        this.action = action;
    }

    public void setItem(ItemStack item) {
        this.item.setType(item.getType());
        this.item.setData(item.getData());
        this.item.setAmount(item.getAmount());
        this.item.setItemMeta(item.getItemMeta());
    }

}
