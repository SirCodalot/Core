package me.codalot.core.listeners;

import me.codalot.core.CodalotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class CodalotListener implements Listener {

    public void register(CodalotPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

}
