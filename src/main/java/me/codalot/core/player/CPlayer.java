package me.codalot.core.player;

import lombok.Getter;
import me.codalot.core.files.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@SuppressWarnings("unused")
public abstract class CPlayer implements ConfigurationSerializable {

    protected UUID uuid;
    protected YamlFile file;

    protected Injector injector;

    public CPlayer(UUID uuid, YamlFile file) {
        this.uuid = uuid;
        this.file = file;

        injector = new Injector(this);
    }

    @Override
    @SuppressWarnings("all")
    public Map<String, Object> serialize() {
        return new HashMap<>();
    }

    public void save() {
        file.set(serialize());
        file.save();
    }

    public String toPlaceholder() {
        return "name:" + getOfflinePlayer().getName();
    }

    public void onJoin() {
        injector.inject();
    }

    public void onQuit() {
        injector.eject();
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

    public void unload() {

    }
}
