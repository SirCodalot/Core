package me.codalot.core.gui.components;

import lombok.Getter;
import lombok.Setter;
import me.codalot.core.utils.PlaceholderUtils;
import me.codalot.core.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;

@Getter
@Setter
public class Button {

    private ItemStack item;
    private BiConsumer<Player, ClickType> action;

    private String permission;
    private String noPermissionMessage;

    private String noPermissionSound;
    private String sound;

    private List<String> commands;
    private boolean console;


    public Button(ItemStack item, BiConsumer<Player, ClickType> action) {
        this.item = item;
        this.action = action;

        this.permission = null;
        this.noPermissionMessage = null;

        this.noPermissionSound = null;
        this.sound = null;

        this.commands = new ArrayList<>();
        this.console = true;

    }

    public Button(ItemStack item) {
        this(item, (player, type) -> {});
    }

    @SuppressWarnings("all")
    public Button(Map<String, ItemStack> items, Map<String, BiConsumer<Player, ClickType>> actions, Map<String, Object> map) {
        item = items.get((String) map.get("item")).clone();
        action = actions.get((String) map.getOrDefault("action", ""));
        if (action == null)
            action = (player, type) -> {};

        permission = (String) map.get("permission");
        noPermissionMessage = map.containsKey("no-permission-message") ?
                ChatColor.translateAlternateColorCodes('&', (String) map.get("no-permission-message")) :
                null;

        noPermissionSound = (String) map.get("no-permission-sound");
        sound = (String) map.get("sound");

        commands = map.containsKey("commands") ? (List<String>) map.get("commands") : new ArrayList<>();
        console = (boolean) map.getOrDefault("console", true);
    }

    public void click(Player player, ClickType type) {
        if (permission != null && !player.hasPermission(permission)) {
            if (noPermissionMessage != null)
                player.sendMessage(noPermissionMessage);

            SoundUtils.play(player, noPermissionSound);

            return;
        }

        SoundUtils.play(player, sound);

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
    public Button clone() {
        Button other = new Button(item.clone(), action);

        other.setPermission(permission);
        other.setNoPermissionMessage(noPermissionMessage);

        other.setCommands(commands);
        other.setConsole(console);

        other.setSound(sound);

        return other;
    }
}
