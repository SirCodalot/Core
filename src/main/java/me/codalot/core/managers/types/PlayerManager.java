package me.codalot.core.managers.types;

import lombok.Getter;
import me.codalot.core.managers.Manager;
import me.codalot.core.player.CPlayer;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@SuppressWarnings({"WeakerAccess", "unused"})
public class PlayerManager<T extends CPlayer> implements Manager {

    private Class playerClass;

    protected Map<UUID, T> players;

    public PlayerManager(Class<T> playerClass) {
        this.playerClass = playerClass;
    }

    @SuppressWarnings("all")
    private T newInstance(UUID uuid) {
        try {
            return (T) playerClass.getDeclaredConstructor(UUID.class).newInstance(uuid);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("all")
    private T newInstance(UUID uuid, Map<String, Object> map) {
        try {
            return (T) playerClass.getDeclaredConstructor(UUID.class, map.getClass()).newInstance(uuid, map);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void load() {
        players = new HashMap<>();
        Bukkit.getOnlinePlayers().forEach(player -> loadPlayer(player.getUniqueId()));
    }

    @Override
    public void save() {
        new HashMap<>(players).forEach((uuid, player) -> unloadPlayer(player));
        players = null;
    }

    public boolean isLoaded(UUID uuid) {
        return players.containsKey(uuid);
    }

    // TODO load from DB
    public T loadPlayer(UUID uuid) {
        T player = newInstance(uuid);
        players.put(uuid, player);
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
        player.onUnload();
        players.remove(player.getUuid());
    }

}
