package me.codalot.core.player;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

@Getter
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class CPlayer implements ConfigurationSerializable {

    protected UUID uuid;

    public CPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public CPlayer(UUID uuid, Map<String, Object> map) {
        this(uuid);
    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }

    public boolean isOnline() {
        return getPlayer() != null;
    }

    public boolean isOffline() {
        return !isOnline();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public abstract void onUnload();
}
