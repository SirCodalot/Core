package me.codalot.core.setup;

import lombok.Getter;
import me.codalot.core.commands.Executor;
import me.codalot.core.files.YamlFile;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuppressWarnings("unused")
public class Message {

    protected List<String> message;

    public Message(String name, YamlFile file) {
        load(name, file);
    }

    public void load(String name, YamlFile file) {
        message = file.getColoredStringList(name);

        if (message == null)
            file.getColoredStringList(name + ".message");
    }

    public List<String> get(String... placeholders) {
        List<String> message = new ArrayList<>();

        for (String line : this.message) {
            for (String placeholder : placeholders)
                line = line.replace(placeholder.split(":")[0], placeholder.split(":", 2)[1]);

            message.add(line);
        }

        return message;
    }

    public void send(CommandSender sender, String... placeholders) {
        get(placeholders).forEach(sender::sendMessage);
    }

    public void send(Executor executor, String... placeholders) {
        send(executor.getSender());
    }
}
