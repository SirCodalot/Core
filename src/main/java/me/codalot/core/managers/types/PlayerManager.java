package me.codalot.core.managers.types;

import lombok.Getter;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.files.YamlFile;
import me.codalot.core.managers.Manager;
import me.codalot.core.player.CPlayer;
import org.bukkit.Bukkit;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

@Getter
@SuppressWarnings({"WeakerAccess", "unused"})
public class PlayerManager<T extends CPlayer> implements Manager {

    private CodalotPlugin plugin;

    private Class playerClass;

    protected Map<UUID, T> players;

    protected File folder;

    public PlayerManager(CodalotPlugin plugin, Class<T> playerClass) {
        this.plugin = plugin;
        this.playerClass = playerClass;
        folder = new File(plugin.getDataFolder(), "players");
    }

    @SuppressWarnings("all")
    private T newInstance(UUID uuid, YamlFile file) {
        try {
            return (T) playerClass.getDeclaredConstructor(UUID.class, YamlFile.class).newInstance(uuid, file);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("all")
    public void load() {
        players = new HashMap<>();

        if (!folder.exists())
            folder.mkdirs();

//        for (File file : folder.listFiles()) {
//            if (file.getName().endsWith(".yml"))
//                loadPlayer(UUID.fromString(file.getName().replace(".yml", "")));
//        }

        Bukkit.getOnlinePlayers().forEach(player -> loadPlayer(player.getUniqueId()));
    }

    @Override
    public void save() {
        new HashSet<>(players.values()).forEach(this::unloadPlayer);
        players = null;
    }

    public boolean isLoaded(UUID uuid) {
        return players.containsKey(uuid);
    }

    public T loadPlayer(UUID uuid) {
        YamlFile file = new YamlFile(plugin, uuid.toString() + ".yml", folder);
        T player = newInstance(uuid, file);
        players.put(uuid, player);

        if (player.isOnline())
            player.onJoin();

        return player;
    }

    public T getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public T getOrLoad(UUID uuid) {
        return isLoaded(uuid) ? getPlayer(uuid) : loadPlayer(uuid);
    }

    public void unloadPlayer(UUID uuid) {
        unloadPlayer(getPlayer(uuid));
    }

    public void unloadPlayer(T player) {
        if (player.isOnline())
            player.onQuit();

        player.unload();
        player.save();
        players.remove(player.getUuid());
    }

}
