package me.codalot.core.listeners.types;

import me.codalot.core.CodalotPlugin;
import me.codalot.core.listeners.CodalotListener;
import me.codalot.core.managers.types.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("unused")
public class CPlayerListener extends CodalotListener {

    private PlayerManager manager;

    public CPlayerListener(CodalotPlugin plugin) {
        manager = plugin.getManager(PlayerManager.class);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        manager.loadPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        manager.unloadPlayer(event.getPlayer().getUniqueId());
    }
}
