package me.codalot.core.commands;

import lombok.Getter;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.utils.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Getter
@SuppressWarnings("unused")
public class Command implements CommandExecutor, TabCompleter {

    private CmdNode node;

    private String[] names;

    public Command(CmdNode node, String... names) {
        this.node = node;
        this.names = names;

    }

    public void register(CodalotPlugin plugin) {
        PluginCommand command;

        try {
            Constructor constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            command = (PluginCommand) constructor.newInstance(names[0], plugin);
        } catch (NoSuchMethodException |InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return;
        }

        command.setExecutor(this);
        command.setTabCompleter(this);

        addToCommandMap(command, names);

    }

    public void unregister() {
        removeFromCommandMap(names);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        node.run(new Executor(sender), args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {

        CmdNode currentNode = node;

        int count;
        for (count = 0; count < args.length - 1; count++) {
            CmdNode nextNode = currentNode.getSubNodes().get(args[count]);

            if (nextNode == null)
                break;

            currentNode = nextNode;
        }

        return CollectionUtils.getMatches(args[args.length - 1],
                currentNode.getCompletionOptions(CollectionUtils.removeFirst(count, args)));
    }

    private static SimpleCommandMap getCommandMap() {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            return (SimpleCommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static void addToCommandMap(PluginCommand command, String... strings) {
        SimpleCommandMap cmdMap = getCommandMap();
        if (cmdMap == null)
            return;

        try {
            Field field = cmdMap.getClass().getSuperclass().getDeclaredField("knownCommands");
            field.setAccessible(true);
            Map<String, org.bukkit.command.Command> commands = (Map<String, org.bukkit.command.Command>) field.get(cmdMap);

            for (String string : strings) {
                commands.put(string, command);
            }

            field.set(cmdMap, commands);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void removeFromCommandMap(String... strings) {
        SimpleCommandMap cmdMap = getCommandMap();
        if (cmdMap == null)
            return;

        try {
            Field field = cmdMap.getClass().getSuperclass().getDeclaredField("knownCommands");
            field.setAccessible(true);
            Map<String, org.bukkit.command.Command> commands = (Map<String, org.bukkit.command.Command>) field.get(cmdMap);

            for (String string : strings) {
                commands.remove(string);
            }

            field.set(cmdMap, commands);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
