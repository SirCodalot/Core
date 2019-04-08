package me.codalot.core;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class CodalotPlugin extends JavaPlugin {

    @Getter private static CodalotPlugin instance;

    private Manager manager;

    @Override
    public void onEnable() {
        instance = this;

        manager.load();
    }

    @Override
    public void onDisable() {
        manager.save();
    }

}
