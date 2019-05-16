package me.codalot.core.setup;

import me.codalot.core.files.YamlFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TitleMessage extends Message {

    private int fadeIn;
    private int duration;
    private int fadeOut;

    public TitleMessage(String name, YamlFile file) {
        super(name, file);

        fadeIn = file.getInt(name + ".fade_in", 5);
        duration = file.getInt(name + ".duration", 40);
        fadeOut = file.getInt(name + ".fade_out", 5);
    }

    @Override
    public void send(CommandSender sender, String... placeholders) {
        if (sender instanceof Player) {
            List<String> message = get(placeholders);
            ((Player) sender).sendTitle(message.get(0), message.size() > 1 ? message.get(1) : "", fadeIn, duration, fadeOut);
        } else
            super.send(sender, placeholders);
    }
}
