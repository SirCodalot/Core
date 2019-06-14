package me.codalot.core.gui;

import lombok.Getter;
import lombok.Setter;
import me.codalot.core.utils.PlaceholderUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Getter
@Setter
public class Button {

    private ItemStack item;
    private BiConsumer<Player, ClickType> action;

    private String permission;
    private String noPermissionMessage;

    private List<String> commands;
    private boolean console;


    public Button(ItemStack item, BiConsumer<Player, ClickType> action) {
        this.item = item;
        this.action = action;

        this.permission = null;
        this.noPermissionMessage = null;

        this.commands = new ArrayList<>();
        this.console = true;
    }

    public Button(ItemStack item) {
        this(item, (player, type) -> {});
    }

    public void click(Player player, ClickType type) {
        if (permission != null && !player.hasPermission(permission)) {
            if (noPermissionMessage != null)
                player.sendMessage(noPermissionMessage);

            return;
        }

        action.accept(player, type);
        executeCommands(player);
    }

    private void executeCommands(Player player) {
        if (commands.isEmpty())
            return;

        CommandSender sender = console ? Bukkit.getConsoleSender() : player;

        PlaceholderUtils.apply(commands, "name:" + player.getName())
                .forEach(command -> Bukkit.dispatchCommand(sender, command));
    }

    public void applyPlaceholders(String... placeholders) {
        PlaceholderUtils.apply(item, placeholders);
    }

    @Override
    @SuppressWarnings("all")
    protected Button clone() {
        Button other = new Button(item.clone(), action);

        other.setPermission(permission);
        other.setNoPermissionMessage(noPermissionMessage);

        other.setCommands(commands);
        other.setConsole(console);

        return other;
    }
}
