package me.codalot.core;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class CodalotPlugin extends JavaPlugin {

    @Getter
    private static CodalotPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

    }

}
