package me.codalot.core.managers.types;

import lombok.Getter;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.commands.Executor;
import me.codalot.core.files.ResourceFile;
import me.codalot.core.managers.Manager;
import me.codalot.core.player.CPlayer;
import me.codalot.core.setup.Message;
import me.codalot.core.setup.MessageType;
import me.codalot.core.setup.TitleMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MessageManager implements Manager {

    private CodalotPlugin plugin;
    private String fileName;

    private Map<String, Message> messages;

    public MessageManager(CodalotPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
    }

    @Override
    @SuppressWarnings("all")
    public void load() {
        messages = new HashMap<>();
        ResourceFile file = new ResourceFile(plugin, fileName);

        for (String key : file.getConfigurationSection("").getKeys(false)) {
            switch (MessageType.from(file.getString(key + ".type"))) {
                case CHAT:
                    messages.put(key, new Message(key, file));
                    break;
                case TITLE:
                    messages.put(key, new TitleMessage(key, file));
                    break;
            }
        }

    }

    public void send(CommandSender sender, String key, String... placeholders) {
        Message message = messages.get(key);
        if (message != null)
            messages.get(key).send(sender, placeholders);
        else
            sender.sendMessage(ChatColor.RED + "Invalid key \"" + key + "\". Please contact an administrator");
    }

    public void send(Executor executor, String key, String... placeholders) {
        send(executor.getSender(), key, placeholders);
    }

    public void send(CPlayer player, String key, String... placeholders) {
        send(player.getPlayer(), key, placeholders);
    }
    
    @Override
    public void save() {

    }
}
