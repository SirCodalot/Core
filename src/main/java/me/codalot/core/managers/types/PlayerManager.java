package me.codalot.core.managers.types;

import lombok.Getter;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.files.YamlFile;
import me.codalot.core.managers.Manager;
import me.codalot.core.player.CPlayer;
import me.codalot.core.utils.MojangUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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

    public Set<T> loadAllPlayers() {
        Set<T> players = new HashSet<>();
        for (File file : folder.listFiles()) {
            UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
            players.add(getOrLoad(uuid));
        }
        return players;
    }

    public boolean isLoaded(UUID uuid) {
        return players.containsKey(uuid);
    }

    private boolean isCreated(UUID uuid) {
        return new File(folder, uuid.toString() + ".yml").exists();
    }

    @SuppressWarnings("all")
    public T loadPlayer(UUID uuid, boolean create) {
        if (!create && !isCreated(uuid))
            return null;

        YamlFile file = new YamlFile(plugin, uuid.toString(), folder);
        T player = newInstance(uuid, file);
        players.put(uuid, player);

        if (player.isOnline())
            player.onJoin();

        return player;
    }

    public T loadPlayer(UUID uuid) {
        return loadPlayer(uuid, true);
    }

    public T getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public T getPlayer(String name) {
        UUID uuid = getUuid(name);
        return uuid == null ? null : getPlayer(uuid);
    }

    public T getOrLoad(UUID uuid, boolean create) {
        return isLoaded(uuid) ? getPlayer(uuid) : loadPlayer(uuid, create);
    }

    public T getOrLoad(UUID uuid) {
        return getOrLoad(uuid, true);
    }

    public T getOrLoad(String name, boolean create) {
        UUID uuid = getUuid(name);
        return uuid == null ? null : getOrLoad(uuid, create);
    }

    public T getOrLoad(String name) {
        return getOrLoad(name, true);
    }

    public void unloadPlayer(T player) {
        if (player.isOnline())
            player.onQuit();

        player.unload();
        player.save();
        players.remove(player.getUuid());
    }

    public void unloadPlayer(UUID uuid) {
        unloadPlayer(getPlayer(uuid));
    }

    public void unloadIfOffline(T player) {
        if (player.isOffline())
            unloadPlayer(player);
    }

    private static UUID getUuid(String name) {
        try {
            return MojangUtils.fetchUuid(name);
        } catch (Exception e) {
            return null;
        }
    }

}
